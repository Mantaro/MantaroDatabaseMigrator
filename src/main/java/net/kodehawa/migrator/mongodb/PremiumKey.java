/*
 * Copyright (C) 2016 Kodehawa
 *
 * Mantaro is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Mantaro is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mantaro. If not, see http://www.gnu.org/licenses/
 *
 */

package net.kodehawa.migrator.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.kodehawa.migrator.Migrator;
import net.kodehawa.migrator.helpers.special.helpers.KeyType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PremiumKey implements ManagedMongoObject {
    @BsonIgnore
    public static final String DB_TABLE = "keys";

    @BsonId
    private String id;
    private long duration;
    private boolean enabled;
    private long expiration;
    private String owner;
    private int type;
    private String linkedTo;

    public PremiumKey() {}

    public PremiumKey(String id, long duration, long expiration, KeyType type, boolean enabled, String owner, String linkedTo) {
        this.id = id;
        this.duration = duration;
        this.expiration = expiration;
        this.type = type.ordinal();
        this.enabled = enabled;
        this.owner = owner;
        this.linkedTo = linkedTo;
    }

    public String getLinkedTo() {
        return this.linkedTo;
    }

    public void setLinkedTo(String linkedTo) {
        this.linkedTo = linkedTo;
    }

    public long getDuration() {
        return this.duration;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public long getExpiration() {
        return this.expiration;
    }

    
    public String getId() {
        return this.id;
    }

    @BsonIgnore
    @Override
    public String getTableName() {
        return DB_TABLE;
    }

    public String getOwner() {
        return this.owner;
    }

    public int getType() {
        return this.type;
    }

    @Override
    public void save() {
        Migrator.saveMongo(this, PremiumKey.class);
    }

    @Override
    public void delete() {
        Migrator.deleteMongo(this, PremiumKey.class);
    }
}
