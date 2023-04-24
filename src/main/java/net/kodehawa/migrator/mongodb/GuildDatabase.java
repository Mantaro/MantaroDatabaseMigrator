package net.kodehawa.migrator.mongodb;

import net.kodehawa.migrator.Migrator;
import net.kodehawa.migrator.helpers.CommandCategory;
import net.kodehawa.migrator.helpers.PollDatabaseObject;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("unused")
public class GuildDatabase implements ManagedMongoObject {
    @BsonIgnore
    public static final String DB_TABLE = "guilds";
    @BsonId
    private String id;
    @BsonIgnore
    public static GuildDatabase of(String guildId) {
        return new GuildDatabase(guildId);
    }

    // Constructors needed for the Mongo Codec to deserialize/serialize this.
    public GuildDatabase() {}
    public GuildDatabase(String id) {
        this.id = id;
    }

    public @NotNull String getId() {
        return id;
    }

    @BsonIgnore
    @Override
    public @NotNull String getTableName() {
        return DB_TABLE;
    }

    @Override
    @BsonIgnore
    public void save() {
        Migrator.saveMongo(this, GuildDatabase.class);
    }

    @Override
    @BsonIgnore
    public void delete() {
        Migrator.deleteMongo(this, GuildDatabase.class);
    }

    // ------------------------- DATA CLASS START ------------------------- //

    private long premiumUntil = 0L;
    private Map<String, String> autoroles = new HashMap<>();
    private String birthdayChannel = null;
    private String birthdayRole = null;
    private long cases = 0L;
    private Map<String, List<CommandCategory>> channelSpecificDisabledCategories = new HashMap<>();
    private Map<String, List<String>> channelSpecificDisabledCommands = new HashMap<>();
    private Set<CommandCategory> disabledCategories = new HashSet<>();
    private Set<String> disabledChannels = new HashSet<>();
    private Set<String> disabledCommands = new HashSet<>();
    private Set<String> disabledRoles = new HashSet<>();
    private List<String> disabledUsers = new ArrayList<>();
    private String guildAutoRole = null;
    private String guildCustomPrefix = null;
    private String guildLogChannel = null;
    private String joinMessage = null;
    private String leaveMessage = null;
    private Set<String> logExcludedChannels = new HashSet<>();
    private String logJoinLeaveChannel = null;
    private int maxFairQueue = 4;
    private Set<String> modlogBlacklistedPeople = new HashSet<>();
    private boolean musicAnnounce = true;
    private String musicChannel = null;
    private String mutedRole = null;
    private boolean noMentionsAction = false;
    private String premiumKey;
    private long ranPolls = 0L;
    private List<String> rolesBlockedFromCommands = new ArrayList<>();
    private long setModTimeout = 0L;
    private int timeDisplay = 0; //0 = 24h, 1 = 12h
    private String gameTimeoutExpectedAt;
    private boolean ignoreBotsWelcomeMessage = false;
    private boolean ignoreBotsAutoRole = false;
    private boolean enabledLevelUpMessages = false;
    private String levelUpMessage = null;
    private Set<String> blackListedImageTags = new HashSet<>();
    private String logJoinChannel = null;
    private String logLeaveChannel = null;
    private Set<String> linkProtectionAllowedUsers = new HashSet<>();
    private Map<String, List<CommandCategory>> roleSpecificDisabledCategories = new HashMap<>();
    private Map<String, List<String>> roleSpecificDisabledCommands = new HashMap<>();
    private String lang = "en_US";
    private boolean musicVote = true;
    private List<String> extraJoinMessages = new ArrayList<>();
    private List<String> extraLeaveMessages = new ArrayList<>();
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
    private List<String> birthdayBlockedIds = new ArrayList<>();
    private boolean gameMultipleDisabled = false;
    private String logTimezone;
    private List<String> allowedBirthdays = new ArrayList<>();
    private boolean notifiedFromBirthdayChange = false;
    private boolean disableExplicit = false;
    private String djRoleId;
    private Long musicQueueSizeLimit = null;
    private Map<String, PollDatabaseObject> runningPolls = new HashMap<>();
    private boolean hasReceivedGreet = false;

