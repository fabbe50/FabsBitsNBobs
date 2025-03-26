package com.fabbe50.fabsbnb.fabric.client;

import com.fabbe50.fabsbnb.ClothScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ClothScreen::getConfigScreen;
    }
}
