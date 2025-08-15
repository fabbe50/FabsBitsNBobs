package com.fabbe50.fabsbnb;

import com.fabbe50.fabsbnb.registries.*;
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public final class FabsBnB {
    public static final String MOD_ID = "fabsbnb";

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static void init() {
        // Write common init code here.
        ModConfig.register();
        ModRegistries.init();
        EventRegistry.register();
    }

    public static Component translatable(String key) {
        return Component.translatable(translation(key));
    public static ResourceLocation location(String owner, String name) {
        return ResourceLocation.fromNamespaceAndPath(owner, name);
    }

    public static Component translatable(String key, Object value) {
        return Component.translatable(translation(key), value);
    public static ResourceLocation location(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static Component translatable(String key, Object value, ChatFormatting formatting) {
        return Component.translatable(translation(key), value).withStyle(formatting);
    }

    public static String translation(String key) {
        return MOD_ID + "." + key;
    }

    }

    }
}