    public void setId(String id) {
        this.id = id;
    }

    public long getPremiumUntil() {
        return premiumUntil;
    }

    public void setPremiumUntil(long premiumUntil) {
        this.premiumUntil = premiumUntil;
    }

    public Map<String, String> getAutoroles() {
        return autoroles;
    }

    public void setAutoroles(Map<String, String> autoroles) {
        this.autoroles = autoroles;
    }

    public String getBirthdayChannel() {
        return birthdayChannel;
    }

    public void setBirthdayChannel(String birthdayChannel) {
        this.birthdayChannel = birthdayChannel;
    }

    public String getBirthdayRole() {
        return birthdayRole;
    }

    public void setBirthdayRole(String birthdayRole) {
        this.birthdayRole = birthdayRole;
    }

    public long getCases() {
        return cases;
    }

    public void setCases(long cases) {
        this.cases = cases;
    }

    public Map<String, List<CommandCategory>> getChannelSpecificDisabledCategories() {
        return channelSpecificDisabledCategories;
    }

    public void setChannelSpecificDisabledCategories(Map<String, List<CommandCategory>> channelSpecificDisabledCategories) {
        this.channelSpecificDisabledCategories = channelSpecificDisabledCategories;
    }

    public Map<String, List<String>> getChannelSpecificDisabledCommands() {
        return channelSpecificDisabledCommands;
    }

    public void setChannelSpecificDisabledCommands(Map<String, List<String>> channelSpecificDisabledCommands) {
        this.channelSpecificDisabledCommands = channelSpecificDisabledCommands;
    }

    public Set<CommandCategory> getDisabledCategories() {
        return disabledCategories;
    }

    public void setDisabledCategories(Set<CommandCategory> disabledCategories) {
        this.disabledCategories = disabledCategories;
    }

    public Set<String> getDisabledChannels() {
        return disabledChannels;
    }

    public void setDisabledChannels(Set<String> disabledChannels) {
        this.disabledChannels = disabledChannels;
    }

    public Set<String> getDisabledCommands() {
        return disabledCommands;
    }

    public void setDisabledCommands(Set<String> disabledCommands) {
        this.disabledCommands = disabledCommands;
    }

    public Set<String> getDisabledRoles() {
        return disabledRoles;
    }

    public void setDisabledRoles(Set<String> disabledRoles) {
        this.disabledRoles = disabledRoles;
    }

    public List<String> getDisabledUsers() {
        return disabledUsers;
    }

    public void setDisabledUsers(List<String> disabledUsers) {
        this.disabledUsers = disabledUsers;
    }

    public String getGuildAutoRole() {
        return guildAutoRole;
    }

    public void setGuildAutoRole(String guildAutoRole) {
        this.guildAutoRole = guildAutoRole;
    }

    public String getGuildCustomPrefix() {
        return guildCustomPrefix;
    }

    public void setGuildCustomPrefix(String guildCustomPrefix) {
        this.guildCustomPrefix = guildCustomPrefix;
    }

    public String getGuildLogChannel() {
        return guildLogChannel;
    }

    public void setGuildLogChannel(String guildLogChannel) {
        this.guildLogChannel = guildLogChannel;
    }

    public String getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public Set<String> getLogExcludedChannels() {
        return logExcludedChannels;
    }

    public void setLogExcludedChannels(Set<String> logExcludedChannels) {
        this.logExcludedChannels = logExcludedChannels;
    }

    public String getLogJoinLeaveChannel() {
        return logJoinLeaveChannel;
    }

    public void setLogJoinLeaveChannel(String logJoinLeaveChannel) {
        this.logJoinLeaveChannel = logJoinLeaveChannel;
    }

