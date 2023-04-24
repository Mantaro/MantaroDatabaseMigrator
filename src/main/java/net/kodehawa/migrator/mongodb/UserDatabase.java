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

import net.kodehawa.migrator.Migrator;
import net.kodehawa.migrator.helpers.PlayerEquipment;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Reminder: all setters MUST be public!
public class UserDatabase implements ManagedMongoObject {
    @BsonIgnore
    public static final String DB_TABLE = "users";
    @BsonIgnore
    public Map<String, Object> fieldTracker = new HashMap<>();

    @BsonId
    private String id;
    private long premiumUntil;
    private String birthday;
    private boolean receivedFirstKey;
    private String premiumKey;
    private int remindedTimes;
    private String timezone;
    private String lang;
    private int dustLevel; //percentage
    private PlayerEquipment equippedItems = new PlayerEquipment(new EnumMap<>(PlayerEquipment.EquipmentType.class), new EnumMap<>(PlayerEquipment.EquipmentType.class), new EnumMap<>(PlayerEquipment.EquipmentType.class)); //hashmap is type -> itemId
    private boolean receivedExpirationWarning = false; //premium key about to expire!
    private Map<String, String> keysClaimed = new HashMap<>(); //Map of user -> key. Will be used to account for keys the user can create themselves.

    // NEW MARRIAGE SYSTEM
    private String marriageId;

    // user id, value bought for.
    private Map<String, Long> waifus = new HashMap<>();
    private int waifuSlots = 3;
    private int timesClaimed;

    // Persistent reminders. UUID is saved here.
    private List<String> reminders = new ArrayList<>();

    // Hide tag (and ID on waifu) on marriage/waifu list
    private boolean privateTag = false; //just explicitly setting it to false to make sure people know it's the default.
    private boolean autoEquip = false;
    private boolean actionsDisabled = false;

    // Mongo serialization
    public UserDatabase() { }

    public UserDatabase(String id, long premiumUntil) {
        this.id = id;
        this.premiumUntil = premiumUntil;
    }

    public static UserDatabase of(String id) {
        return new UserDatabase(id, 0);
    }

    // --- Getters
    public String getBirthday() {
        return this.birthday;
    }

    public String getPremiumKey() {
        return this.premiumKey;
    }

