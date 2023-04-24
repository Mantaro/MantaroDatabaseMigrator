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
import net.kodehawa.migrator.mongodb.ManagedMongoObject;
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
    public static void main(String[] args) {
        System.out.println("Starting migration...");
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
        System.out.println("Started user migration...");
        var users = getRethinkDBUsers();
        var i = 0;
        for (var user : users) {
            var id = user.getId();
            System.out.println("Migrating user " + ++i + " out of " + users.size() + " (id: " + id + ")");
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

        System.out.println("Finished user migration.");
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
        System.out.println("Started key migration...");
        var keys = getRethinkDBPremiumKeys();
        i = 0;
        for (var key : keys) {
            var id = key.getId();
            System.out.println("Migrating key " + ++i + " out of " + keys.size() + " (id: " + id + ")");
            var mongoKey = new PremiumKey(id, key.getDuration(), key.getExpiration(), key.getParsedType(), key.isEnabled(), key.getOwner(), key.getData().getLinkedTo());

            mongoKey.save();
        }
        System.out.println("Finished key migration.");
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");


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
                    System.out.println("Established first MongoDB connection.");
                } catch (Exception e) {
                    System.err.println("Cannot connect to database! Bailing out");
                    e.printStackTrace();
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

                System.out.println("Established first RethinkDB connection.");
            }
        }

        return rethinkConnection;
    }

    public static List<RethinkPlayer> getRethinkDBPlayers() {
        System.out.println("Getting all players...");
        String pattern = ":g$";
        Result<RethinkPlayer> c = r.table(RethinkPlayer.DB_TABLE).filter(quote -> quote.g("id").match(pattern)).run(rethinkConnection(), RethinkPlayer.class);
        var list = c.toList();
        System.out.println("Got all players, list size is: " + list.size());
        return list;
    }

    public static List<RethinkMarriage> getRethinkDBMarriages() {
        System.out.println("Getting all marriages...");
        Result<RethinkMarriage> c = r.table(RethinkMarriage.DB_TABLE).run(rethinkConnection(), RethinkMarriage.class);
        var list = c.toList();
        System.out.println("Got all marriages, list size is: " + list.size());
        return list;
    }

    public static List<RethinkPremiumKey> getRethinkDBPremiumKeys() {
        System.out.println("Getting all keys...");
        Result<RethinkPremiumKey> c = r.table(RethinkPremiumKey.DB_TABLE).run(rethinkConnection(), RethinkPremiumKey.class);
        var list = c.toList();
        System.out.println("Got all keys, list size is: " + list.size());
        return list;
    }

    public static List<RethinkUser> getRethinkDBUsers() {
        System.out.println("Getting all users...");
        Result<RethinkUser> c = r.table(RethinkUser.DB_TABLE).run(rethinkConnection(), RethinkUser.class);
        var list = c.toList();
        System.out.println("Got all users, list size is: " + list.size());
        return list;
    }

    public static List<RethinkCustomCommand> getRethinkCC() {
        System.out.println("Getting all custom commands...");
        Result<RethinkCustomCommand> c = r.table(RethinkCustomCommand.DB_TABLE).run(rethinkConnection(), RethinkCustomCommand.class);
        var list = c.toList();
        System.out.println("Got all custom commands, list size is: " + list.size());
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
        var collection = mongoConnection().getDatabase("mantaro").getCollection(object.getTableName(), clazz);
        if (collection.find().filter(Filters.eq(object.getId())).first() != null) {
            System.out.println("Skipping save: object already exists?");
            return;
        }

        System.out.println("Saved to Mongo: " + object);
        collection.insertOne(object);
    }

    public static <T extends ManagedMongoObject> void deleteMongo(T object, Class<T> clazz) {
        MongoCollection<T> collection = mongoConnection().getDatabase("mantaro").getCollection(object.getTableName(), clazz);
        collection.deleteOne(Filters.eq(object.getId()));
    }
}