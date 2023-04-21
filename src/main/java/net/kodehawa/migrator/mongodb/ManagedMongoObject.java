package net.kodehawa.migrator.mongodb;

import org.bson.codecs.pojo.annotations.BsonIgnore;

public interface ManagedMongoObject {
    String getId();

    @BsonIgnore
    String getTableName();

    @BsonIgnore
    default String getDatabaseId() {
        return getId();
    }

    @BsonIgnore
    default void updateAllChanged() {
        throw new UnsupportedOperationException();
    }

    // Need to implement class-by-class...
    @BsonIgnore
    void save();
    @BsonIgnore
    void delete();
}
