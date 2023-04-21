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

import com.fasterxml.jackson.annotation.JsonProperty;
import net.kodehawa.migrator.helpers.CommandCategory;
import net.kodehawa.migrator.helpers.PollDatabaseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GuildData {
    private boolean antiSpam = false;
    private HashMap<String, String> autoroles = new HashMap<>();
    private String birthdayChannel = null;
    private String birthdayRole = null;
    private long cases = 0L;
    private HashMap<String, List<CommandCategory>> channelSpecificDisabledCategories = new HashMap<>();
    private HashMap<String, List<String>> channelSpecificDisabledCommands = new HashMap<>();
    private boolean customAdminLock = false;
    private Set<CommandCategory> disabledCategories = new HashSet<>();
    private Set<String> disabledChannels = new HashSet<>();
    private Set<String> disabledCommands = new HashSet<>();
    private Set<String> disabledRoles = new HashSet<>();
    private List<String> disabledUsers = new ArrayList<>();
    private String guildAutoRole = null;
    private String guildCustomPrefix = null;
    private String guildLogChannel = null;
    private Set<String> guildUnsafeChannels = new HashSet<>();
    private String joinMessage = null;
    private String leaveMessage = null;
    private boolean linkProtection = false;
    private Set<String> linkProtectionAllowedChannels = new HashSet<>();
    private Set<String> logExcludedChannels = new HashSet<>();
    private String logJoinLeaveChannel = null;
    private int maxFairQueue = 4;
    private int maxResultsSearch = 5;
    private Set<String> modlogBlacklistedPeople = new HashSet<>();
    private boolean musicAnnounce = true;
    private String musicChannel = null;
    private Long musicQueueSizeLimit = null;
    private Long musicSongDurationLimit = null;
    private String mutedRole = null;
    private ConcurrentHashMap<Long, Long> mutedTimelyUsers = new ConcurrentHashMap<>();
    private boolean noMentionsAction = false;
    private String premiumKey;
    private long quoteLastId = 0L;
    private long ranPolls = 0L;
    private boolean reactionMenus = true;
    private ArrayList<String> rolesBlockedFromCommands = new ArrayList<>();
    private boolean rpgDevaluation = true;
    private boolean rpgLocalMode = false;
    private long setModTimeout = 0L;
    private boolean slowMode = false;
    private Set<String> slowModeChannels = new HashSet<>();
    private Set<String> spamModeChannels = new HashSet<>();
    private int timeDisplay = 0; //0 = 24h, 1 = 12h
    private String gameTimeoutExpectedAt;
    private boolean ignoreBotsWelcomeMessage = false;
    private boolean ignoreBotsAutoRole = false;
    private boolean enabledLevelUpMessages = false;
    private String levelUpChannel = null;
    private String levelUpMessage = null;
    private Set<String> blackListedImageTags = new HashSet<>();
    private String logJoinChannel = null;
    private String logLeaveChannel = null;
    private List<LocalExperienceData> localPlayerExperience = new ArrayList<>();
    private Set<String> linkProtectionAllowedUsers = new HashSet<>();
    private HashMap<String, List<CommandCategory>> roleSpecificDisabledCategories = new HashMap<>();
    private HashMap<String, List<String>> roleSpecificDisabledCommands = new HashMap<>();
    private String lang = "en_US";
    private boolean musicVote = true;
    private List<String> extraJoinMessages = new ArrayList<>();
    private List<String> extraLeaveMessages = new ArrayList<>();
    private String whitelistedRole = null;
    private String birthdayMessage = null;
    private boolean customAdminLockNew = true;
    private String mpLinkedTo = null; //user id of the person who linked MP to a specific server (used for patreon checks)
    private List<String> modLogBlacklistWords = new ArrayList<>();
    private Map<String, List<String>> autoroleCategories = new HashMap<>();
    private String editMessageLog;
    private String deleteMessageLog;
    private String bannedMemberLog;
    private String unbannedMemberLog;
    private String kickedMemberLog;
    private boolean commandWarningDisplay = false;
    @JsonProperty("hasReceivedGreet")
    private boolean hasReceivedGreet = false;
    private List<String> birthdayBlockedIds = new ArrayList<>();
    @JsonProperty("gameMultipleDisabled")
    private boolean gameMultipleDisabled = false;
    private String logTimezone;
    private List<String> allowedBirthdays = new ArrayList<>();
    private boolean notifiedFromBirthdayChange = false;
    private String djRoleId;
    private boolean disableExplicit = false;
    private Map<String, PollDatabaseObject> runningPolls = new HashMap<>();

    public GuildData() { }

    public boolean isHasReceivedGreet() {
        return hasReceivedGreet;
    }

    public boolean isAntiSpam() {
        return this.antiSpam;
    }

    public void setAntiSpam(boolean antiSpam) {
        this.antiSpam = antiSpam;
    }

    public HashMap<String, String> getAutoroles() {
        return this.autoroles;
    }

    public void setAutoroles(HashMap<String, String> autoroles) {
        this.autoroles = autoroles;
    }

    public String getBirthdayChannel() {
        return this.birthdayChannel;
    }

    public void setBirthdayChannel(String birthdayChannel) {
        this.birthdayChannel = birthdayChannel;
    }

    public String getBirthdayRole() {
        return this.birthdayRole;
    }

    public void setBirthdayRole(String birthdayRole) {
        this.birthdayRole = birthdayRole;
    }

    public long getCases() {
        return this.cases;
    }

    public void setCases(long cases) {
        this.cases = cases;
    }

    public boolean hasReceivedGreet() {
        return hasReceivedGreet;
    }

    public void setHasReceivedGreet(boolean hasReceivedGreet) {
        this.hasReceivedGreet = hasReceivedGreet;
    }

    public HashMap<String, List<CommandCategory>> getChannelSpecificDisabledCategories() {
        return this.channelSpecificDisabledCategories;
    }

    public void setChannelSpecificDisabledCategories(HashMap<String, List<CommandCategory>> channelSpecificDisabledCategories) {
        this.channelSpecificDisabledCategories = channelSpecificDisabledCategories;
    }

    public HashMap<String, List<String>> getChannelSpecificDisabledCommands() {
        return this.channelSpecificDisabledCommands;
    }

    public void setChannelSpecificDisabledCommands(HashMap<String, List<String>> channelSpecificDisabledCommands) {
        this.channelSpecificDisabledCommands = channelSpecificDisabledCommands;
    }

    public boolean isCustomAdminLock() {
        return this.customAdminLock;
    }

    public void setCustomAdminLock(boolean customAdminLock) {
        this.customAdminLock = customAdminLock;
    }

    public Set<CommandCategory> getDisabledCategories() {
        return this.disabledCategories;
    }

    public void setDisabledCategories(Set<CommandCategory> disabledCategories) {
        this.disabledCategories = disabledCategories;
    }

    public Set<String> getDisabledChannels() {
        return this.disabledChannels;
    }

    public void setDisabledChannels(Set<String> disabledChannels) {
        this.disabledChannels = disabledChannels;
    }

    public Set<String> getDisabledCommands() {
        return this.disabledCommands;
    }

    public void setDisabledCommands(Set<String> disabledCommands) {
        this.disabledCommands = disabledCommands;
    }

    public Set<String> getDisabledRoles() {
        return this.disabledRoles;
    }

    public void setDisabledRoles(Set<String> disabledRoles) {
        this.disabledRoles = disabledRoles;
    }

    public List<String> getDisabledUsers() {
        return this.disabledUsers;
    }

    public void setDisabledUsers(List<String> disabledUsers) {
        this.disabledUsers = disabledUsers;
    }

    public String getGuildAutoRole() {
        return this.guildAutoRole;
    }

    public void setGuildAutoRole(String guildAutoRole) {
        this.guildAutoRole = guildAutoRole;
    }

    public String getGuildCustomPrefix() {
        return this.guildCustomPrefix;
    }

    public void setGuildCustomPrefix(String guildCustomPrefix) {
        this.guildCustomPrefix = guildCustomPrefix;
    }

    public String getGuildLogChannel() {
        return this.guildLogChannel;
    }

    public void setGuildLogChannel(String guildLogChannel) {
        this.guildLogChannel = guildLogChannel;
    }

    public Set<String> getGuildUnsafeChannels() {
        return this.guildUnsafeChannels;
    }

    public void setGuildUnsafeChannels(Set<String> guildUnsafeChannels) {
        this.guildUnsafeChannels = guildUnsafeChannels;
    }

    public String getJoinMessage() {
        return this.joinMessage;
    }

    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    public String getLeaveMessage() {
        return this.leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public boolean isLinkProtection() {
        return this.linkProtection;
    }

    public void setLinkProtection(boolean linkProtection) {
        this.linkProtection = linkProtection;
    }

    public Set<String> getLinkProtectionAllowedChannels() {
        return this.linkProtectionAllowedChannels;
    }

    public void setLinkProtectionAllowedChannels(Set<String> linkProtectionAllowedChannels) {
        this.linkProtectionAllowedChannels = linkProtectionAllowedChannels;
    }

    public Set<String> getLogExcludedChannels() {
        return this.logExcludedChannels;
    }

    public void setLogExcludedChannels(Set<String> logExcludedChannels) {
        this.logExcludedChannels = logExcludedChannels;
    }

    public String getLogJoinLeaveChannel() {
        return this.logJoinLeaveChannel;
    }

    public void setLogJoinLeaveChannel(String logJoinLeaveChannel) {
        this.logJoinLeaveChannel = logJoinLeaveChannel;
    }

    public int getMaxFairQueue() {
        return this.maxFairQueue;
    }

    public void setMaxFairQueue(int maxFairQueue) {
        this.maxFairQueue = maxFairQueue;
    }

    public int getMaxResultsSearch() {
        return this.maxResultsSearch;
    }

    public void setMaxResultsSearch(int maxResultsSearch) {
        this.maxResultsSearch = maxResultsSearch;
    }

    public Set<String> getModlogBlacklistedPeople() {
        return this.modlogBlacklistedPeople;
    }

    public void setModlogBlacklistedPeople(Set<String> modlogBlacklistedPeople) {
        this.modlogBlacklistedPeople = modlogBlacklistedPeople;
    }

    public boolean isMusicAnnounce() {
        return this.musicAnnounce;
    }

    public void setMusicAnnounce(boolean musicAnnounce) {
        this.musicAnnounce = musicAnnounce;
    }

    public String getMusicChannel() {
        return this.musicChannel;
    }

    public void setMusicChannel(String musicChannel) {
        this.musicChannel = musicChannel;
    }

    public Long getMusicQueueSizeLimit() {
        return this.musicQueueSizeLimit;
    }

    public void setMusicQueueSizeLimit(Long musicQueueSizeLimit) {
        this.musicQueueSizeLimit = musicQueueSizeLimit;
    }

    public Long getMusicSongDurationLimit() {
        return this.musicSongDurationLimit;
    }

    public void setMusicSongDurationLimit(Long musicSongDurationLimit) {
        this.musicSongDurationLimit = musicSongDurationLimit;
    }

    public String getMutedRole() {
        return this.mutedRole;
    }

    public ConcurrentHashMap<Long, Long> getMutedTimelyUsers() {
        return this.mutedTimelyUsers;
    }

    public void setMutedTimelyUsers(ConcurrentHashMap<Long, Long> mutedTimelyUsers) {
        this.mutedTimelyUsers = mutedTimelyUsers;
    }

    public boolean isNoMentionsAction() {
        return this.noMentionsAction;
    }

    public void setNoMentionsAction(boolean noMentionsAction) {
        this.noMentionsAction = noMentionsAction;
    }

    public String getPremiumKey() {
        return this.premiumKey;
    }

    public void setPremiumKey(String premiumKey) {
        this.premiumKey = premiumKey;
    }

    public long getQuoteLastId() {
        return this.quoteLastId;
    }

    public void setQuoteLastId(long quoteLastId) {
        this.quoteLastId = quoteLastId;
    }

    public long getRanPolls() {
        return this.ranPolls;
    }

    public void setRanPolls(long ranPolls) {
        this.ranPolls = ranPolls;
    }

    public boolean isReactionMenus() {
        return this.reactionMenus;
    }

    public void setReactionMenus(boolean reactionMenus) {
        this.reactionMenus = reactionMenus;
    }

    public ArrayList<String> getRolesBlockedFromCommands() {
        return this.rolesBlockedFromCommands;
    }

    public void setRolesBlockedFromCommands(ArrayList<String> rolesBlockedFromCommands) {
        this.rolesBlockedFromCommands = rolesBlockedFromCommands;
    }

    public boolean isRpgDevaluation() {
        return this.rpgDevaluation;
    }

    public void setRpgDevaluation(boolean rpgDevaluation) {
        this.rpgDevaluation = rpgDevaluation;
    }

    public boolean isRpgLocalMode() {
        return this.rpgLocalMode;
    }

    public void setRpgLocalMode(boolean rpgLocalMode) {
        this.rpgLocalMode = rpgLocalMode;
    }

    public long getSetModTimeout() {
        return this.setModTimeout;
    }

    public void setSetModTimeout(long setModTimeout) {
        this.setModTimeout = setModTimeout;
    }

    public boolean isSlowMode() {
        return this.slowMode;
    }

    public void setSlowMode(boolean slowMode) {
        this.slowMode = slowMode;
    }

    public Set<String> getSlowModeChannels() {
        return this.slowModeChannels;
    }

    public void setSlowModeChannels(Set<String> slowModeChannels) {
        this.slowModeChannels = slowModeChannels;
    }

    public Set<String> getSpamModeChannels() {
        return this.spamModeChannels;
    }

    public void setSpamModeChannels(Set<String> spamModeChannels) {
        this.spamModeChannels = spamModeChannels;
    }

    public int getTimeDisplay() {
        return this.timeDisplay;
    }

    public void setTimeDisplay(int timeDisplay) {
        this.timeDisplay = timeDisplay;
    }

    public String getGameTimeoutExpectedAt() {
        return this.gameTimeoutExpectedAt;
    }

    public void setGameTimeoutExpectedAt(String gameTimeoutExpectedAt) {
        this.gameTimeoutExpectedAt = gameTimeoutExpectedAt;
    }

    public boolean isIgnoreBotsWelcomeMessage() {
        return this.ignoreBotsWelcomeMessage;
    }

    public void setIgnoreBotsWelcomeMessage(boolean ignoreBotsWelcomeMessage) {
        this.ignoreBotsWelcomeMessage = ignoreBotsWelcomeMessage;
    }

    public boolean isIgnoreBotsAutoRole() {
        return this.ignoreBotsAutoRole;
    }

    public void setIgnoreBotsAutoRole(boolean ignoreBotsAutoRole) {
        this.ignoreBotsAutoRole = ignoreBotsAutoRole;
    }

    public boolean isEnabledLevelUpMessages() {
        return this.enabledLevelUpMessages;
    }

    public void setEnabledLevelUpMessages(boolean enabledLevelUpMessages) {
        this.enabledLevelUpMessages = enabledLevelUpMessages;
    }

    public String getLevelUpChannel() {
        return this.levelUpChannel;
    }

    public void setLevelUpChannel(String levelUpChannel) {
        this.levelUpChannel = levelUpChannel;
    }

    public String getLevelUpMessage() {
        return this.levelUpMessage;
    }

    public void setLevelUpMessage(String levelUpMessage) {
        this.levelUpMessage = levelUpMessage;
    }

    public Set<String> getBlackListedImageTags() {
        return this.blackListedImageTags;
    }

    public void setBlackListedImageTags(Set<String> blackListedImageTags) {
        this.blackListedImageTags = blackListedImageTags;
    }

    public String getLogJoinChannel() {
        return this.logJoinChannel;
    }

    public void setLogJoinChannel(String logJoinChannel) {
        this.logJoinChannel = logJoinChannel;
    }

    public String getLogLeaveChannel() {
        return this.logLeaveChannel;
    }

    public void setLogLeaveChannel(String logLeaveChannel) {
        this.logLeaveChannel = logLeaveChannel;
    }

    public List<LocalExperienceData> getLocalPlayerExperience() {
        return this.localPlayerExperience;
    }

    public void setLocalPlayerExperience(List<LocalExperienceData> localPlayerExperience) {
        this.localPlayerExperience = localPlayerExperience;
    }

    public Set<String> getLinkProtectionAllowedUsers() {
        return this.linkProtectionAllowedUsers;
    }

    public void setLinkProtectionAllowedUsers(Set<String> linkProtectionAllowedUsers) {
        this.linkProtectionAllowedUsers = linkProtectionAllowedUsers;
    }

    public HashMap<String, List<CommandCategory>> getRoleSpecificDisabledCategories() {
        return this.roleSpecificDisabledCategories;
    }

    public void setRoleSpecificDisabledCategories(HashMap<String, List<CommandCategory>> roleSpecificDisabledCategories) {
        this.roleSpecificDisabledCategories = roleSpecificDisabledCategories;
    }

    public HashMap<String, List<String>> getRoleSpecificDisabledCommands() {
        return this.roleSpecificDisabledCommands;
    }

    public void setRoleSpecificDisabledCommands(HashMap<String, List<String>> roleSpecificDisabledCommands) {
        this.roleSpecificDisabledCommands = roleSpecificDisabledCommands;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isMusicVote() {
        return this.musicVote;
    }

    public void setMusicVote(boolean musicVote) {
        this.musicVote = musicVote;
    }

    public List<String> getExtraJoinMessages() {
        return this.extraJoinMessages;
    }

    public void setExtraJoinMessages(List<String> extraJoinMessages) {
        this.extraJoinMessages = extraJoinMessages;
    }

    public List<String> getExtraLeaveMessages() {
        return this.extraLeaveMessages;
    }

    public void setExtraLeaveMessages(List<String> extraLeaveMessages) {
        this.extraLeaveMessages = extraLeaveMessages;
    }

    public String getWhitelistedRole() {
        return this.whitelistedRole;
    }

    public void setWhitelistedRole(String whitelistedRole) {
        this.whitelistedRole = whitelistedRole;
    }

    public String getBirthdayMessage() {
        return this.birthdayMessage;
    }

    public void setBirthdayMessage(String birthdayMessage) {
        this.birthdayMessage = birthdayMessage;
    }

    public boolean isCustomAdminLockNew() {
        return this.customAdminLockNew;
    }

    public void setCustomAdminLockNew(boolean customAdminLockNew) {
        this.customAdminLockNew = customAdminLockNew;
    }

    public String getMpLinkedTo() {
        return this.mpLinkedTo;
    }

    public void setMpLinkedTo(String mpLinkedTo) {
        this.mpLinkedTo = mpLinkedTo;
    }

    public List<String> getModLogBlacklistWords() {
        return this.modLogBlacklistWords;
    }

    public void setModLogBlacklistWords(List<String> modLogBlacklistWords) {
        this.modLogBlacklistWords = modLogBlacklistWords;
    }

    public Map<String, List<String>> getAutoroleCategories() {
        return this.autoroleCategories;
    }

    public void setAutoroleCategories(Map<String, List<String>> autoroleCategories) {
        this.autoroleCategories = autoroleCategories;
    }

    public String getEditMessageLog() {
        return this.editMessageLog;
    }

    public void setEditMessageLog(String editMessageLog) {
        this.editMessageLog = editMessageLog;
    }

    public String getDeleteMessageLog() {
        return this.deleteMessageLog;
    }

    public void setDeleteMessageLog(String deleteMessageLog) {
        this.deleteMessageLog = deleteMessageLog;
    }

    public String getBannedMemberLog() {
        return this.bannedMemberLog;
    }

    public void setBannedMemberLog(String bannedMemberLog) {
        this.bannedMemberLog = bannedMemberLog;
    }

    public String getUnbannedMemberLog() {
        return this.unbannedMemberLog;
    }

    public void setUnbannedMemberLog(String unbannedMemberLog) {
        this.unbannedMemberLog = unbannedMemberLog;
    }

    public String getKickedMemberLog() {
        return this.kickedMemberLog;
    }

    public void setKickedMemberLog(String kickedMemberLog) {
        this.kickedMemberLog = kickedMemberLog;
    }

    public boolean isCommandWarningDisplay() {
        return this.commandWarningDisplay;
    }

    public void setCommandWarningDisplay(boolean commandWarningDisplay) {
        this.commandWarningDisplay = commandWarningDisplay;
    }

    public List<String> getBirthdayBlockedIds() {
        return birthdayBlockedIds;
    }

    public void setBirthdayBlockedIds(List<String> birthdayBlockedIds) {
        this.birthdayBlockedIds = birthdayBlockedIds;
    }

    public boolean isGameMultipleDisabled() {
        return gameMultipleDisabled;
    }

    public void setGameMultipleDisabled(boolean gameMultipleDisabled) {
        this.gameMultipleDisabled = gameMultipleDisabled;
    }

    public boolean isNotifiedFromBirthdayChange() {
        return notifiedFromBirthdayChange;
    }

    public void setNotifiedFromBirthdayChange(boolean notifiedFromBirthdayChange) {
        this.notifiedFromBirthdayChange = notifiedFromBirthdayChange;
    }

    public void setAllowedBirthdays(List<String> allowedBirthdays) {
        this.allowedBirthdays = allowedBirthdays;
    }

    public List<String> getAllowedBirthdays() {
        return allowedBirthdays;
    }

    public String getDjRoleId() {
        return djRoleId;
    }

    public void setDjRoleId(String djRoleId) {
        this.djRoleId = djRoleId;
    }

    public String getLogTimezone() {
        return logTimezone;
    }

    public void setLogTimezone(String logTimezone) {
        this.logTimezone = logTimezone;
    }

    public boolean isDisableExplicit() {
        return disableExplicit;
    }

    public void setDisableExplicit(boolean disableExplicit) {
        this.disableExplicit = disableExplicit;
    }

    public Map<String, PollDatabaseObject> getRunningPolls() {
        return runningPolls;
    }
}
