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
import com.fasterxml.jackson.annotation.JsonProperty;
import net.kodehawa.migrator.rethinkdb.helpers.MarriageData;

import java.beans.ConstructorProperties;

@SuppressWarnings("ClassCanBeRecord")
public class RethinkMarriage implements ManagedObject {
    public static final String DB_TABLE = "marriages";
    private final String player1;
    private final String player2;
    private final String id;
    private final MarriageData data;

    @JsonCreator
    @ConstructorProperties({"id", "player1", "player2", "data"})
    public RethinkMarriage(@JsonProperty("id") String id, @JsonProperty("player1") String player1, @JsonProperty("player2") String player2, MarriageData data) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.data = data;
    }

    /**
     * The Marriage.of methods are for resetting marriages or creating new ones when they don't exist.
     *
     * @return The new Marriage.
     */
    public static RethinkMarriage of(String marriageId, String userId1, String userId2) {
        return new RethinkMarriage(marriageId, userId1, userId2, new MarriageData());
    }

    @JsonIgnore
    public String getOtherPlayer(String id) {
        if (player1.equals(id)) {
            return player2;
        } else if (player2.equals(id)) {
            return player1;
        } else {
            return null;
        }
    }

    public String getPlayer1() {
        return this.player1;
    }

    public String getPlayer2() {
        return this.player2;
    }

    //it's 3am and i cba to replace usages of this so whatever
    @JsonIgnore
    public boolean isLocked() {
        return data.getLockedUntil() - System.currentTimeMillis() > 0;
    }

    @JsonIgnore
    public void setLocked(boolean locked) {
        data.setLockedUntil(locked ? System.currentTimeMillis() + 35000 : 0);
    }

    
    public String getId() {
        return this.id;
    }

    @JsonIgnore
    
    @Override
    public String getTableName() {
        return DB_TABLE;
    }

    public MarriageData getData() {
        return this.data;
    }
}
