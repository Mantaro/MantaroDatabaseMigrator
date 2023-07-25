package net.kodehawa.migrator;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.connection.ConnectionPoolSettings;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Result;
import net.kodehawa.migrator.mongodb.CustomCommand;
import net.kodehawa.migrator.mongodb.MantaroObject;
import net.kodehawa.migrator.mongodb.MongoGuild;
import net.kodehawa.migrator.mongodb.ManagedMongoObject;
import net.kodehawa.migrator.mongodb.Marriage;
import net.kodehawa.migrator.mongodb.Player;
import net.kodehawa.migrator.mongodb.PlayerStats;
import net.kodehawa.migrator.mongodb.PremiumKey;
import net.kodehawa.migrator.mongodb.MongoUser;
import net.kodehawa.migrator.mongodb.codecs.MapCodecProvider;
import net.kodehawa.migrator.rethinkdb.ManagedObject;
import net.kodehawa.migrator.rethinkdb.RethinkCustomCommand;
import net.kodehawa.migrator.rethinkdb.RethinkGuild;
import net.kodehawa.migrator.rethinkdb.RethinkMarriage;
import net.kodehawa.migrator.rethinkdb.RethinkPlayer;
import net.kodehawa.migrator.rethinkdb.RethinkPlayerStats;
import net.kodehawa.migrator.rethinkdb.RethinkPremiumKey;
import net.kodehawa.migrator.rethinkdb.RethinkUser;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.rethinkdb.RethinkDB.r;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Migrator {
    public static final Logger logger = LoggerFactory.getLogger(Migrator.class);

    public static void main(String[] args) {
        logger.info("Starting migration...\n");

        if (getValue("migrator.only_marriage_pets") != null) { // I messed up, and I shall fix it.
            logger.info("Migrating only Marriage pets...");
            var marriages = getRethinkDBMarriages();
            var collection = mongoConnection().getDatabase("mantaro").getCollection(Marriage.DB_TABLE, Marriage.class);

            int i = 0;
            for (var marriage : marriages) {
                var id = marriage.getId();
                var mongoMarriage = collection.find().filter(Filters.eq(id)).first();
                if (mongoMarriage == null) {
                    logger.warn("No marriage for object {}?", id);
                    continue;
                }

                if (mongoMarriage.getPet() != null) {
                    logger.warn("Pet already was overriden on marriage {}", id);
                    continue;
                }

                var rethinkPet = marriage.getData().getPet();
                if (rethinkPet != null) {
                    i++;
                    mongoMarriage.pet(rethinkPet);
                    mongoMarriage.save();
                    logger.info("Saved pet on marriage {} to db", id);
                }
            }

            logger.info("Bailing out, amount was {}...", i);
            return;
        }

        logger.info("Started MantaroObject migration...");
        var rethinkObj = getRethinkDBMantaroObject();
        var mongoObj = new MantaroObject(rethinkObj.getBlackListedGuilds(), rethinkObj.getBlackListedUsers());
        mongoObj.save();
        logger.info("!!! Finished MantaroObject migration\n");

        logger.info("Started User migration...");
        var users = getRethinkDBUsers();
        var i = 0;
        for (var user : users) {
            var id = user.getId();
            logger.info("Migrating user {} out of {} (id: {})", ++i, users.size(), id);
            var mongoUser = MongoUser.of(id);
            var rtdbData = user.getData();

            mongoUser.setPremiumUntil(user.getPremiumUntil());
            mongoUser.autoEquip(rtdbData.isAutoEquip());
            mongoUser.birthday(rtdbData.getBirthday());
            mongoUser.language(rtdbData.getLang());
            mongoUser.marriageId(rtdbData.getMarriageId());
            mongoUser.timezone(rtdbData.getTimezone());
            mongoUser.dustLevel(rtdbData.getDustLevel());
            mongoUser.waifuSlots(rtdbData.getWaifuSlots());
            mongoUser.privateTag(rtdbData.isPrivateTag());
            mongoUser.receivedExpirationWarning(rtdbData.hasReceivedExpirationWarning());
            mongoUser.receivedFirstKey(rtdbData.hasReceivedFirstKey());
            mongoUser.actionsDisabled(rtdbData.isActionsDisabled());
            mongoUser.premiumKey(rtdbData.getPremiumKey());
            mongoUser.setWaifus(rtdbData.getWaifus());
            mongoUser.setReminders(rtdbData.getReminders());
            mongoUser.setKeysClaimed(rtdbData.getKeysClaimed());
            mongoUser.setTimesClaimed(rtdbData.getTimesClaimed());
            mongoUser.setEquippedItems(rtdbData.getEquippedItems());
            mongoUser.setRemindedTimes(rtdbData.getRemindedTimes());

            if (mongoUser.equals(MongoUser.of(id))) {
                logger.warn("Unchanged object id {}, skipping...", id);
                continue;
            }

            mongoUser.save();
        }

        logger.info("!!! Finished User migration.\n");

        logger.info("Started Key migration...");
        var keys = getRethinkDBPremiumKeys();
        i = 0;
        for (var key : keys) {
            var id = key.getId();
            logger.info("Migrating key {} out of {} (id: {})", ++i, keys.size(), id);
            var mongoKey = new PremiumKey(id, key.getDuration(), key.getExpiration(), key.getParsedType(), key.isEnabled(), key.getOwner(), key.getData().getLinkedTo());

            mongoKey.save();
        }

        logger.info("!!! Finished Key migration.\n");

        logger.info("Started Custom Commands migration...");
        var customs = getRethinkCC();
        i = 0;
        for (var custom : customs) {
            var id = custom.getId();
            logger.info("Migrating Custom Command {} out of {} (id: {})", ++i, customs.size(), id);
            var mongoCustom = CustomCommand.of(custom.getGuildId(), custom.getName(), custom.getValues());
            mongoCustom.setLocked(custom.getData().isLocked());
            mongoCustom.setNsfw(custom.getData().isNsfw());
            mongoCustom.setOwner(custom.getData().getOwner());

            mongoCustom.save();
        }

        logger.info("!!! Finished Custom Commands migration.\n");

        logger.info("Started Marriage migration...");
        var marriages = getRethinkDBMarriages();
        i = 0;
        for (var marriage : marriages) {
            var id = marriage.getId();
            logger.info("Migrating Marriage {} out of {} (id: {})", ++i, marriages.size(), id);
            var mongoMarriage = Marriage.of(id, marriage.getPlayer1(), marriage.getPlayer2());
            mongoMarriage.carName(marriage.getData().getCarName());
            mongoMarriage.houseName(marriage.getData().getHouseName());
            mongoMarriage.loveLetter(marriage.getData().getLoveLetter());
            mongoMarriage.timezone(marriage.getData().getTimezone());
            mongoMarriage.car(marriage.getData().hasCar());
            mongoMarriage.house(marriage.getData().hasHouse());
            mongoMarriage.pet(marriage.getData().getPet());

            mongoMarriage.save();
        }

        logger.info("!!! Finished Marriage migration.\n");

        // Reminder: inventory format changed from id:amount to name:amount!
        logger.info("Started Player migration...");
        var players = getRethinkDBPlayers();
        i = 0;
        var skipped = 0;
        var failed = 0;

        for (var player : players) {
            var id = player.getId();
            try {
                logger.info("Migrating Player {} out of {} (id: {})", ++i, players.size(), id);
                var mongoPlayer = Player.of(player.getUserId());
                var rethinkData = player.getData();
                if (player.getLevel() > 1) { // Avoid level 1 players getting "not new" treatment.
                    mongoPlayer.level(player.getLevel());
                }

                mongoPlayer.setOldMoney(player.getOldMoney());
                mongoPlayer.setNewMoney(rethinkData.getNewMoney());
                mongoPlayer.reputation(player.getReputation());

                if (rethinkData.getExperience() > 500) { // Avoid a bunch of 1~150 experience and nothing else changed objects.
                    mongoPlayer.setExperience(rethinkData.getExperience());
                }

                mongoPlayer.dailyStreak(rethinkData.getDailyStreak());
                mongoPlayer.description(rethinkData.getDescription());
                mongoPlayer.gamesWon(rethinkData.getGamesWon());
                mongoPlayer.lastDailyAt(rethinkData.getLastDailyAt());
                mongoPlayer.setLockedUntil(rethinkData.getLockedUntil());
                mongoPlayer.mainBadge(rethinkData.getMainBadge());
                mongoPlayer.marketUsed(rethinkData.getMarketUsed());
                mongoPlayer.showBadge(rethinkData.isShowBadge());
                mongoPlayer.setActivePotion(rethinkData.getActivePotion());
                mongoPlayer.setActiveBuff(rethinkData.getActiveBuff());
                mongoPlayer.waifuCachedValue(rethinkData.getWaifuCachedValue());
                mongoPlayer.claimLocked(rethinkData.isClaimLocked());
                mongoPlayer.setMiningExperience(rethinkData.getMiningExperience());
                mongoPlayer.setFishingExperience(rethinkData.getFishingExperience());
                mongoPlayer.setChopExperience(rethinkData.getChopExperience());
                mongoPlayer.timesMopped(rethinkData.getTimesMopped());
                mongoPlayer.cratesOpened(rethinkData.getCratesOpened());
                mongoPlayer.sharksCaught(rethinkData.getSharksCaught());
                mongoPlayer.waifuout(rethinkData.isWaifuout());
                mongoPlayer.lastCrateGiven(rethinkData.getLastCrateGiven());
                mongoPlayer.setLastSeenCampaign(rethinkData.getLastSeenCampaign());
                mongoPlayer.setResetWarning(rethinkData.isResetWarning());
                mongoPlayer.inventorySortType(rethinkData.getInventorySortType());
                mongoPlayer.hiddenLegacy(rethinkData.isHiddenLegacy());
                mongoPlayer.newPlayerNotice(rethinkData.isNewPlayerNotice());
                mongoPlayer.setPetSlots(rethinkData.getPetSlots());
                mongoPlayer.setPetChoice(rethinkData.getPetChoice());
                mongoPlayer.setPet(rethinkData.getPet());
                mongoPlayer.setBadges(rethinkData.getBadges());
                mongoPlayer.setProfileComponents(rethinkData.getProfileComponents());
                // This takes care of the inventory format change, as merge is done as a list of ItemStack, which get serialized on save.
                mongoPlayer.mergeInventory(player.getInventory().asList());

                if (mongoPlayer.equals(Player.of(player.getUserId()))) {
                    logger.warn("Unchanged object id {}, skipping...", id);
                    skipped++;
                    continue;
                }

                mongoPlayer.save();
            } catch (Exception e) { // continue loop
                failed++;
                logger.error("Error on id " + id, e);
            }
        }

        logger.info("!!! Finished Player migration, skipped unchanged objects: {}, failed objects: {}\n", skipped, failed);

        logger.info("Started Player Statistics migration...");
        var playerStats = getRethinkPlayerStats();
        i = 0;
        skipped = 0;
        failed = 0;
        for (var stat : playerStats) {
            var id = stat.getId();
            try {
                logger.info("Migrating PlayerStats {} out of {} (id: {})", ++i, playerStats.size(), id);
                var mongoStat = PlayerStats.of(id);
                mongoStat.setCraftedItems(stat.getCraftedItems());
                mongoStat.setToolsBroken(stat.getToolsBroken());
                mongoStat.setGambleWinAmount(stat.getGambleWinAmount());
                mongoStat.setGambleWins(stat.getGambleWins());
                mongoStat.setSlotsWins(stat.getSlotsWins());
                mongoStat.setRepairedItems(stat.getRepairedItems());
                mongoStat.setSalvagedItems(stat.getSalvagedItems());
                mongoStat.setGambleLose(stat.getData().getGambleLose());
                mongoStat.setSlotsLose(stat.getData().getSlotsLose());

                if (mongoStat.equals(PlayerStats.of(id))) {
                    skipped++;
                    logger.warn("Unchanged object id {}, skipping...", id);
                    continue;
                }

                mongoStat.save();
            } catch (Exception e) {
                failed++;
                logger.error("Error on id " + id, e);
            }
        }

        logger.info("!!! Finished Player Statistics migration. skipped objects: {}, failed objects: {}\n", skipped, failed);

        logger.info("Started Guild migration...");
        var guilds = getRethinkGuilds();
        i = 0;
        skipped = 0;
        failed = 0;
        for (var guild : guilds) {
            var id = guild.getId();
            try {
                var id = guild.getId();
                logger.info("Migrating Guild {} out of {} (id: {})", ++i, guilds.size(), id);
                var mongoGuild = MongoGuild.of(id);
                var rethinkData = guild.getData();
                // This was painstakingly double checked using the incredible technology of a single notepad with the fields on it and making sure the amount matched up.
                // Yep, I'm suffering.
                mongoGuild.setPremiumUntil(guild.getPremiumUntil());
                mongoGuild.setAutoroles(rethinkData.getAutoroles());
                mongoGuild.setBirthdayChannel(rethinkData.getBirthdayChannel());
                mongoGuild.setBirthdayRole(rethinkData.getBirthdayRole());
                mongoGuild.setCases(rethinkData.getCases());
                mongoGuild.setChannelSpecificDisabledCategories(rethinkData.getChannelSpecificDisabledCategories());
                mongoGuild.setChannelSpecificDisabledCommands(rethinkData.getChannelSpecificDisabledCommands());
                mongoGuild.setDisabledCategories(rethinkData.getDisabledCategories());
                mongoGuild.setDisabledChannels(rethinkData.getDisabledChannels());
                mongoGuild.setDisabledCommands(rethinkData.getDisabledCommands());
                mongoGuild.setDisabledRoles(rethinkData.getDisabledRoles());
                mongoGuild.setDisabledUsers(rethinkData.getDisabledUsers());
                mongoGuild.setGuildAutoRole(rethinkData.getGuildAutoRole());
                mongoGuild.setGuildCustomPrefix(rethinkData.getGuildCustomPrefix());
                mongoGuild.setGuildLogChannel(rethinkData.getGuildLogChannel());
                mongoGuild.setJoinMessage(rethinkData.getJoinMessage());
                mongoGuild.setLeaveMessage(rethinkData.getLeaveMessage());
                mongoGuild.setLogExcludedChannels(rethinkData.getLogExcludedChannels());
                mongoGuild.setLogJoinLeaveChannel(rethinkData.getLogJoinLeaveChannel());
                mongoGuild.setMaxFairQueue(rethinkData.getMaxFairQueue());
                mongoGuild.setModlogBlacklistedPeople(rethinkData.getModlogBlacklistedPeople());
                mongoGuild.setMusicAnnounce(rethinkData.isMusicAnnounce());
                mongoGuild.setMusicChannel(rethinkData.getMusicChannel());
                mongoGuild.setMutedRole(rethinkData.getMutedRole());
                mongoGuild.setNoMentionsAction(rethinkData.isNoMentionsAction());
                mongoGuild.setPremiumKey(rethinkData.getPremiumKey());
                mongoGuild.setRanPolls(rethinkData.getRanPolls());
                mongoGuild.setRolesBlockedFromCommands(rethinkData.getRolesBlockedFromCommands());
                mongoGuild.setSetModTimeout(rethinkData.getSetModTimeout());
                mongoGuild.setTimeDisplay(rethinkData.getTimeDisplay());
                mongoGuild.setGameTimeoutExpectedAt(rethinkData.getGameTimeoutExpectedAt());
                mongoGuild.setIgnoreBotsAutoRole(rethinkData.isIgnoreBotsAutoRole());
                mongoGuild.setIgnoreBotsWelcomeMessage(rethinkData.isIgnoreBotsWelcomeMessage());
                mongoGuild.setBlackListedImageTags(rethinkData.getBlackListedImageTags());
                mongoGuild.setLogJoinChannel(rethinkData.getLogJoinChannel());
                mongoGuild.setLogLeaveChannel(rethinkData.getLogLeaveChannel());
                mongoGuild.setRoleSpecificDisabledCategories(rethinkData.getRoleSpecificDisabledCategories());
                mongoGuild.setRoleSpecificDisabledCommands(rethinkData.getRoleSpecificDisabledCommands());
                mongoGuild.setLang(rethinkData.getLang());
                mongoGuild.setMusicVote(rethinkData.isMusicVote());
                mongoGuild.setExtraJoinMessages(rethinkData.getExtraJoinMessages());
                mongoGuild.setExtraLeaveMessages(rethinkData.getExtraLeaveMessages());
                mongoGuild.setBirthdayMessage(rethinkData.getBirthdayMessage());
                mongoGuild.setCustomAdminLockNew(rethinkData.isCustomAdminLockNew());
                mongoGuild.setMpLinkedTo(rethinkData.getMpLinkedTo());
                mongoGuild.setModlogBlacklistedPeople(rethinkData.getModlogBlacklistedPeople());
                mongoGuild.setAutoroleCategories(rethinkData.getAutoroleCategories());
                mongoGuild.setEditMessageLog(rethinkData.getEditMessageLog());
                mongoGuild.setDeleteMessageLog(rethinkData.getDeleteMessageLog());
                mongoGuild.setBannedMemberLog(rethinkData.getBannedMemberLog());
                mongoGuild.setUnbannedMemberLog(rethinkData.getUnbannedMemberLog());
                mongoGuild.setKickedMemberLog(rethinkData.getKickedMemberLog());
                mongoGuild.setCommandWarningDisplay(rethinkData.isCommandWarningDisplay());
                mongoGuild.setBirthdayBlockedIds(rethinkData.getBirthdayBlockedIds());
                mongoGuild.setGameMultipleDisabled(rethinkData.isGameMultipleDisabled());
                mongoGuild.setLogTimezone(rethinkData.getLogTimezone());
                mongoGuild.setAllowedBirthdays(rethinkData.getAllowedBirthdays());
                mongoGuild.setNotifiedFromBirthdayChange(rethinkData.isNotifiedFromBirthdayChange());
                mongoGuild.setDisableExplicit(rethinkData.isDisableExplicit());
                mongoGuild.setDjRoleId(rethinkData.getDjRoleId());
                mongoGuild.setMusicQueueSizeLimit(rethinkData.getMusicQueueSizeLimit());
                mongoGuild.setRunningPolls(rethinkData.getRunningPolls());
                mongoGuild.setHasReceivedGreet(rethinkData.isHasReceivedGreet());

                if (mongoGuild.equals(MongoGuild.of(id))) {
                    logger.warn("Unchanged object id {}, skipping...", id);
                    skipped++;
                    continue;
                }

                mongoGuild.save();
            } catch (Exception e) {
                failed++;
                logger.error("Error on id " + id, e);
            }
        }

        logger.info("!!! Finished Guild migration, skipped unchanged objects: {}, failed: {}\n", skipped, failed);
    }

    private static Connection rethinkConnection;
    private static MongoClient mongoClient;
    private static final CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
            .automatic(true)
            .register(new MapCodecProvider())
            .conventions(Arrays.asList(Conventions.CLASS_AND_PROPERTY_CONVENTION, Conventions.ANNOTATION_CONVENTION, Conventions.OBJECT_ID_GENERATORS, Conventions.SET_PRIVATE_FIELDS_CONVENTION))
            .build();

    private static final CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
    public static MongoClient mongoConnection() {
        if (mongoClient == null) {
            synchronized (Migrator.class) {
                try {
                    ConnectionString connectionString = new ConnectionString(getValue("migrator.mongo_uri"));
                    ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
                            .minSize(2)
                            .maxSize(30)
                            .maxConnectionIdleTime(0, TimeUnit.MILLISECONDS)
                            .maxConnectionLifeTime(120, TimeUnit.SECONDS)
                            .build();

                    MongoClientSettings clientSettings = MongoClientSettings.builder()
                            .applyConnectionString(connectionString)
                            .applyToConnectionPoolSettings(builder -> builder.applySettings(connectionPoolSettings))
                            .codecRegistry(pojoCodecRegistry)
                            .build();

                    mongoClient = MongoClients.create(clientSettings);
                    logger.info("Established first MongoDB connection.");
                } catch (Exception e) {
                    logger.error("Cannot connect to database (MongoDB)! Bailing out!", e);
                    System.exit(1);
                }
            }
        }

        return mongoClient;
    }

    public static Connection rethinkConnection() {
        if (rethinkConnection == null) {
            synchronized (Migrator.class) {
                try {
                    if (rethinkConnection != null) {
                        return rethinkConnection;
                    }

                    rethinkConnection = r.connection()
                            .hostname(getValue("migrator.rethink_host"))
                            .port(28015)
                            .db("mantaro")
                            .user(getValue("migrator.rethink_user"), getValue("migrator.rethink_pw"))
                            .connect();

                    logger.info("Established first RethinkDB connection.");
                } catch (Exception e) {
                    logger.error("Cannot connect to database (RethinkDB)! Bailing out!", e);
                    System.exit(1);
                }
            }
        }

        return rethinkConnection;
    }

    public static List<RethinkPlayer> getRethinkDBPlayers() {
        logger.info("Getting all players...");
        String pattern = ":g$";
        Result<RethinkPlayer> c = r.table(RethinkPlayer.DB_TABLE).filter(quote -> quote.g("id").match(pattern)).run(rethinkConnection(), RethinkPlayer.class);
        var list = c.toList();
        logger.info("Got all players, list size is: {}", list.size());
        return list;
    }

    public static List<RethinkMarriage> getRethinkDBMarriages() {
        logger.info("Getting all marriages...");
        Result<RethinkMarriage> c = r.table(RethinkMarriage.DB_TABLE).run(rethinkConnection(), RethinkMarriage.class);
        var list = c.toList();
        logger.info("Got all marriages, list size is: {}", list.size());
        return list;
    }

    public static List<RethinkPremiumKey> getRethinkDBPremiumKeys() {
        logger.info("Getting all keys...");
        Result<RethinkPremiumKey> c = r.table(RethinkPremiumKey.DB_TABLE).run(rethinkConnection(), RethinkPremiumKey.class);
        var list = c.toList();
        logger.info("Got all keys, list size is: {}", list.size());
        return list;
    }

    public static MantaroObject getRethinkDBMantaroObject() {
        logger.info("Getting MantaroObject...");
        return r.table(MantaroObject.DB_TABLE).get("mantaro").runAtom(rethinkConnection(), MantaroObject.class);
    }

    public static List<RethinkUser> getRethinkDBUsers() {
        logger.info("Getting all users...");
        Result<RethinkUser> c = r.table(RethinkUser.DB_TABLE).run(rethinkConnection(), RethinkUser.class);
        var list = c.toList();
        logger.info("Got all users, list size is: {}", list.size());
        return list;
    }

    public static List<RethinkCustomCommand> getRethinkCC() {
        logger.info("Getting all custom commands...");
        Result<RethinkCustomCommand> c = r.table(RethinkCustomCommand.DB_TABLE).run(rethinkConnection(), RethinkCustomCommand.class);
        var list = c.toList();
        logger.info("Got all custom commands, list size is: {}", list.size());
        return list;
    }

    public static List<RethinkGuild> getRethinkGuilds() {
        logger.info("Getting all guilds...");
        Result<RethinkGuild> c = r.table(RethinkGuild.DB_TABLE).run(rethinkConnection(), RethinkGuild.class);
        var list = c.toList();
        logger.info("Got all guilds, list size is: {}", list.size());
        return list;
    }

    public static List<RethinkPlayerStats> getRethinkPlayerStats() {
        logger.info("Getting all guilds...");
        Result<RethinkPlayerStats> c = r.table(RethinkPlayerStats.DB_TABLE).run(rethinkConnection(), RethinkPlayerStats.class);
        var list = c.toList();
        logger.info("Got all player statistics, list size is: {}", list.size());
        return list;
    }

    private static String getValue(String name) {
        return System.getProperty(name, System.getenv(name.replace("-", "_").replace(".", "_").toUpperCase()));
    }

    public static String decodeURL(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

    public static String encodeURL(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    public static void saveRethinkDB(ManagedObject object) {
        r.table(object.getTableName())
                .insert(object)
                .optArg("conflict", "replace")
                .runNoReply(rethinkConnection());
    }

    public static void deleteRethinkDB(ManagedObject object) {
        r.table(object.getTableName())
                .get(object.getId())
                .delete()
                .runNoReply(rethinkConnection());
    }

    public static <T extends ManagedMongoObject> void saveMongo(T object, Class<T> clazz) {
        try {
            var collection = mongoConnection().getDatabase("mantaro").getCollection(object.getTableName(), clazz);
            if (getValue("migrator.only_marriage_pets") != null && !object.getTableName().equals(Marriage.DB_TABLE)) {
                logger.error("!! Tried to save a non-marriage object? Something is wrong.");
                return;
            }

            if (collection.find().filter(Filters.eq(object.getId())).first() != null && getValue("migrator.only_marriage_pets") == null) {
                logger.warn("Skipping save: object already exists?");
                return;
            }

            if (getValue("migrator.only_marriage_pets") != null) {
                collection.findOneAndReplace(Filters.eq(object.getId()), object);
            } else {
                collection.insertOne(object);
            }
            logger.info("Saved id {} to Mongo: {}", object.getId(), object);
        } catch (Exception e) {
            logger.error("Error saving object!", e);
        }
    }


    public static <T extends ManagedMongoObject> void deleteMongo(T object, Class<T> clazz) {
        MongoCollection<T> collection = mongoConnection().getDatabase("mantaro").getCollection(object.getTableName(), clazz);
        collection.deleteOne(Filters.eq(object.getId()));
    }
}