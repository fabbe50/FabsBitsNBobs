package com.fabbe50.fabsbnb.fabric.client;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.FabsBnBClient;
import com.fabbe50.fabsbnb.Platform;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return FabsBnBClient::getConfigScreen;
    }

    static {
        if (Platform.isModLoaded("cloth_config")) {
            FabsBnB.log("Mod Menu and Cloth Config API are both loaded! Config screen is available.");
        } else {
            FabsBnB.warn("Mod Menu is loaded, but Cloth Config API is not installed. Config Screen is not available!");
        }
    }
}
