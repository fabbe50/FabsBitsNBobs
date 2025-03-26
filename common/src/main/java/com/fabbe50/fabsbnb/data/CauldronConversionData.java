package com.fabbe50.fabsbnb.data;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class CauldronConversionData {
    private static final Map<Item, Item> CONVERSION_MAP = new HashMap<>();

    public static Map<Item, Item> getConversionMap() {
        return CONVERSION_MAP;
    }

    static {
        CONVERSION_MAP.put(Items.WHITE_CONCRETE_POWDER,                 Items.WHITE_CONCRETE);
        CONVERSION_MAP.put(Items.LIGHT_GRAY_CONCRETE_POWDER,            Items.LIGHT_GRAY_CONCRETE);
        CONVERSION_MAP.put(Items.GRAY_CONCRETE_POWDER,                  Items.GRAY_CONCRETE);
        CONVERSION_MAP.put(Items.BLACK_CONCRETE_POWDER,                 Items.BLACK_CONCRETE);
        CONVERSION_MAP.put(Items.BROWN_CONCRETE_POWDER,                 Items.BROWN_CONCRETE);
        CONVERSION_MAP.put(Items.RED_CONCRETE_POWDER,                   Items.RED_CONCRETE);
        CONVERSION_MAP.put(Items.ORANGE_CONCRETE_POWDER,                Items.ORANGE_CONCRETE);
        CONVERSION_MAP.put(Items.YELLOW_CONCRETE_POWDER,                Items.YELLOW_CONCRETE);
        CONVERSION_MAP.put(Items.LIME_CONCRETE_POWDER,                  Items.LIME_CONCRETE);
        CONVERSION_MAP.put(Items.GREEN_CONCRETE_POWDER,                 Items.GREEN_CONCRETE);
        CONVERSION_MAP.put(Items.CYAN_CONCRETE_POWDER,                  Items.CYAN_CONCRETE);
        CONVERSION_MAP.put(Items.LIGHT_BLUE_CONCRETE_POWDER,            Items.LIGHT_BLUE_CONCRETE);
        CONVERSION_MAP.put(Items.BLUE_CONCRETE_POWDER,                  Items.BLUE_CONCRETE);
        CONVERSION_MAP.put(Items.PURPLE_CONCRETE_POWDER,                Items.PURPLE_CONCRETE);
        CONVERSION_MAP.put(Items.MAGENTA_CONCRETE_POWDER,               Items.MAGENTA_CONCRETE);
        CONVERSION_MAP.put(Items.PINK_CONCRETE_POWDER,                  Items.PINK_CONCRETE);
        CONVERSION_MAP.put(ModRegistries.ITEM_LAVA_SPONGE_USED.get(),   ModRegistries.ITEM_LAVA_SPONGE.get());
    }
}
