package net.kodehawa.migrator.helpers;

import java.util.Arrays;
import java.util.Optional;

public class ItemHelper {
    public static int idOf(Item item) {
        return Arrays.asList(ItemReference.ALL)
                .indexOf(item);
    }

    public static Item fromId(int id) {
        return ItemReference.ALL[id];
    }

    public static Optional<Item> fromTranslationSlice(String slice) {
        return Arrays.stream(ItemReference.ALL)
                .filter(item -> item.getTranslatedName().equals("items." + slice))
                .findFirst();
    }
}