    public int getMaxFairQueue() {
        return maxFairQueue;
    }

    public void setMaxFairQueue(int maxFairQueue) {
        this.maxFairQueue = maxFairQueue;
    }

    public Set<String> getModlogBlacklistedPeople() {
        return modlogBlacklistedPeople;
    }

    public void setModlogBlacklistedPeople(Set<String> modlogBlacklistedPeople) {
        this.modlogBlacklistedPeople = modlogBlacklistedPeople;
    }

    public boolean isMusicAnnounce() {
        return musicAnnounce;
    }

    public void setMusicAnnounce(boolean musicAnnounce) {
        this.musicAnnounce = musicAnnounce;
    }

    public String getMusicChannel() {
        return musicChannel;
    }

    public void setMusicChannel(String musicChannel) {
        this.musicChannel = musicChannel;
    }

    public String getMutedRole() {
        return mutedRole;
    }

    public void setMutedRole(String mutedRole) {
        this.mutedRole = mutedRole;
    }

    public boolean isNoMentionsAction() {
        return noMentionsAction;
    }

    public void setNoMentionsAction(boolean noMentionsAction) {
        this.noMentionsAction = noMentionsAction;
    }

    public String getPremiumKey() {
        return premiumKey;
    }

    public void setPremiumKey(String premiumKey) {
        this.premiumKey = premiumKey;
    }

    public long getRanPolls() {
        return ranPolls;
    }

    public void setRanPolls(long ranPolls) {
        this.ranPolls = ranPolls;
    }

    public List<String> getRolesBlockedFromCommands() {
        return rolesBlockedFromCommands;
    }

    public void setRolesBlockedFromCommands(List<String> rolesBlockedFromCommands) {
        this.rolesBlockedFromCommands = rolesBlockedFromCommands;
    }

    public long getSetModTimeout() {
        return setModTimeout;
    }

    public void setSetModTimeout(long setModTimeout) {
        this.setModTimeout = setModTimeout;
    }

    public int getTimeDisplay() {
        return timeDisplay;
    }

    public void setTimeDisplay(int timeDisplay) {
        this.timeDisplay = timeDisplay;
    }

    public String getGameTimeoutExpectedAt() {
        return gameTimeoutExpectedAt;
    }

    public void setGameTimeoutExpectedAt(String gameTimeoutExpectedAt) {
        this.gameTimeoutExpectedAt = gameTimeoutExpectedAt;
    }

    public boolean isIgnoreBotsWelcomeMessage() {
        return ignoreBotsWelcomeMessage;
    }

    public void setIgnoreBotsWelcomeMessage(boolean ignoreBotsWelcomeMessage) {
        this.ignoreBotsWelcomeMessage = ignoreBotsWelcomeMessage;
    }

    public boolean isIgnoreBotsAutoRole() {
        return ignoreBotsAutoRole;
    }

    public void setIgnoreBotsAutoRole(boolean ignoreBotsAutoRole) {
        this.ignoreBotsAutoRole = ignoreBotsAutoRole;
    }

    public boolean isEnabledLevelUpMessages() {
        return enabledLevelUpMessages;
    }

    public void setEnabledLevelUpMessages(boolean enabledLevelUpMessages) {
        this.enabledLevelUpMessages = enabledLevelUpMessages;
    }

    public String getLevelUpMessage() {
        return levelUpMessage;
    }

    public void setLevelUpMessage(String levelUpMessage) {
        this.levelUpMessage = levelUpMessage;
    }

    public Set<String> getBlackListedImageTags() {
        return blackListedImageTags;
    }

    public void setBlackListedImageTags(Set<String> blackListedImageTags) {
        this.blackListedImageTags = blackListedImageTags;
    }

    public String getLogJoinChannel() {
        return logJoinChannel;
    }

    public void setLogJoinChannel(String logJoinChannel) {
        this.logJoinChannel = logJoinChannel;
    }

    public String getLogLeaveChannel() {
        return logLeaveChannel;
    }

