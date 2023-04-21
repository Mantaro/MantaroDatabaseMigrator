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

package net.kodehawa.migrator.helpers.special;

import net.kodehawa.migrator.helpers.Item;
import net.kodehawa.migrator.helpers.ItemType;

public class Potion extends Item {
    private final int maxUses;

    public Potion(ItemType type, int maxUses, String emoji, String name, String translatedName,
                  String desc, long value, String recipe, int... recipeTypes) {
        super(type, emoji, name, translatedName, desc, value, true, false, recipe, recipeTypes);
        this.maxUses = maxUses;
    }

    public Potion(ItemType type, int maxUses, String emoji, String name, String translatedName,
                  String desc, long value, boolean buyable, String recipe, int... recipeTypes) {
        super(type, emoji, name, translatedName, desc, value, true, buyable, recipe, recipeTypes);
        this.maxUses = maxUses;
    }

    public Potion(ItemType type, int maxUses, String emoji, String name, String translatedName,
                  String desc, long value, boolean buyable) {
        super(type, emoji, name, translatedName, desc, value, true, buyable);
        this.maxUses = maxUses;
    }

    public int getMaxUses() {
        return this.maxUses;
    }
}
