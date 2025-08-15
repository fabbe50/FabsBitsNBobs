package com.fabbe50.fabsbnb;

import com.fabbe50.fabsbnb.config.IConfigOption;
import com.fabbe50.fabsbnb.util.LangUtils;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.IOException;

public class ClothScreen {
    public static Screen getConfigScreen(Screen parent) {
        Component title = LangUtils.MOD_NAME_C;

        var builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(title);

        var entryBuilder = builder.entryBuilder();
        var general = builder.getOrCreateCategory(LangUtils.getConfig("category.general"));

        for (IConfigOption<?, ?> configOption : ModConfig.getConfigOptions().values()) {
            general.addEntry(configOption.buildClothEntry(entryBuilder));
        }

        return builder.setSavingRunnable(() -> {
            try {
                ModConfig.save(ModConfig.getConfigFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ModConfig.load(ModConfig.getConfigFile());
        }).build();
    }
}
