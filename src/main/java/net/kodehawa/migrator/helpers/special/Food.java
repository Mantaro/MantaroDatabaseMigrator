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

public class Food extends Item {
    private final int hungerLevel;
    private final FoodType type;

    public Food(FoodType type, int hungerLevel, String emoji, String name, String translatedName,
                String desc, long value, boolean buyable) {
        super(ItemType.PET_FOOD, emoji, name, translatedName, desc, value, true, buyable);
        this.hungerLevel = hungerLevel;
        this.type = type;
    }

    public int getHungerLevel() {
        return this.hungerLevel;
    }

    public FoodType getType() {
        return type;
    }

    public enum FoodType {
        CAT, DOG, HAMSTER, GENERAL;
    }
}