    public int getRemindedTimes() {
        return this.remindedTimes;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public String getLang() {
        return this.lang;
    }

    public int getDustLevel() {
        return this.dustLevel;
    }

    // TODO: Need to track changes for this object?...
    public PlayerEquipment getEquippedItems() {
        return this.equippedItems;
    }

    public String getMarriageId() {
        return this.marriageId;
    }

    public int getWaifuSlots() {
        return this.waifuSlots;
    }

    public int getTimesClaimed() {
        return this.timesClaimed;
    }

    public boolean isPrivateTag() {
        return this.privateTag;
    }

    public boolean isAutoEquip() {
        return autoEquip;
    }

    public boolean isActionsDisabled() {
        return actionsDisabled;
    }

    public boolean getReceivedFirstKey() {
        return this.receivedFirstKey;
    }

    public boolean getReceivedExpirationWarning() {
        return this.receivedExpirationWarning;
    }

    public Map<String, Long> getWaifus() {
        return this.waifus;
    }

    public Map<String, String> getKeysClaimed() {
        return this.keysClaimed;
    }

    public List<String> getReminders() {
        return this.reminders;
    }

    @BsonIgnore
    public boolean hasReceivedFirstKey() {
        return this.receivedFirstKey;
    }

    @BsonIgnore
    public boolean hasReceivedExpirationWarning() {
        return this.receivedExpirationWarning;
    }

    // --- Setters needed for serialization (unless I want to make the structure more rigid and use a constructor)
    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setPremiumKey(String premiumKey) {
        this.premiumKey = premiumKey;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setDustLevel(int dustLevel) {
        this.dustLevel = dustLevel;
    }

    public void setMarriageId(String marriageId) {
        this.marriageId = marriageId;
    }

    public void setWaifuSlots(int waifuSlots) {
        this.waifuSlots = waifuSlots;
    }

    public void setTimesClaimed(int timesClaimed) {
        this.timesClaimed = timesClaimed;
    }

    public void setPrivateTag(boolean privateTag) {
        this.privateTag = privateTag;
    }

    public void setAutoEquip(boolean autoEquip) {
        this.autoEquip = autoEquip;
    }

    public void setActionsDisabled(boolean actionsDisabled) {
        this.actionsDisabled = actionsDisabled;
    }

    public void setReceivedFirstKey(boolean hasReceivedFirstKey) {
        this.receivedFirstKey = hasReceivedFirstKey;
    }

    public void setReceivedExpirationWarning(boolean receivedExpirationWarning) {
        this.receivedExpirationWarning = receivedExpirationWarning;
    }

    // --- Unused (?) setters, also definitely needed for serialization.
    public void setWaifus(Map<String, Long> waifus) {
        this.waifus = waifus;
    }

    public void setReminders(List<String> reminders) {
        this.reminders = reminders;
    }

    public void setRemindedTimes(int remindedTimes) {
        this.remindedTimes = remindedTimes;
    }

    public void setEquippedItems(PlayerEquipment equippedItems) {
        this.equippedItems = equippedItems;
    }

    public void setKeysClaimed(Map<String, String> keysClaimed) {
        this.keysClaimed = keysClaimed;
    }

    public void setPremiumUntil(long premiumUntil) {
        this.premiumUntil = premiumUntil;
    }

    // --- Track changes to use update
    @BsonIgnore
    public void actionsDisabled(boolean actionsDisabled) {
        this.actionsDisabled = actionsDisabled;
        fieldTracker.put("actionsDisabled", this.actionsDisabled);
    }

    @BsonIgnore
    public void receivedFirstKey(boolean hasReceivedFirstKey) {
        this.receivedFirstKey = hasReceivedFirstKey;
        fieldTracker.put("receivedFirstKey", this.hasReceivedFirstKey());
    }

    @BsonIgnore
    public void receivedExpirationWarning(boolean receivedExpirationWarning) {
        this.receivedExpirationWarning = receivedExpirationWarning;
        fieldTracker.put("receivedExpirationWarning", this.receivedExpirationWarning);
    }

    @BsonIgnore
    public void autoEquip(boolean autoEquip) {
        this.autoEquip = autoEquip;
        fieldTracker.put("autoEquip", this.autoEquip);
    }

    @BsonIgnore
    public void privateTag(boolean privateTag) {
        this.privateTag = privateTag;
        fieldTracker.put("privateTag", this.privateTag);
    }

    @BsonIgnore
    public void waifuSlots(int waifuSlots) {
        this.waifuSlots = waifuSlots;
        fieldTracker.put("waifuSlots", this.waifuSlots);
    }

    @BsonIgnore
    public void marriageId(String marriageId) {
        this.marriageId = marriageId;
        fieldTracker.put("marriageId", this.marriageId);
    }

    @BsonIgnore
    public void dustLevel(int dustLevel) {
        this.dustLevel = dustLevel;
        fieldTracker.put("dustLevel", this.dustLevel);
    }

    @BsonIgnore
    public void timezone(String timezone) {
        this.timezone = timezone;
        fieldTracker.put("timezone", this.timezone);
    }

    @BsonIgnore
    public void language(String lang) {
        this.lang = lang;
        fieldTracker.put("lang", this.lang);
    }

    @BsonIgnore
    public void birthday(String birthday) {
        this.birthday = birthday;
        fieldTracker.put("birthday", this.birthday);
    }

    @BsonIgnore
    public void premiumKey(String premiumKey) {
        this.premiumKey = premiumKey;
        fieldTracker.put("premiumKey", this.premiumKey);
    }

    public String getId() {
        return this.id;
    }

    @BsonIgnore
    @Override
    public String getTableName() {
        return DB_TABLE;
    }

    @Override
    public void save() {
        Migrator.saveMongo(this, UserDatabase.class);
    }

    @Override
    public void delete() {
        Migrator.deleteMongo(this, UserDatabase.class);
    }

    public long getPremiumUntil() {
        return this.premiumUntil;
    }
}
