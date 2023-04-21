package net.kodehawa.migrator;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.connection.ConnectionPoolSettings;
import com.rethinkdb.net.Connection;
import net.kodehawa.migrator.mongodb.ManagedMongoObject;
import net.kodehawa.migrator.mongodb.codecs.MapCodecProvider;
import net.kodehawa.migrator.rethinkdb.ManagedObject;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.rethinkdb.RethinkDB.r;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static com.rethinkdb.RethinkDB.r;

public class Migrator {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        // TODO: Write the actual migration process...
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
        var returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
        var found = collection.findOneAndReplace(Filters.eq(object.getId()), object, returnDoc);
        if (found == null) { // New document?
            collection.insertOne(object);
        }
    }

    public static <T extends ManagedMongoObject> void deleteMongo(T object, Class<T> clazz) {
        MongoCollection<T> collection = mongoConnection().getDatabase("mantaro").getCollection(object.getTableName(), clazz);
        collection.deleteOne(Filters.eq(object.getId()));
    }
}