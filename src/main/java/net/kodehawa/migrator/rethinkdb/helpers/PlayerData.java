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

package net.kodehawa.migrator.rethinkdb.helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.kodehawa.migrator.helpers.Badge;
import net.kodehawa.migrator.helpers.HousePet;
import net.kodehawa.migrator.helpers.InventorySortType;
import net.kodehawa.migrator.helpers.PetChoice;
import net.kodehawa.migrator.helpers.PotionEffect;
import net.kodehawa.migrator.helpers.ProfileComponent;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerData {
    public long experience = 0;
    public long newMoney = 0L;
    private List<Badge> badges = new ArrayList<>();
    //Fix massive misspelling fuck up.
    @JsonProperty("dailyStrike")
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
    private List<ProfileComponent> profileComponents = new LinkedList<>();
    private boolean isClaimLocked = false;
    private long miningExperience;
    private long fishingExperience;
    private long chopExperience;
    private long timesMopped;
    private long cratesOpened;
    private long sharksCaught;
    private boolean waifuout;
    private int lastCrateGiven = 69;
    private long lastSeenCampaign;
    private int questQuota = 3;
    private boolean resetWarning = false;
    private InventorySortType inventorySortType = InventorySortType.AMOUNT;
    private boolean hiddenLegacy = false;
    private boolean newPlayerNotice = false;
    private long petSlots = 4;
    private PetChoice petChoice = null;
    // main pet
    private HousePet pet;

    public PlayerData() { }

    @JsonIgnore
    public boolean hasBadge(Badge b) {
        return badges.contains(b);
    }

    @JsonIgnore
    public boolean addBadgeIfAbsent(Badge b) {
        if (hasBadge(b)) {
            return false;
        }

        badges.add(b);
        return true;
    }

    @JsonIgnore
    public boolean removeBadge(Badge b) {
        if (!hasBadge(b)) {
            return false;
        }

        badges.remove(b);
        return true;
    }

    public boolean isClaimLocked() {
        return isClaimLocked;
    }

    public void setClaimLocked(boolean claimLocked) {
        isClaimLocked = claimLocked;
    }

    public long getExperience() {
        return this.experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public List<Badge> getBadges() {
        return this.badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }

    public long getDailyStreak() {
        return this.dailyStreak;
    }

    public void setDailyStreak(long dailyStreak) {
        this.dailyStreak = dailyStreak;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getGamesWon() {
        return this.gamesWon;
    }

    public void setGamesWon(long gamesWon) {
        this.gamesWon = gamesWon;
    }

    public long getLastDailyAt() {
        return this.lastDailyAt;
    }

    public void setLastDailyAt(long lastDailyAt) {
        this.lastDailyAt = lastDailyAt;
    }

    public long getLockedUntil() {
        return this.lockedUntil;
    }

    public void setLockedUntil(long lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public Long getMarriedSince() {
        return this.marriedSince;
    }

    public void setMarriedSince(Long marriedSince) {
        this.marriedSince = marriedSince;
    }

    public String getMarriedWith() {
        return this.marriedWith;
    }

    public void setMarriedWith(String marriedWith) {
        this.marriedWith = marriedWith;
    }

    public long getMoneyOnBank() {
        return this.moneyOnBank;
    }

    public void setMoneyOnBank(long moneyOnBank) {
        this.moneyOnBank = moneyOnBank;
    }

    public Badge getMainBadge() {
        return this.mainBadge;
    }

    public void setMainBadge(Badge mainBadge) {
        this.mainBadge = mainBadge;
    }

    public long getMarketUsed() {
        return this.marketUsed;
    }

    public void setMarketUsed(long marketUsed) {
        this.marketUsed = marketUsed;
    }

    public boolean isShowBadge() {
        return this.showBadge;
    }

    public void setShowBadge(boolean showBadge) {
        this.showBadge = showBadge;
    }

    public PotionEffect getActivePotion() {
        return this.activePotion;
    }

    public void setActivePotion(PotionEffect activePotion) {
        this.activePotion = activePotion;
    }

    public PotionEffect getActiveBuff() {
        return this.activeBuff;
    }

    public void setActiveBuff(PotionEffect activeBuff) {
        this.activeBuff = activeBuff;
    }

    public long getWaifuCachedValue() {
        return this.waifuCachedValue;
    }

    public void setWaifuCachedValue(long waifuCachedValue) {
        this.waifuCachedValue = waifuCachedValue;
    }

    public List<ProfileComponent> getProfileComponents() {
        return this.profileComponents;
    }

    public void setProfileComponents(List<ProfileComponent> profileComponents) {
        this.profileComponents = profileComponents;
    }

    public long getPetSlots() {
        return this.petSlots;
    }

    public void setPetSlots(long petSlots) {
        this.petSlots = petSlots;
    }

    public long getMiningExperience() {
        return miningExperience;
    }

    public void setMiningExperience(long miningExperience) {
        this.miningExperience = miningExperience;
    }

    public long getFishingExperience() {
        return fishingExperience;
    }

    public void setFishingExperience(long fishingExperience) {
        this.fishingExperience = fishingExperience;
    }

    public long getTimesMopped() {
        return timesMopped;
    }

    public void setTimesMopped(long timesMopped) {
        this.timesMopped = timesMopped;
    }

    public long getCratesOpened() {
        return cratesOpened;
    }

    public void setCratesOpened(long cratesOpened) {
        this.cratesOpened = cratesOpened;
    }

    public long getSharksCaught() {
        return sharksCaught;
    }

    public void setSharksCaught(long sharksCaught) {
        this.sharksCaught = sharksCaught;
    }

    public boolean isWaifuout() {
        return waifuout;
    }

    public void setWaifuout(boolean waifuout) {
        this.waifuout = waifuout;
    }

    public int getLastCrateGiven() {
        return lastCrateGiven;
    }

    public void setLastCrateGiven(int lastCrateGiven) {
        this.lastCrateGiven = lastCrateGiven;
    }

    public long getChopExperience() {
        return chopExperience;
    }

    public void setChopExperience(long chopExperience) {
        this.chopExperience = chopExperience;
    }

    public int getQuestQuota() {
        return questQuota;
    }

    public void setQuestQuota(int questQuota) {
        this.questQuota = questQuota;
    }

    @JsonIgnore
    public void incrementMiningExperience(Random random) {
        this.miningExperience = miningExperience + (random.nextInt(5) + 1);
    }

    @JsonIgnore
    public void incrementFishingExperience(Random random) {
        this.fishingExperience = fishingExperience + (random.nextInt(5) + 1);
    }

    @JsonIgnore
    public void incrementChopExperience(Random random) {
        this.chopExperience = chopExperience + (random.nextInt(5) + 1);
    }

    public long getNewMoney() {
        return newMoney;
    }

    public void setNewMoney(long newMoney) {
        this.newMoney = newMoney;
    }

    public long getLastSeenCampaign() {
        return lastSeenCampaign;
    }

    public void setLastSeenCampaign(long lastSeenCampaign) {
        this.lastSeenCampaign = lastSeenCampaign;
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

    public void setInventorySortType(InventorySortType inventorySortType) {
        this.inventorySortType = inventorySortType;
    }

    public void setHiddenLegacy(boolean hiddenLegacy) {
        this.hiddenLegacy = hiddenLegacy;
    }

    public boolean isHiddenLegacy() {
        return hiddenLegacy;
    }

    public boolean isNewPlayerNotice() {
        return newPlayerNotice;
    }

    public void setNewPlayerNotice(boolean newPlayerNotice) {
        this.newPlayerNotice = newPlayerNotice;
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

    public void setPetChoice(PetChoice petChoice) {
        this.petChoice = petChoice;
    }
}
