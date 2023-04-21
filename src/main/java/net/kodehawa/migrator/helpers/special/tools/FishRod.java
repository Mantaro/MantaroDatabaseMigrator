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

package net.kodehawa.migrator.helpers.special.tools;

import net.kodehawa.migrator.helpers.Item;
import net.kodehawa.migrator.helpers.ItemType;
import net.kodehawa.migrator.helpers.special.helpers.Castable;
import net.kodehawa.migrator.helpers.special.helpers.Salvageable;
import net.kodehawa.migrator.helpers.special.helpers.attributes.Attribute;
import net.kodehawa.migrator.helpers.special.helpers.attributes.ItemUsage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FishRod extends Item implements Castable, Salvageable, Attribute {
    private final int level;
    private final int castLevelRequired;
    private final int maximumCastAmount;
    private final int maxDurability;
    private final int rarity;
    private final String explanation;
    private final List<Integer> salvageReturns;

    public FishRod(ItemType type, int level, int castLevelRequired, int maximumCastAmount, String emoji, String name,
                   String translatedName, String desc, String explanation, int rarity, long value, String recipe,
                   int maxDurability, int... recipeTypes) {
        super(type, emoji, name, translatedName, desc, value, true, false, recipe, recipeTypes);
        this.level = level;
        this.castLevelRequired = castLevelRequired;
        this.maximumCastAmount = maximumCastAmount;
        this.maxDurability = maxDurability;
        this.salvageReturns = Arrays.stream(recipeTypes).boxed().collect(Collectors.toList());
        this.rarity = rarity;
        this.explanation = explanation;
    }

    public FishRod(ItemType type, int level, int castLevelRequired, int maximumCastAmount, String emoji, String name,
                   String alias, String translatedName, String desc, String explanation, int rarity, long value, String recipe,
                   int maxDurability, int... recipeTypes) {
        super(type, emoji, name, alias, translatedName, desc, value, true, false, recipe, recipeTypes);
        this.level = level;
        this.castLevelRequired = castLevelRequired;
        this.maximumCastAmount = maximumCastAmount;
        this.maxDurability = maxDurability;
        this.salvageReturns = Arrays.stream(recipeTypes).boxed().collect(Collectors.toList());
        this.rarity = rarity;
        this.explanation = explanation;
    }

    public FishRod(ItemType type, int level, int castLevelRequired, int maximumCastAmount, String emoji, String name,
                   String translatedName, String desc, String explanation, int rarity, long value, boolean buyable, String recipe,
                   int maxDurability, int... recipeTypes) {
        super(type, emoji, name, translatedName, desc, value, true, buyable, recipe, recipeTypes);
        this.level = level;
        this.castLevelRequired = castLevelRequired;
        this.maximumCastAmount = maximumCastAmount;
        this.maxDurability = maxDurability;
        this.salvageReturns = Arrays.stream(recipeTypes).filter(id -> id > 1).boxed().collect(Collectors.toList());
        this.rarity = rarity;
        this.explanation = explanation;
    }

    public FishRod(ItemType type, int level, int castLevelRequired, int maximumCastAmount, String emoji, String name,
                   String alias, String translatedName, String desc, String explanation, int rarity, long value, boolean buyable, String recipe,
                   int maxDurability, int... recipeTypes) {
        super(type, emoji, name, alias, translatedName, desc, value, true, buyable, recipe, recipeTypes);
        this.level = level;
        this.castLevelRequired = castLevelRequired;
        this.maximumCastAmount = maximumCastAmount;
        this.maxDurability = maxDurability;
        this.salvageReturns = Arrays.stream(recipeTypes).filter(id -> id > 1).boxed().collect(Collectors.toList());
        this.rarity = rarity;
        this.explanation = explanation;
    }

    @Override
    public int getMaxDurability() {
        return maxDurability;
    }

    public int getLevel() {
        return this.level;
    }

    public int getCastLevelRequired() {
        return this.castLevelRequired;
    }

    public int getMaximumCastAmount() {
        return this.maximumCastAmount;
    }

    @Override
    public List<Integer> getReturns() {
        return salvageReturns;
    }

    @Override
    public int getTier() {
        return rarity;
    }

    @Override
    public String buildAttributes() {
        return null;
    }

    @Override
    public String getExplanation() {
        return explanation;
    }

    @Override
    public ItemUsage getType() {
        return ItemUsage.FISHING;
    }
}
