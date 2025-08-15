package com.fabbe50.fabsbnb;

import com.fabbe50.fabsbnb.registries.*;
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
    }

    public static ResourceLocation location(String owner, String name) {
        return ResourceLocation.fromNamespaceAndPath(owner, name);
    }

    public static ResourceLocation location(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void log(String msg) {
        LOGGER.info(msg);
    }

    public static void debug(String msg) {
        if (ModConfig.debugMode.getValue()) {
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
