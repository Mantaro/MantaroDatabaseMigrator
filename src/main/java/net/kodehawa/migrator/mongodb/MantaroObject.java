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
import net.kodehawa.migrator.Pair;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MantaroObject implements ManagedMongoObject {
    @BsonIgnore
    public static final String DB_TABLE = "mantaro";
    @BsonId
    public static final String id = "mantaro";

    public List<String> blackListedGuilds;
    public List<String> blackListedUsers;
    public List<String> patreonUsers;
    private Map<String, Pair<String, Long>> mutes;
    private Map<String, Long> tempBans;

    @BsonCreator
    public MantaroObject(@BsonProperty("blackListedGuilds") List<String> blackListedGuilds,
                         @BsonProperty("blackListedUsers") List<String> blackListedUsers,
                         @BsonProperty("patreonUsers") List<String> patreonUsers,
                         @BsonProperty("mutes") Map<String, Pair<String, Long>> mutes) {
        this.blackListedGuilds = blackListedGuilds;
        this.blackListedUsers = blackListedUsers;
        this.patreonUsers = patreonUsers;
        this.mutes = mutes;
    }

    public MantaroObject() { }

    public static MantaroObject create() {
        return new MantaroObject(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ConcurrentHashMap<>());
    }

    public String getId() {
        return id;
    }

    @BsonIgnore
    @Override
    public String getTableName() {
        return DB_TABLE;
    }

    @Override
    @BsonIgnore
    public void save() {
        Migrator.saveMongo(this, MantaroObject.class);
    }

    @Override
    @BsonIgnore
    public void delete() {
        Migrator.deleteMongo(this, MantaroObject.class);
    }

    public List<String> getBlackListedGuilds() {
        return this.blackListedGuilds;
    }

    public void setBlackListedGuilds(List<String> blackListedGuilds) {
        this.blackListedGuilds = blackListedGuilds;
    }

    public List<String> getBlackListedUsers() {
        return this.blackListedUsers;
    }

    public void setBlackListedUsers(List<String> blackListedUsers) {
        this.blackListedUsers = blackListedUsers;
    }

    public List<String> getPatreonUsers() {
        return this.patreonUsers;
    }

    public void setPatreonUsers(List<String> patreonUsers) {
        this.patreonUsers = patreonUsers;
    }

    public Map<String, Pair<String, Long>> getMutes() {
        return this.mutes;
    }

    public void setMutes(Map<String, Pair<String, Long>> mutes) {
        this.mutes = mutes;
    }

    public Map<String, Long> getTempBans() {
        return this.tempBans;
    }

    public void setTempBans(Map<String, Long> tempBans) {
        this.tempBans = tempBans;
    }
}
