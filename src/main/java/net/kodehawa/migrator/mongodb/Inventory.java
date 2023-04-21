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

import net.kodehawa.migrator.helpers.Item;
import net.kodehawa.migrator.helpers.ItemHelper;
import net.kodehawa.migrator.helpers.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Inventory {
    private static final Logger LOGGER = LoggerFactory.getLogger("Inventory");
    private Map<String, Integer> stored = new HashMap<>();

    public Inventory() {}

    public List<ItemStack> asList() {
        return unserialize(stored);
    }

    public Map<Item, ItemStack> asMap() {
        return ItemStack.mapped(asList());
    }

    public void clear() {
        replaceWith(new ArrayList<>());
    }

    public void clearOnlySellables() {
        List<ItemStack> ns = asList().stream().filter(item -> !item.getItem().isSellable()).collect(Collectors.toList());
        replaceWith(ns);
    }

    public boolean containsItem(Item item) {
        return asMap().containsKey(item);
    }

    public ItemStack getStackOf(Item item) {
        if (containsItem(item)) {
            return asMap().get(item);
        } else {
            return null;
        }
    }

    public int getAmount(Item item) {
        return asMap().getOrDefault(item, new ItemStack(item, 0)).getAmount();
    }

    public boolean merge(List<ItemStack> inv) {
        Map<String, Integer> map = new HashMap<>(stored);
        Map<String, Integer> toAdd = serialize(inv);
        boolean[] hadOverflow = {false};
        toAdd.forEach((id, amount) -> {
            int currentAmount = map.getOrDefault(id, 0);
            if (currentAmount + amount > 5000) {
                currentAmount = 5000;
                hadOverflow[0] = true;
            } else {
                currentAmount += amount;
            }
            map.put(id, currentAmount);
        });
        replaceWith(unserialize(map));
        return hadOverflow[0];
    }

    public void process(List<ItemStack> is) {
        if (merge(is)) {
            LOGGER.error("Unhandled 'overflow' at {}", Thread.currentThread().getStackTrace()[2]);
        }
    }

    public void process(ItemStack... stacks) {
        merge(Arrays.asList(stacks));
    }

    public void replaceWith(List<ItemStack> inv) {
        stored = serialize(inv);
    }

    public static Map<String, Integer> serialize(List<ItemStack> list) {
        Map<String, Integer> collect = list.stream().filter(stack -> stack.getAmount() != 0)
                .collect(Collectors.toMap(stack -> stack.getItem().getTranslatedName().split("\\.")[1], ItemStack::getAmount, Integer::sum));
        collect.values().remove(0);
        return collect;
    }

    public static List<ItemStack> unserialize(Map<String, Integer> map) {
        return map.entrySet().stream().filter(e -> e.getValue() != 0)
                .filter(e -> ItemHelper.fromTranslationSlice(e.getKey()).isPresent())
                .map(entry -> new ItemStack(ItemHelper.fromTranslationSlice(entry.getKey()).get(), Math.max(Math.min(entry.getValue(), 5000), 0)))
                .collect(Collectors.toList());
    }
}