    public void setLogLeaveChannel(String logLeaveChannel) {
        this.logLeaveChannel = logLeaveChannel;
    }

    public Set<String> getLinkProtectionAllowedUsers() {
        return linkProtectionAllowedUsers;
    }

    public void setLinkProtectionAllowedUsers(Set<String> linkProtectionAllowedUsers) {
        this.linkProtectionAllowedUsers = linkProtectionAllowedUsers;
    }

    public Map<String, List<CommandCategory>> getRoleSpecificDisabledCategories() {
        return roleSpecificDisabledCategories;
    }

    public void setRoleSpecificDisabledCategories(Map<String, List<CommandCategory>> roleSpecificDisabledCategories) {
        this.roleSpecificDisabledCategories = roleSpecificDisabledCategories;
    }

    public Map<String, List<String>> getRoleSpecificDisabledCommands() {
        return roleSpecificDisabledCommands;
    }

    public void setRoleSpecificDisabledCommands(Map<String, List<String>> roleSpecificDisabledCommands) {
        this.roleSpecificDisabledCommands = roleSpecificDisabledCommands;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isMusicVote() {
        return musicVote;
    }

    public void setMusicVote(boolean musicVote) {
        this.musicVote = musicVote;
    }

    public List<String> getExtraJoinMessages() {
        return extraJoinMessages;
    }

    public void setExtraJoinMessages(List<String> extraJoinMessages) {
        this.extraJoinMessages = extraJoinMessages;
    }

    public List<String> getExtraLeaveMessages() {
        return extraLeaveMessages;
    }

    public void setExtraLeaveMessages(List<String> extraLeaveMessages) {
        this.extraLeaveMessages = extraLeaveMessages;
    }

    public String getBirthdayMessage() {
        return birthdayMessage;
    }

    public void setBirthdayMessage(String birthdayMessage) {
        this.birthdayMessage = birthdayMessage;
    }

    public boolean isCustomAdminLockNew() {
        return customAdminLockNew;
    }

    public void setCustomAdminLockNew(boolean customAdminLockNew) {
        this.customAdminLockNew = customAdminLockNew;
    }

    public String getMpLinkedTo() {
        return mpLinkedTo;
    }

    public void setMpLinkedTo(String mpLinkedTo) {
        this.mpLinkedTo = mpLinkedTo;
    }

    public List<String> getModLogBlacklistWords() {
        return modLogBlacklistWords;
    }

    public void setModLogBlacklistWords(List<String> modLogBlacklistWords) {
        this.modLogBlacklistWords = modLogBlacklistWords;
    }

    public Map<String, List<String>> getAutoroleCategories() {
        return autoroleCategories;
    }

    public void setAutoroleCategories(Map<String, List<String>> autoroleCategories) {
        this.autoroleCategories = autoroleCategories;
    }

    public String getEditMessageLog() {
        return editMessageLog;
    }

    public void setEditMessageLog(String editMessageLog) {
        this.editMessageLog = editMessageLog;
    }

    public String getDeleteMessageLog() {
        return deleteMessageLog;
    }

    public void setDeleteMessageLog(String deleteMessageLog) {
        this.deleteMessageLog = deleteMessageLog;
    }

    public String getBannedMemberLog() {
        return bannedMemberLog;
    }

    public void setBannedMemberLog(String bannedMemberLog) {
        this.bannedMemberLog = bannedMemberLog;
    }

    public String getUnbannedMemberLog() {
        return unbannedMemberLog;
    }

    public void setUnbannedMemberLog(String unbannedMemberLog) {
        this.unbannedMemberLog = unbannedMemberLog;
    }

    public String getKickedMemberLog() {
        return kickedMemberLog;
    }

    public void setKickedMemberLog(String kickedMemberLog) {
        this.kickedMemberLog = kickedMemberLog;
    }

    public boolean isCommandWarningDisplay() {
        return commandWarningDisplay;
    }

    public void setCommandWarningDisplay(boolean commandWarningDisplay) {
        this.commandWarningDisplay = commandWarningDisplay;
    }

    public boolean isHasReceivedGreet() {
        return hasReceivedGreet;
    }

    public void setHasReceivedGreet(boolean hasReceivedGreet) {
        this.hasReceivedGreet = hasReceivedGreet;
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

    public String getLogTimezone() {
        return logTimezone;
    }

    public void setLogTimezone(String logTimezone) {
        this.logTimezone = logTimezone;
    }

    public List<String> getAllowedBirthdays() {
        return allowedBirthdays;
    }

    public void setAllowedBirthdays(List<String> allowedBirthdays) {
        this.allowedBirthdays = allowedBirthdays;
    }

    public boolean isNotifiedFromBirthdayChange() {
        return notifiedFromBirthdayChange;
    }

    public void setNotifiedFromBirthdayChange(boolean notifiedFromBirthdayChange) {
        this.notifiedFromBirthdayChange = notifiedFromBirthdayChange;
    }

    public String getDjRoleId() {
        return djRoleId;
    }

    public void setDjRoleId(String djRoleId) {
        this.djRoleId = djRoleId;
    }

    public boolean isDisableExplicit() {
        return disableExplicit;
    }

    public void setDisableExplicit(boolean disableExplicit) {
        this.disableExplicit = disableExplicit;
    }

    public Long getMusicQueueSizeLimit() {
        return musicQueueSizeLimit;
    }

    public void setMusicQueueSizeLimit(Long musicQueueSizeLimit) {
        this.musicQueueSizeLimit = musicQueueSizeLimit;
    }

    public void setRunningPolls(Map<String, PollDatabaseObject> polls) {
        this.runningPolls = polls;
    }

    public Map<String, PollDatabaseObject> getRunningPolls() {
        return runningPolls;
    }

    public boolean hasReceivedGreet() {
        return hasReceivedGreet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuildDatabase that = (GuildDatabase) o;
        return premiumUntil == that.premiumUntil && cases == that.cases && maxFairQueue == that.maxFairQueue && musicAnnounce == that.musicAnnounce && noMentionsAction == that.noMentionsAction && ranPolls == that.ranPolls && setModTimeout == that.setModTimeout && timeDisplay == that.timeDisplay && ignoreBotsWelcomeMessage == that.ignoreBotsWelcomeMessage && ignoreBotsAutoRole == that.ignoreBotsAutoRole && enabledLevelUpMessages == that.enabledLevelUpMessages && musicVote == that.musicVote && customAdminLockNew == that.customAdminLockNew && commandWarningDisplay == that.commandWarningDisplay && gameMultipleDisabled == that.gameMultipleDisabled && notifiedFromBirthdayChange == that.notifiedFromBirthdayChange && disableExplicit == that.disableExplicit && hasReceivedGreet == that.hasReceivedGreet && Objects.equals(id, that.id) && Objects.equals(autoroles, that.autoroles) && Objects.equals(birthdayChannel, that.birthdayChannel) && Objects.equals(birthdayRole, that.birthdayRole) && Objects.equals(channelSpecificDisabledCategories, that.channelSpecificDisabledCategories) && Objects.equals(channelSpecificDisabledCommands, that.channelSpecificDisabledCommands) && Objects.equals(disabledCategories, that.disabledCategories) && Objects.equals(disabledChannels, that.disabledChannels) && Objects.equals(disabledCommands, that.disabledCommands) && Objects.equals(disabledRoles, that.disabledRoles) && Objects.equals(disabledUsers, that.disabledUsers) && Objects.equals(guildAutoRole, that.guildAutoRole) && Objects.equals(guildCustomPrefix, that.guildCustomPrefix) && Objects.equals(guildLogChannel, that.guildLogChannel) && Objects.equals(joinMessage, that.joinMessage) && Objects.equals(leaveMessage, that.leaveMessage) && Objects.equals(logExcludedChannels, that.logExcludedChannels) && Objects.equals(logJoinLeaveChannel, that.logJoinLeaveChannel) && Objects.equals(modlogBlacklistedPeople, that.modlogBlacklistedPeople) && Objects.equals(musicChannel, that.musicChannel) && Objects.equals(mutedRole, that.mutedRole) && Objects.equals(premiumKey, that.premiumKey) && Objects.equals(rolesBlockedFromCommands, that.rolesBlockedFromCommands) && Objects.equals(gameTimeoutExpectedAt, that.gameTimeoutExpectedAt) && Objects.equals(levelUpMessage, that.levelUpMessage) && Objects.equals(blackListedImageTags, that.blackListedImageTags) && Objects.equals(logJoinChannel, that.logJoinChannel) && Objects.equals(logLeaveChannel, that.logLeaveChannel) && Objects.equals(linkProtectionAllowedUsers, that.linkProtectionAllowedUsers) && Objects.equals(roleSpecificDisabledCategories, that.roleSpecificDisabledCategories) && Objects.equals(roleSpecificDisabledCommands, that.roleSpecificDisabledCommands) && Objects.equals(lang, that.lang) && Objects.equals(extraJoinMessages, that.extraJoinMessages) && Objects.equals(extraLeaveMessages, that.extraLeaveMessages) && Objects.equals(birthdayMessage, that.birthdayMessage) && Objects.equals(mpLinkedTo, that.mpLinkedTo) && Objects.equals(modLogBlacklistWords, that.modLogBlacklistWords) && Objects.equals(autoroleCategories, that.autoroleCategories) && Objects.equals(editMessageLog, that.editMessageLog) && Objects.equals(deleteMessageLog, that.deleteMessageLog) && Objects.equals(bannedMemberLog, that.bannedMemberLog) && Objects.equals(unbannedMemberLog, that.unbannedMemberLog) && Objects.equals(kickedMemberLog, that.kickedMemberLog) && Objects.equals(birthdayBlockedIds, that.birthdayBlockedIds) && Objects.equals(logTimezone, that.logTimezone) && Objects.equals(allowedBirthdays, that.allowedBirthdays) && Objects.equals(djRoleId, that.djRoleId) && Objects.equals(musicQueueSizeLimit, that.musicQueueSizeLimit) && Objects.equals(runningPolls, that.runningPolls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, premiumUntil, autoroles, birthdayChannel, birthdayRole, cases, channelSpecificDisabledCategories, channelSpecificDisabledCommands, disabledCategories, disabledChannels, disabledCommands, disabledRoles, disabledUsers, guildAutoRole, guildCustomPrefix, guildLogChannel, joinMessage, leaveMessage, logExcludedChannels, logJoinLeaveChannel, maxFairQueue, modlogBlacklistedPeople, musicAnnounce, musicChannel, mutedRole, noMentionsAction, premiumKey, ranPolls, rolesBlockedFromCommands, setModTimeout, timeDisplay, gameTimeoutExpectedAt, ignoreBotsWelcomeMessage, ignoreBotsAutoRole, enabledLevelUpMessages, levelUpMessage, blackListedImageTags, logJoinChannel, logLeaveChannel, linkProtectionAllowedUsers, roleSpecificDisabledCategories, roleSpecificDisabledCommands, lang, musicVote, extraJoinMessages, extraLeaveMessages, birthdayMessage, customAdminLockNew, mpLinkedTo, modLogBlacklistWords, autoroleCategories, editMessageLog, deleteMessageLog, bannedMemberLog, unbannedMemberLog, kickedMemberLog, commandWarningDisplay, birthdayBlockedIds, gameMultipleDisabled, logTimezone, allowedBirthdays, notifiedFromBirthdayChange, disableExplicit, djRoleId, musicQueueSizeLimit, runningPolls, hasReceivedGreet);
    }
}
