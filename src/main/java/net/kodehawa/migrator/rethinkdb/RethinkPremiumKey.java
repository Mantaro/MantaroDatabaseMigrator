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
import net.kodehawa.migrator.helpers.special.helpers.KeyType;
import net.kodehawa.migrator.rethinkdb.helpers.PremiumKeyData;

import java.beans.ConstructorProperties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RethinkPremiumKey implements ManagedObject {
    public static final String DB_TABLE = "keys";
    private long duration;
    private boolean enabled;
    private long expiration;
    private String id;
    private String owner;
    private int type;
    //Setting a default to avoid backwards compat issues.
    private PremiumKeyData data = new PremiumKeyData();

    @JsonCreator
    @ConstructorProperties({"id", "duration", "expiration", "type", "enabled", "owner"})
    public RethinkPremiumKey(@JsonProperty("id") String id, @JsonProperty("duration") long duration,
                             @JsonProperty("expiration") long expiration, @JsonProperty("type") KeyType type,
                             @JsonProperty("enabled") boolean enabled, @JsonProperty("owner") String owner, @JsonProperty("data") PremiumKeyData data) {
        this.id = id;
        this.duration = duration;
        this.expiration = expiration;
        this.type = type.ordinal();
        this.enabled = enabled;
        this.owner = owner;
        if (data != null)
            this.data = data;
    }

    @JsonIgnore
    public RethinkPremiumKey() {
    }

    @JsonIgnore
    public static RethinkPremiumKey generatePremiumKey(String owner, KeyType type, boolean linked) {
        String premiumId = UUID.randomUUID().toString();
        RethinkPremiumKey newKey = new RethinkPremiumKey(premiumId, -1, -1, type, false, owner, new PremiumKeyData());
        if (linked)
            newKey.data.setLinkedTo(owner); //used for patreon checks in newly-activated keys (if applicable)

        newKey.save();
        return newKey;
    }

    @JsonIgnore
    public static RethinkPremiumKey generatePremiumKeyTimed(String owner, KeyType type, int days, boolean linked) {
        String premiumId = UUID.randomUUID().toString();
        RethinkPremiumKey newKey = new RethinkPremiumKey(premiumId, TimeUnit.DAYS.toMillis(days), currentTimeMillis() + TimeUnit.DAYS.toMillis(days), type, false, owner, new PremiumKeyData());
        if (linked)
            newKey.data.setLinkedTo(owner); //used for patreon checks in newly-activated keys (if applicable)

        newKey.save();
        return newKey;
    }

    @JsonIgnore
    public KeyType getParsedType() {
        return KeyType.values()[type];
    }

    @JsonIgnore
    public long getDurationDays() {
        return TimeUnit.MILLISECONDS.toDays(duration);
    }

    @JsonIgnore
    public long validFor() {
        return TimeUnit.MILLISECONDS.toDays(getExpiration() - currentTimeMillis());
    }

    @JsonIgnore
    public long validForMs() {
        return getExpiration() - currentTimeMillis();
    }

    @JsonIgnore
    public void activate(int days) {
        this.enabled = true;
        this.duration = TimeUnit.DAYS.toMillis(days);
        this.expiration = currentTimeMillis() + TimeUnit.DAYS.toMillis(days);
        save();
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

    @JsonIgnore
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

    public PremiumKeyData getData() {
        return this.data;
    }
}
