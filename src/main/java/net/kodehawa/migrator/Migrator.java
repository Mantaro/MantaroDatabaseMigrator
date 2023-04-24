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
import net.kodehawa.migrator.mongodb.ManagedMongoObject;
import net.kodehawa.migrator.mongodb.Marriage;
import net.kodehawa.migrator.mongodb.PremiumKey;
import net.kodehawa.migrator.mongodb.UserDatabase;
import net.kodehawa.migrator.mongodb.codecs.MapCodecProvider;
import net.kodehawa.migrator.rethinkdb.ManagedObject;
import net.kodehawa.migrator.rethinkdb.RethinkCustomCommand;
import net.kodehawa.migrator.rethinkdb.RethinkMarriage;
import net.kodehawa.migrator.rethinkdb.RethinkPlayer;
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
    public static Logger logger = LoggerFactory.getLogger(Migrator.class);
    public static void main(String[] args) {
        logger.info("Starting migration...\n");

        logger.info("Started User migration...");
        var users = getRethinkDBUsers();
        var i = 0;
        for (var user : users) {
            var id = user.getId();
            logger.info("Migrating user {} out of {} (id: {}", ++i, users.size(), id);
            var mongoUser = UserDatabase.of(id);
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

            mongoMarriage.save();
        }

        logger.info("!!! Finished Marriage migration.\n");

        // Reminder: inventory format changed from id:amount to name:amount!
        logger.info("Started Player migration...");
        logger.info("!!! Finished Player migration.\n");
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
                    logger.error("Cannot connect to database! Bailing out!", e);
                    System.exit(1);
                }
            }
        }

        return mongoClient;
    }

    public static Connection rethinkConnection() {
        if (rethinkConnection == null) {
            synchronized (Migrator.class) {
                if (rethinkConnection != null) {
                    return rethinkConnection;
                }

                rethinkConnection = r.connection()
                        .hostname(getValue("migrator.rethink_host"))
                        .port(28015)
                        .db("mantaro")
                        .user(getValue("migrator.rethink_user"), getValue("migrator.rethink_pw"))
                        .connect();

                logger.error("Established first RethinkDB connection.");
            }
        }

        return rethinkConnection;
    }

    public static List<RethinkPlayer> getRethinkDBPlayers() {
        logger.error("Getting all players...");
        String pattern = ":g$";
        Result<RethinkPlayer> c = r.table(RethinkPlayer.DB_TABLE).filter(quote -> quote.g("id").match(pattern)).run(rethinkConnection(), RethinkPlayer.class);
        var list = c.toList();
        logger.error("Got all players, list size is: {}", list.size());
        return list;
    }

    public static List<RethinkMarriage> getRethinkDBMarriages() {
        System.out.println("Getting all marriages...");
        Result<RethinkMarriage> c = r.table(RethinkMarriage.DB_TABLE).run(rethinkConnection(), RethinkMarriage.class);
        var list = c.toList();
        logger.error("Got all marriages, list size is: {}", list.size());
        return list;
    }

    public static List<RethinkPremiumKey> getRethinkDBPremiumKeys() {
        System.out.println("Getting all keys...");
        Result<RethinkPremiumKey> c = r.table(RethinkPremiumKey.DB_TABLE).run(rethinkConnection(), RethinkPremiumKey.class);
        var list = c.toList();
        logger.error("Got all keys, list size is: {}", list.size());
        return list;
    }

    public static List<RethinkUser> getRethinkDBUsers() {
        System.out.println("Getting all users...");
        Result<RethinkUser> c = r.table(RethinkUser.DB_TABLE).run(rethinkConnection(), RethinkUser.class);
        var list = c.toList();
        logger.error("Got all users, list size is: {}", list.size());
        return list;
    }

    public static List<RethinkCustomCommand> getRethinkCC() {
        System.out.println("Getting all custom commands...");
        Result<RethinkCustomCommand> c = r.table(RethinkCustomCommand.DB_TABLE).run(rethinkConnection(), RethinkCustomCommand.class);
        var list = c.toList();
        logger.error("Got all custom commands, list size is: {}", list.size());
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
            if (collection.find().filter(Filters.eq(object.getId())).first() != null) {
                logger.warn("Skipping save: object already exists?");
                return;
            }

            logger.info("Saved to Mongo: " + object);
            collection.insertOne(object);
        } catch (Exception e) {
            logger.error("Error saving object!", e);
        }
    }


    public static <T extends ManagedMongoObject> void deleteMongo(T object, Class<T> clazz) {
        MongoCollection<T> collection = mongoConnection().getDatabase("mantaro").getCollection(object.getTableName(), clazz);
        collection.deleteOne(Filters.eq(object.getId()));
    }
}