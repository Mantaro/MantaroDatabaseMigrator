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
import net.kodehawa.migrator.helpers.Badge;
import net.kodehawa.migrator.helpers.HousePet;
import net.kodehawa.migrator.helpers.InventorySortType;
import net.kodehawa.migrator.helpers.Item;
import net.kodehawa.migrator.helpers.ItemStack;
import net.kodehawa.migrator.helpers.PetChoice;
import net.kodehawa.migrator.helpers.PotionEffect;
import net.kodehawa.migrator.helpers.ProfileComponent;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Player implements ManagedMongoObject {
    @BsonIgnore
    public static final String DB_TABLE = "players";
    @BsonIgnore
    private final MongoInventory inventoryObject = new MongoInventory();
    @BsonIgnore
    public Map<String, Object> fieldTracker = new HashMap<>();

    @BsonId
    private String id;
    private long level;
    private long oldMoney;
    private long reputation;
    private long experience = 0;
    private long newMoney = 0L;
    private long dailyStreak;
    private String description = null;
    private long gamesWon = 0;
    private long lastDailyAt;
    private long lockedUntil = 0;
    private Long marriedSince = null;
    private String marriedWith = null;
    private long moneyOnBank = 0;
    //null = most important badge shows.
    private Badge mainBadge = null;
    private long marketUsed;
    private boolean showBadge = true;
    private PotionEffect activePotion;
    private PotionEffect activeBuff;
    private long waifuCachedValue;
    private boolean claimLocked = false;
    private long miningExperience;
    private long fishingExperience;
    private long chopExperience;
    private long timesMopped;
    private long cratesOpened;
    private long sharksCaught;
    private boolean waifuout;
    private int lastCrateGiven = 69;
    private long lastSeenCampaign;
    private boolean resetWarning = false;
    private InventorySortType inventorySortType = InventorySortType.AMOUNT;
    private boolean hiddenLegacy = false;
    private boolean newPlayerNotice = false;
    private long petSlots = 4;
    private PetChoice petChoice = null;
    private HousePet pet;
    private List<Badge> badges = new ArrayList<>();
    private List<ProfileComponent> profileComponents = new LinkedList<>();
    private Map<String, Integer> inventory = new HashMap<>();

    public Player() {}

    private Player(String id, Long level, Long oldMoney, Long reputation, Map<String, Integer> inventory) {
        this.id = id;
        this.level = level == null ? 0 : level;
        this.oldMoney = oldMoney == null ? 0 : oldMoney;
        this.reputation = reputation == null ? 0 : reputation;
        this.inventoryObject.replaceWith(MongoInventory.unserialize(inventory));
    }

    /**
     * The Player.of methods are for resetting players or creating new ones when they don't exist.
     *
     * @param userId The user to create or reset.
     * @return The new Player.
     */
    public static Player of(String userId) {
        return new Player(userId, 0L, 0L, 0L, new HashMap<>());
    }

    @BsonIgnore
    public boolean hasBadge(Badge b) {
        return badges.contains(b);
    }

    public boolean isClaimLocked() {
        return claimLocked;
    }

    public long getExperience() {
        return this.experience;
    }

    public List<Badge> getBadges() {
        return this.badges;
    }

    public long getDailyStreak() {
        return this.dailyStreak;
    }

    public String getDescription() {
        return this.description;
    }

    public long getGamesWon() {
        return this.gamesWon;
    }

    public long getLastDailyAt() {
        return this.lastDailyAt;
    }

    public long getLockedUntil() {
        return this.lockedUntil;
    }

    public Long getMarriedSince() {
        return this.marriedSince;
    }

    public String getMarriedWith() {
        return this.marriedWith;
    }

    public long getMoneyOnBank() {
        return this.moneyOnBank;
    }

    public Badge getMainBadge() {
        return this.mainBadge;
    }

    public long getMarketUsed() {
        return this.marketUsed;
    }

    public boolean isShowBadge() {
        return this.showBadge;
    }

    public PotionEffect getActivePotion() {
        return this.activePotion;
    }

    public PotionEffect getActiveBuff() {
        return this.activeBuff;
    }

    public long getWaifuCachedValue() {
        return this.waifuCachedValue;
    }

    public List<ProfileComponent> getProfileComponents() {
        return this.profileComponents;
    }

    public long getPetSlots() {
        return this.petSlots;
    }

    public long getMiningExperience() {
        return miningExperience;
    }

    public long getFishingExperience() {
        return fishingExperience;
    }

    public long getTimesMopped() {
        return timesMopped;
    }

    public long getCratesOpened() {
        return cratesOpened;
    }

    public long getSharksCaught() {
        return sharksCaught;
    }

    public boolean isWaifuout() {
        return waifuout;
    }

    public int getLastCrateGiven() {
        return lastCrateGiven;
    }

    public long getChopExperience() {
        return chopExperience;
    }

    public Map<String, Integer> getInventory() {
        return MongoInventory.serialize(inventoryObject.asList());
    }

    // -- Setters (public if possible)
    public void setClaimLocked(boolean claimLocked) {
        this.claimLocked = claimLocked;
    }

    // Unused, only used for migration
    public void setExperience(long experience) {
        this.experience = experience;
    }

    // Unused, only used for migration
    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    // Unused, only used for migration
    public void setMiningExperience(long miningExperience) {
        this.miningExperience = miningExperience;
    }

    // Unused, only used for migration
    public void setFishingExperience(long fishingExperience) {
        this.fishingExperience = fishingExperience;
    }

    public void setDailyStreak(long dailyStreak) {
        this.dailyStreak = dailyStreak;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGamesWon(long gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void setLastDailyAt(long lastDailyAt) {
        this.lastDailyAt = lastDailyAt;
    }

    public void setLockedUntil(long lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public void setMarriedSince(Long marriedSince) {
        this.marriedSince = marriedSince;
    }

    public void setMarriedWith(String marriedWith) {
        this.marriedWith = marriedWith;
    }

    public void setMoneyOnBank(long moneyOnBank) {
        this.moneyOnBank = moneyOnBank;
    }

    public void setMainBadge(Badge mainBadge) {
        this.mainBadge = mainBadge;
    }

    public void setShowBadge(boolean showBadge) {
        this.showBadge = showBadge;
    }

    public void setMarketUsed(long marketUsed) {
        this.marketUsed = marketUsed;
    }

    public void setActivePotion(PotionEffect activePotion) {
        this.activePotion = activePotion;
    }

    public void setActiveBuff(PotionEffect activeBuff) {
        this.activeBuff = activeBuff;
    }

    public void setWaifuCachedValue(long waifuCachedValue) {
        this.waifuCachedValue = waifuCachedValue;
    }

    public void setProfileComponents(List<ProfileComponent> profileComponents) {
        this.profileComponents = profileComponents;
    }

    public void setPetSlots(long petSlots) {
        this.petSlots = petSlots;
    }

    public void setTimesMopped(long timesMopped) {
        this.timesMopped = timesMopped;
    }

    public void setCratesOpened(long cratesOpened) {
        this.cratesOpened = cratesOpened;
    }

    public void setSharksCaught(long sharksCaught) {
        this.sharksCaught = sharksCaught;
    }

    public void setWaifuout(boolean waifuout) {
        this.waifuout = waifuout;
    }

    public void setLastCrateGiven(int lastCrateGiven) {
        this.lastCrateGiven = lastCrateGiven;
    }

    public void setNewMoney(long newMoney) {
        this.newMoney = newMoney;
    }

    public void setInventorySortType(InventorySortType inventorySortType) {
        this.inventorySortType = inventorySortType;
    }

    public void setHiddenLegacy(boolean hiddenLegacy) {
        this.hiddenLegacy = hiddenLegacy;
    }

    public void setNewPlayerNotice(boolean newPlayerNotice) {
        this.newPlayerNotice = newPlayerNotice;
    }

    public void setOldMoney(long newAmount) {
        this.oldMoney = newAmount;
    }

    public void setReputation(Long reputation) {
        this.reputation = reputation;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public void setChopExperience(long chopExperience) {
        this.chopExperience = chopExperience;
    }

    public void setLastSeenCampaign(long lastSeenCampaign) {
        this.lastSeenCampaign = lastSeenCampaign;
    }

    public void setPetChoice(PetChoice petChoice) {
        this.petChoice = petChoice;
    }

    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
        this.inventoryObject.replaceWith(MongoInventory.unserialize(inventory));
    }

    // -- Tracking setters (always public)
    @BsonIgnore
    public void timesMopped(long timesMopped) {
        this.timesMopped = timesMopped;
        fieldTracker.put("timesMopped", this.timesMopped);
    }

    @BsonIgnore
    public void sharksCaught(long sharksCaught) {
        this.sharksCaught = sharksCaught;
        fieldTracker.put("sharksCaught", this.sharksCaught);
    }

    @BsonIgnore
    public void waifuout(boolean waifuout) {
        this.waifuout = waifuout;
        fieldTracker.put("waifuout", this.waifuout);
    }

    @BsonIgnore
    public void lastCrateGiven(int lastCrateGiven) {
        this.lastCrateGiven = lastCrateGiven;
        fieldTracker.put("lastCrateGiven", this.lastCrateGiven);
    }

    @BsonIgnore
    public void inventorySortType(InventorySortType inventorySortType) {
        this.inventorySortType = inventorySortType;
        fieldTracker.put("inventorySortType", this.inventorySortType);
    }

    @BsonIgnore
    public void hiddenLegacy(boolean hiddenLegacy) {
        this.hiddenLegacy = hiddenLegacy;
        fieldTracker.put("hiddenLegacy", this.hiddenLegacy);
    }

    @BsonIgnore
    public void newPlayerNotice(boolean newPlayerNotice) {
        this.newPlayerNotice = newPlayerNotice;
        fieldTracker.put("newPlayerNotice", this.newPlayerNotice);
    }

    @BsonIgnore
    public void reputation(Long reputation) {
        this.reputation = reputation;
        fieldTracker.put("reputation", this.reputation);
    }

    @BsonIgnore
    public void level(long level) {
        this.level = level;
        fieldTracker.put("level", this.level);
    }

    @BsonIgnore
    public void cratesOpened(long cratesOpened) {
        this.cratesOpened = cratesOpened;
        fieldTracker.put("cratesOpened", this.cratesOpened);
    }

    @BsonIgnore
    public void waifuCachedValue(long waifuCachedValue) {
        this.waifuCachedValue = waifuCachedValue;
        fieldTracker.put("waifuCachedValue", this.waifuCachedValue);
    }

    @BsonIgnore
    public void profileComponents(List<ProfileComponent> profileComponents) {
        this.profileComponents = profileComponents;
        fieldTracker.put("profileComponents", this.profileComponents);
    }

    @BsonIgnore
    public void showBadge(boolean showBadge) {
        this.showBadge = showBadge;
        fieldTracker.put("showBadge", this.showBadge);
    }

    @BsonIgnore
    public void mainBadge(Badge mainBadge) {
        this.mainBadge = mainBadge;
        fieldTracker.put("mainBadge", this.mainBadge);
    }

    @BsonIgnore
    public void gamesWon(long gamesWon) {
        this.gamesWon = gamesWon;
        fieldTracker.put("gamesWon", this.gamesWon);
    }

    @BsonIgnore
    public void petChoice(PetChoice petChoice) {
        this.petChoice = petChoice;
        fieldTracker.put("petChoice", this.petChoice);
    }

    @BsonIgnore
    public void lastDailyAt(long lastDailyAt) {
        this.lastDailyAt = lastDailyAt;
        fieldTracker.put("lastDailyAt", this.lastDailyAt);
    }

    @BsonIgnore
    public void marketUsed(long marketUsed) {
        this.marketUsed = marketUsed;
        fieldTracker.put("marketUsed", this.marketUsed);
    }

    @BsonIgnore
    public void description(String description) {
        this.description = description;
        fieldTracker.put("description", this.description);
    }

    @BsonIgnore
    public void dailyStreak(long dailyStreak) {
        this.dailyStreak = dailyStreak;
        fieldTracker.put("dailyStreak", this.dailyStreak);
    }

    @BsonIgnore
    public void claimLocked(boolean claimLocked) {
        this.claimLocked = claimLocked;
        fieldTracker.put("claimLocked", this.claimLocked);
    }

    // -- Helpers
    @BsonIgnore
    public void resetProfileComponents() {
        profileComponents.clear();
        fieldTracker.put("profileComponents", profileComponents);
    }

    @BsonIgnore
    public void incrementMiningExperience(Random random) {
        this.miningExperience = miningExperience + (random.nextInt(5) + 1);
        fieldTracker.put("miningExperience", miningExperience);
    }

    @BsonIgnore
    public void incrementFishingExperience(Random random) {
        this.fishingExperience = fishingExperience + (random.nextInt(5) + 1);
        fieldTracker.put("fishingExperience", fishingExperience);
    }

    @BsonIgnore
    public void incrementChopExperience(Random random) {
        this.chopExperience = chopExperience + (random.nextInt(5) + 1);
        fieldTracker.put("chopExperience", chopExperience);
    }

    @BsonProperty("inventory")
    public Map<String, Integer> rawInventory() {
        return MongoInventory.serialize(inventoryObject.asList());
    }

    public long getNewMoney() {
        return newMoney;
    }

    public long getLastSeenCampaign() {
        return lastSeenCampaign;
    }

    public boolean isResetWarning() {
        return resetWarning;
    }

    public void setResetWarning(boolean resetWarning) {
        this.resetWarning = resetWarning;
    }

    public InventorySortType getInventorySortType() {
        return inventorySortType;
    }

    public boolean isHiddenLegacy() {
        return hiddenLegacy;
    }

    public boolean isNewPlayerNotice() {
        return newPlayerNotice;
    }

    public void setPet(HousePet pet) {
        this.pet = pet;
    }

    public HousePet getPet() {
        return pet;
    }

    public PetChoice getPetChoice() {
        return petChoice;
    }

    public long getOldMoney() {
        return oldMoney;
    }

    public long getReputation() {
        return this.reputation;
    }

    public Long getLevel() {
        return this.level;
    }

    @BsonIgnore
    public int getItemAmount(Item item) {
        return inventoryObject.getAmount(item);
    }

    @BsonIgnore
    public void processItem(Item item, int amount) {
        inventoryObject.process(new ItemStack(item, amount));
        fieldTracker.put("inventory", getInventory());
    }

    @BsonIgnore
    public void processItem(ItemStack stack) {
        inventoryObject.process(stack);
        fieldTracker.put("inventory", getInventory());
    }

    @BsonIgnore
    public void processItems(List<ItemStack> stack) {
        inventoryObject.process(stack);
        fieldTracker.put("inventory", getInventory());
    }

    @BsonIgnore
    public boolean mergeInventory(List<ItemStack> stack) {
        var merge = inventoryObject.merge(stack);
        fieldTracker.put("inventory", getInventory());
        return merge;
    }

    @BsonIgnore
    public boolean containsItem(Item item) {
        return inventoryObject.containsItem(item);
    }

    @BsonIgnore
    public List<ItemStack> getInventoryList() {
        return inventoryObject.asList();
    }

    @BsonIgnore
    public void markPetChange() {
        fieldTracker.put("pet", this.pet);
    }

    @BsonIgnore
    public boolean addBadgeIfAbsent(Badge b) {
        if (hasBadge(b)) {
            return false;
        }

        badges.add(b);
        fieldTracker.put("badges", this.badges);
        return true;
    }

    @BsonIgnore
    public boolean removeBadge(Badge b) {
        if (!hasBadge(b)) {
            return false;
        }

        badges.remove(b);
        fieldTracker.put("badges", this.badges);
        return true;
    }

    @BsonIgnore
    public PetChoice getActiveChoice(Marriage marriage) {
        if (getPetChoice() == null) {
            if (marriage == null || marriage.getPet() == null) {
                return PetChoice.PERSONAL;
            } else {
                return PetChoice.MARRIAGE;
            }
        } else {
            return getPetChoice();
        }
    }

    @BsonIgnore
    public boolean shouldSeeCampaign() {
        return System.currentTimeMillis() > (getLastSeenCampaign() + TimeUnit.HOURS.toMillis(12));
    }

    @BsonIgnore
    public void markCampaignAsSeen() {
        this.lastSeenCampaign = System.currentTimeMillis();
    }

    /**
     * Adds x amount of reputation to a player. Normally 1.
     *
     * @param rep how much?
     */
    @BsonIgnore
    public void addReputation(long rep) {
        this.reputation += rep;
        this.setReputation(reputation);
        fieldTracker.put("reputation", this.reputation);
    }

    //it's 3am and i cba to replace usages of this so whatever
    @BsonIgnore
    public boolean isLocked() {
        return getLockedUntil() - System.currentTimeMillis() > 0;
    }

    @BsonIgnore
    public void locked(boolean locked) {
        setLockedUntil(locked ? System.currentTimeMillis() + 35000 : 0);
        fieldTracker.put("lockedUntil", lockedUntil);
    }

    
    public String getId() {
        return this.id;
    }

    @BsonIgnore
    @Override
    
    public String getTableName() {
        return DB_TABLE;
    }

    @BsonIgnore
    
    @Override
    public String getDatabaseId() {
        return getId();
    }

    @Override
    public void save() {
        Migrator.saveMongo(this, Player.class);
    }

    @Override
    public void delete() {
        Migrator.deleteMongo(this, Player.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return level == player.level && oldMoney == player.oldMoney && reputation == player.reputation && experience == player.experience && newMoney == player.newMoney && dailyStreak == player.dailyStreak && gamesWon == player.gamesWon && lastDailyAt == player.lastDailyAt && lockedUntil == player.lockedUntil && moneyOnBank == player.moneyOnBank && marketUsed == player.marketUsed && showBadge == player.showBadge && waifuCachedValue == player.waifuCachedValue && claimLocked == player.claimLocked && miningExperience == player.miningExperience && fishingExperience == player.fishingExperience && chopExperience == player.chopExperience && timesMopped == player.timesMopped && cratesOpened == player.cratesOpened && sharksCaught == player.sharksCaught && waifuout == player.waifuout && lastCrateGiven == player.lastCrateGiven && lastSeenCampaign == player.lastSeenCampaign && resetWarning == player.resetWarning && hiddenLegacy == player.hiddenLegacy && newPlayerNotice == player.newPlayerNotice && petSlots == player.petSlots && Objects.equals(fieldTracker, player.fieldTracker) && Objects.equals(id, player.id) && Objects.equals(description, player.description) && Objects.equals(marriedSince, player.marriedSince) && Objects.equals(marriedWith, player.marriedWith) && mainBadge == player.mainBadge && Objects.equals(activePotion, player.activePotion) && Objects.equals(activeBuff, player.activeBuff) && inventorySortType == player.inventorySortType && petChoice == player.petChoice && Objects.equals(pet, player.pet) && Objects.equals(badges, player.badges) && Objects.equals(profileComponents, player.profileComponents) && Objects.equals(inventory, player.inventory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, level, oldMoney, reputation, experience, newMoney, dailyStreak, description, gamesWon, lastDailyAt, lockedUntil, marriedSince, marriedWith, moneyOnBank, mainBadge, marketUsed, showBadge, activePotion, activeBuff, waifuCachedValue, claimLocked, miningExperience, fishingExperience, chopExperience, timesMopped, cratesOpened, sharksCaught, waifuout, lastCrateGiven, lastSeenCampaign, resetWarning, inventorySortType, hiddenLegacy, newPlayerNotice, petSlots, petChoice, pet, badges, profileComponents, inventory);
    }
}
