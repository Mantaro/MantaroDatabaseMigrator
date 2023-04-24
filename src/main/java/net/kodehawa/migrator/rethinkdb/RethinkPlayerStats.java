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
import net.kodehawa.migrator.rethinkdb.helpers.PlayerStatsData;

import java.beans.ConstructorProperties;

public class RethinkPlayerStats implements ManagedObject {
    public static final String DB_TABLE = "playerstats";

    private final String id;
    private final PlayerStatsData data;
    private long gambleWins;
    private long slotsWins;
    private long gambleWinAmount;
    private long slotsWinAmount;
    private long craftedItems;
    private long repairedItems;
    private long salvagedItems;
    private long toolsBroken;

    @JsonCreator
    @ConstructorProperties({"id", "gambleWins", "slotsWins", "gambleWinAmount", "slotsWinAmount", "data"})
    public RethinkPlayerStats(@JsonProperty("id") String id, @JsonProperty("gambleWins") long gambleWins, @JsonProperty("slotsWins") long slotsWins, @JsonProperty("gambleWinAmount") long gambleWinAmount, @JsonProperty("slotsWinAmount") long slotsWinAmount, @JsonProperty("data") PlayerStatsData data) {
        this.id = id;
        this.gambleWins = gambleWins;
        this.slotsWins = slotsWins;
        this.gambleWinAmount = gambleWinAmount;
        this.slotsWinAmount = slotsWinAmount;
        this.data = data;
    }

    public static RethinkPlayerStats of(String userId) {
        return new RethinkPlayerStats(userId, 0L, 0L, 0L, 0L, new PlayerStatsData());
    }

    @JsonIgnore
    public void incrementGambleWins() {
        this.gambleWins += 1;
    }

    @JsonIgnore
    public void incrementSlotsWins() {
        this.slotsWins += 1;
    }

    @JsonIgnore
    public void addGambleWin(long amount) {
        this.gambleWinAmount += amount;
    }

    @JsonIgnore
    public void addSlotsWin(long amount) {
        this.slotsWinAmount += amount;
    }

    
    public String getId() {
        return this.id;
    }

    
    @Override
    public String getTableName() {
        return DB_TABLE;
    }

    @JsonIgnore
    
    @Override
    public String getDatabaseId() {
        return getId();
    }

    public long getGambleWins() {
        return this.gambleWins;
    }

    public long getSlotsWins() {
        return this.slotsWins;
    }

    public long getGambleWinAmount() {
        return this.gambleWinAmount;
    }

    public long getSlotsWinAmount() {
        return this.slotsWinAmount;
    }

    public PlayerStatsData getData() {
        return this.data;
    }

    public long getCraftedItems() {
        return craftedItems;
    }

    public void setCraftedItems(long craftedItems) {
        this.craftedItems = craftedItems;
    }

    public long getRepairedItems() {
        return repairedItems;
    }

    public void setRepairedItems(long repairedItems) {
        this.repairedItems = repairedItems;
    }

    public long getToolsBroken() {
        return toolsBroken;
    }

    public void setToolsBroken(long toolsBroken) {
        this.toolsBroken = toolsBroken;
    }

    public long getSalvagedItems() {
        return salvagedItems;
    }

    public void setSalvagedItems(long salvagedItems) {
        this.salvagedItems = salvagedItems;
    }

    @JsonIgnore
    public void incrementToolsBroken() {
        this.toolsBroken++;
    }

    @JsonIgnore
    public void incrementCraftedItems() {
        this.craftedItems++;
    }

    @JsonIgnore
    public void incrementCraftedItems(int amount) {
        this.craftedItems += amount;
    }

    @JsonIgnore
    public void incrementRepairedItems() {
        this.repairedItems++;
    }

    @JsonIgnore
    public void incrementSalvagedItems() {
        this.salvagedItems++;
    }
}
