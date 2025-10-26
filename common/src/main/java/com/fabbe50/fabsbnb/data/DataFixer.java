package com.fabbe50.fabsbnb.data;

import com.fabbe50.fabsbnb.FabsBnB;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class DataFixer {
    public static Map<ResourceLocation, ResourceLocation> BLOCK_FIXER = new HashMap<>();
    public static Map<ResourceLocation, ResourceLocation> ITEM_FIXER = new HashMap<>();

    static {
        BLOCK_FIXER.put(FabsBnB.location("pusher_block"), FabsBnB.location("normal_pusher_block"));

        ITEM_FIXER.put(FabsBnB.location("pusher_block"), FabsBnB.location("normal_pusher_block"));
    }
}
