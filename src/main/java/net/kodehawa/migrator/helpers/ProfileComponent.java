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

package net.kodehawa.mantarobot.commands.currency.profile;

import net.dv8tion.jda.api.entities.User;
import net.kodehawa.mantarobot.MantaroBot;
import net.kodehawa.mantarobot.commands.currency.item.Item;
import net.kodehawa.mantarobot.commands.currency.item.ItemStack;
import net.kodehawa.mantarobot.commands.currency.pets.HousePet;
import net.kodehawa.mantarobot.commands.currency.pets.PetChoice;
import net.kodehawa.mantarobot.core.modules.commands.i18n.I18nContext;
import net.kodehawa.mantarobot.db.entities.Marriage;
import net.kodehawa.mantarobot.db.entities.Player;
import net.kodehawa.mantarobot.db.entities.UserDatabase;
import net.kodehawa.mantarobot.utils.Utils;
import net.kodehawa.mantarobot.utils.commands.EmoteReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ProfileComponent {
    HEADER(null,
            i18nContext -> String.format(i18nContext.get("commands.profile.badge_header"), EmoteReference.TROPHY), (holder, i18nContext) -> {
        var playerData = holder.getPlayer();
        if (holder.getBadges().isEmpty() || !playerData.isShowBadge()) {
            return " \u2009\u2009None";
        }

        if (playerData.getMainBadge() != null) {
            return String.format(" \u2009**%s**%n", playerData.getMainBadge());
        } else {
            return String.format(" \u2009**%s**%n", holder.getBadges().get(0));
        }
    }, true, false),
    CREDITS(EmoteReference.MONEY, i18nContext -> i18nContext.get("commands.profile.credits"),
            (holder, i18nContext) -> String.format(Utils.getLocaleFromLanguage(i18nContext), "$ %,d", holder.getPlayer().getCurrentMoney()),
            true, false
    ),
    OLD_CREDITS(EmoteReference.DOLLAR, i18nContext -> i18nContext.get("commands.profile.old_credits"), (holder, i18nContext) ->
            "$ %,d".formatted(holder.getPlayer().getOldMoney()),
            true, false
    ),
    REPUTATION(EmoteReference.REP, i18nContext -> i18nContext.get("commands.profile.rep"), (holder, i18nContext) -> String.valueOf(holder.getPlayer().getReputation())),
    LEVEL(EmoteReference.ZAP, i18nContext -> i18nContext.get("commands.profile.level"), (holder, i18nContext) -> {
        var player = holder.getPlayer();
        return String.format(Utils.getLocaleFromLanguage(i18nContext), "%d (%s: %,d)", player.getLevel(), i18nContext.get("commands.profile.xp"), player.getExperience());
    }, true, false),
    EXPERIENCE(EmoteReference.ZAP, i18nContext -> i18nContext.get("commands.profile.activity_xp"), (holder, i18nContext) -> {
        var data = holder.getPlayer();
        var mine = Utils.roundPrefixNumber(data.getMiningExperience());
        var fish = Utils.roundPrefixNumber(data.getFishingExperience());
        var chop = Utils.roundPrefixNumber(data.getChopExperience());

        return "**Mine:** %s XP | **Fish:** %s XP | **Chop:** %s XP".formatted(mine, fish, chop);
    }, true, false),
    BIRTHDAY(EmoteReference.POPPER, i18nContext -> i18nContext.get("commands.profile.birthday"), (holder, i18nContext) -> {
        var data = holder.getDbUser();

        try {
            if (data.getBirthday() == null)
                return i18nContext.get("commands.profile.not_specified");
            else {
                // This goes through two Formatter calls since it has to first format the stuff birthdays use.
                var parsed = LocalDate.parse(data.getBirthday(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                // Then format it back to a readable format for a human. A little annoying, but works.
                var readable = DateTimeFormatter.ofPattern("MMM d", Utils.getLocaleFromLanguage(data.getLang()));
                return String.format("%s (%s)", readable.format(parsed), data.getBirthday().substring(0, 5));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return i18nContext.get("commands.profile.not_specified");
        }
    }),
    MARRIAGE(EmoteReference.HEART, i18nContext -> i18nContext.get("commands.profile.married"), (holder, i18nContext) -> {
        var userData = holder.getDbUser();
        var currentMarriage = holder.getMarriage();
        User marriedTo = null;

        //Expecting save to work in PlayerCmds, not here, just handle this here.
        if (currentMarriage != null) {
            String marriedToId = currentMarriage.getOtherPlayer(holder.getUser().getId());
            if (marriedToId != null)
                //Yes, this uses complete, not like we have many options.
                try {
                    marriedTo = MantaroBot.getInstance().getShardManager().retrieveUserById(marriedToId).complete();
                } catch (Exception ignored) { }
        }

        if (marriedTo == null) {
            return i18nContext.get("commands.profile.nobody");
        } else {
            if (userData.isPrivateTag()) {
                return String.format("%s", marriedTo.getName());
            } else {
                return String.format("%s#%s", marriedTo.getName(), marriedTo.getDiscriminator());
            }
        }
    }, true, false),
    INVENTORY(EmoteReference.POUCH, i18nContext -> i18nContext.get("commands.profile.inventory"), (holder, i18nContext) -> {
        final var stackList = holder.getPlayer().getInventoryList();
        if (stackList.isEmpty()) {
            return i18nContext.get("general.dust");
        }

        return stackList.stream()
                .map(ItemStack::getItem)
                .sorted(Comparator.comparingLong(Item::getValue).reversed())
                .limit(10)
                .map(Item::getEmoji)
                .collect(Collectors.joining(" \u2009\u2009"));
    }, true, false),
    BADGES(EmoteReference.HEART, i18nContext -> i18nContext.get("commands.profile.badges"), (holder, i18nContext) -> {
        final var badges = holder.getBadges();
        if (badges.isEmpty()) {
            return i18nContext.get("commands.profile.no_badges");
        }

        return badges.stream()
                .limit(5)
                .map(Badge::getUnicode)
                .collect(Collectors.joining(" \u2009\u2009"));
    }, true, false),
    PET(EmoteReference.DOG, i18nContext -> i18nContext.get("commands.profile.pet.header"), (holder, i18nContext) -> {
        final var playerData = holder.getPlayer();
        final var petType = playerData.getActiveChoice(holder.getMarriage());
        HousePet pet = null;
        if (petType == PetChoice.MARRIAGE && holder.getMarriage() != null) {
            pet = holder.getMarriage().getPet();
        }

        if (petType == PetChoice.PERSONAL) {
            pet = playerData.getPet();
        }

        if (pet == null) {
            return i18nContext.get("commands.profile.pet.none");
        }

        return "%s**%s** [%s: %,d, XP: %,d]"
                .formatted(
                        pet.getType().getEmoji(), pet.getName(),
                        i18nContext.get("commands.profile.level"), pet.getLevel(), pet.getExperience()
                );
    }, true, false),
    FOOTER(null, null, (holder, i18nContext) -> {
        var userData = holder.getDbUser();
        String timezone;

        if (userData.getTimezone() == null) {
            timezone = i18nContext.get("commands.profile.no_timezone");
        } else {
            timezone = userData.getTimezone();
        }

        return String.format("%s", String.format(i18nContext.get("commands.profile.timezone_user"), timezone));
    }, false);

    //See: getTitle()
    private final EmoteReference emoji;
    private final Function<I18nContext, String> title;

    private final BiFunction<Holder, I18nContext, String> content;
    private final boolean assignable;
    private final boolean inline;

    ProfileComponent(EmoteReference emoji, Function<I18nContext, String> title,
                     BiFunction<Holder, I18nContext, String> content, boolean isAssignable, boolean inline) {
        this.emoji = emoji;
        this.title = title;
        this.content = content;
        this.assignable = isAssignable;
        this.inline = inline;
    }

    ProfileComponent(EmoteReference emoji, Function<I18nContext, String> title,
                     BiFunction<Holder, I18nContext, String> content, boolean isAssignable) {
        this.emoji = emoji;
        this.title = title;
        this.content = content;
        this.assignable = isAssignable;
        this.inline = true;
    }

    ProfileComponent(EmoteReference emoji, Function<I18nContext, String> title,
                     BiFunction<Holder, I18nContext, String> content) {
        this.emoji = emoji;
        this.title = title;
        this.content = content;
        this.assignable = true;
        this.inline = true;
    }

    /**
     * Looks up the component based on a String value, if nothing is found returns null.
     *
     * @param name The String value to match
     * @return The component, or null if nothing is found.
     */
    public static ProfileComponent lookupFromString(String name) {
        for (var component : ProfileComponent.values()) {
            if (component.name().equalsIgnoreCase(name)) {
                return component;
            }
        }
        return null;
    }

    public String getTitle(I18nContext context) {
        return (emoji == null ? "" : emoji) + " " + title.apply(context);
    }

    public BiFunction<Holder, I18nContext, String> getContent() {
        return this.content;
    }

    public boolean isAssignable() {
        return this.assignable;
    }

    public boolean isInline() {
        return this.inline;
    }

    public static class Holder {
        private User user;
        private Player player;
        private UserDatabase dbUser;
        private List<Badge> badges;
        private Marriage marriage;

        public Holder(User user, Player player, UserDatabase dbUser, Marriage marriage, List<Badge> badges) {
            this.user = user;
            this.player = player;
            this.dbUser = dbUser;
            this.badges = badges;
            this.marriage = marriage;
        }

        public Holder() { }
        public User getUser() {
            return this.user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Player getPlayer() {
            return this.player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public UserDatabase getDbUser() {
            return this.dbUser;
        }

        public void setDbUser(UserDatabase dbUser) {
            this.dbUser = dbUser;
        }

        public List<Badge> getBadges() {
            return this.badges;
        }

        public void setBadges(List<Badge> badges) {
            this.badges = badges;
        }

        public Marriage getMarriage() {
            return marriage;
        }

        public void setMarriage(Marriage marriage) {
            this.marriage = marriage;
        }
    }
}
