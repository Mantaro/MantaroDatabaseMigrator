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
import net.kodehawa.migrator.rethinkdb.helpers.Inventory;
import net.kodehawa.migrator.rethinkdb.helpers.PlayerData;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.Map;

import static net.kodehawa.migrator.rethinkdb.helpers.Inventory.Resolver.serialize;
import static net.kodehawa.migrator.rethinkdb.helpers.Inventory.Resolver.unserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Player implements ManagedObject {
    public static final String DB_TABLE = "players";
    @JsonProperty("data")
    private final PlayerData data;
    @JsonProperty("id")
    private final String id;

    @JsonIgnore
    private final transient Inventory inventory = new Inventory();

    @JsonProperty("level")
    private Long level;
    @JsonProperty("money")
    private Long oldMoney;
    @JsonProperty("reputation")
    private Long reputation;

    @JsonCreator
    @ConstructorProperties({"id", "level", "money", "reputation", "inventory", "data"})
    public Player(@JsonProperty("id") String id, @JsonProperty("level") Long level, @JsonProperty("money") Long oldMoney, @JsonProperty("reputation") Long reputation, @JsonProperty("inventory") Map<Integer, Integer> inventory, @JsonProperty("data") PlayerData data) {
        this.id = id;
        this.level = level == null ? 0 : level;
        this.oldMoney = oldMoney == null ? 0 : oldMoney;
        this.reputation = reputation == null ? 0 : reputation;
        this.data = data;
        this.inventory.replaceWith(unserialize(inventory));
    }


    /**
     * The Player.of methods are for resetting players or creating new ones when they don't exist.
     *
     * @param userId The user to create or reset.
     * @return The new Player.
     */
    public static Player of(String userId) {
        return new Player(userId + ":g", 0L, 0L, 0L, new HashMap<>(), new PlayerData());
    }

    /**
     * Adds x amount of reputation to a player. Normally 1.
     *
     * @param rep how much?
     */
    @JsonIgnore
    public void addReputation(long rep) {
        this.reputation += rep;
        this.setReputation(reputation);
    }

    @JsonIgnore
    public String getGuildId() {
        return getId().split(":")[1];
    }

    @JsonIgnore
    public Inventory getInventory() {
        return inventory;
    }

    @JsonIgnore
    public String getUserId() {
        return getId().split(":")[0];
    }

    @JsonIgnore
    public boolean isGlobal() {
        return getGuildId().equals("g");
    }

    @JsonProperty("inventory")
    public Map<Integer, Integer> rawInventory() {
        return serialize(inventory.asList());
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

    public PlayerData getData() {
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

    @JsonIgnore
    
    @Override
    public String getDatabaseId() {
        return getUserId();
    }

    public Long getLevel() {
        return this.level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    // So it doesn't fail to de-serialize it. Blame JacksonXML.
    public long getOldMoney() {
        return oldMoney;
    }

    // So it doesn't fail to de-serialize it. Blame JacksonXML.
    public void setOldMoney(long newAmount) {
        this.oldMoney = newAmount;
    }

    public Long getReputation() {
        return this.reputation;
    }

    public void setReputation(Long reputation) {
        this.reputation = reputation;
    }
}
