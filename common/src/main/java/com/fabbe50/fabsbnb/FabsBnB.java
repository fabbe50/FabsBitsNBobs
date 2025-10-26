package com.fabbe50.fabsbnb;

import com.fabbe50.fabsbnb.registries.*;
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public final class FabsBnB {
    public static final String MOD_ID = "fabsbnb";

    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static void init() {
        log("Initializing Fab's Bits & Bobs...");

        // Write common init code here.
        ModConfig.register();
        ModRegistries.init();
        EventRegistry.register();

        Platform.dataFix();
    }

    public static ResourceLocation location(String owner, String name) {
        return ResourceLocation.fromNamespaceAndPath(owner, name);
    }

    public static ResourceLocation location(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static String locationString(String name) {
        return location(name).toString();
    }

    public static ResourceKey<Recipe<?>> recipeKey(String name) {
        return key(Registries.RECIPE, name);
    }

    public static <T> ResourceKey<T> key(ResourceKey<Registry<T>> registry, String name) {
        return ResourceKey.create(registry, location(name));
    }

    public static void log(String msg) {
        LOGGER.info(msg);
    }

    public static void debug(String msg) {
        if (ModConfig.debugMode.getValue()) {
            LOGGER.info("[DEBUG] {}", msg);
        } else {
            LOGGER.debug(msg);
        }
    }

    public static void warn(String msg) {
        LOGGER.warn(msg);
    }

    public static void error(String msg) {
        LOGGER.error(msg);
    }
}