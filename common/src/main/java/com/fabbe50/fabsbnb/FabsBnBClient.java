package com.fabbe50.fabsbnb;

import com.fabbe50.fabsbnb.registries.client.ClientRegistries;
import net.minecraft.client.gui.screens.Screen;

public class FabsBnBClient {
    public static void initClient() {
        FabsBnB.log("Initializing Fab's Bits & Bobs Client...");
        ClientRegistries.init();
    }

    public static Screen getConfigScreen(Screen parent) {
        if (Platform.isModLoaded("cloth_config")) {
            return getConfigScreen(parent);
        }
        return null;
    }
}
