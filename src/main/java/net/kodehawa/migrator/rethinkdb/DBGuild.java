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

package net.kodehawa.migrator.rethinkdb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.kodehawa.migrator.rethinkdb.helpers.GuildData;

import java.beans.ConstructorProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DBGuild implements ManagedObject {
    public static final String DB_TABLE = "guilds";
    private final GuildData data;
    private final String id;
    private long premiumUntil;

    @JsonCreator
    @ConstructorProperties({"id", "premiumUntil", "data"})
    public DBGuild(@JsonProperty("id") String id, @JsonProperty("premiumUntil") long premiumUntil, @JsonProperty("data") GuildData data) {
        this.id = id;
        this.premiumUntil = premiumUntil;
        this.data = data;
    }

    public static DBGuild of(String id) {
        return new DBGuild(id, 0, new GuildData());
    }

    public static DBGuild of(String id, long premiumUntil) {
        return new DBGuild(id, premiumUntil, new GuildData());
    }

    public GuildData getData() {
        return this.data;
    }

    public String getId() {
        return this.id;
    }

    @JsonIgnore
    @Override
    
    public String getTableName() {
        return DB_TABLE;
    }

    public long getPremiumUntil() {
        return this.premiumUntil;
    }
}
