package com.fabbe50.fabsbnb;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.gui.entries.DoubleListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.IOException;

public class ClothScreen {
    public static Screen getConfigScreen(Screen parent) {
        Component title = Component.literal("Fab's Bits & Bobs");

        var builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(title);

        var entryBuilder = builder.entryBuilder();
        var general = builder.getOrCreateCategory(FabsBnB.translatable("category.general"));

        IntegerSliderEntry woodenWandRadius = entryBuilder.startIntSlider(FabsBnB.translatable("config.wand-radius.wooden"), ModConfig.INSTANCE.woodenBuildingWandRadius, 1, 30)
                .setDefaultValue(1)
                .setTooltip(FabsBnB.translatable("config.wand-radius.wooden.tooltip"))
                .setTextGetter(integer -> FabsBnB.translatable("value.blocks", integer))
                .setSaveConsumer(integer -> ModConfig.INSTANCE.woodenBuildingWandRadius = integer)
                .build();
        IntegerSliderEntry stoneWandRadius = entryBuilder.startIntSlider(FabsBnB.translatable("config.wand-radius.stone"), ModConfig.INSTANCE.stoneBuildingWandRadius, 1, 30)
                .setDefaultValue(2)
                .setTooltip(FabsBnB.translatable("config.wand-radius.stone.tooltip"))
                .setTextGetter(integer -> FabsBnB.translatable("value.blocks", integer))
                .setSaveConsumer(integer -> ModConfig.INSTANCE.stoneBuildingWandRadius = integer)
                .build();
        IntegerSliderEntry ironWandRadius = entryBuilder.startIntSlider(FabsBnB.translatable("config.wand-radius.iron"), ModConfig.INSTANCE.ironBuildingWandRadius, 1, 30)
                .setDefaultValue(7)
                .setTooltip(FabsBnB.translatable("config.wand-radius.iron.tooltip"))
                .setTextGetter(integer -> FabsBnB.translatable("value.blocks", integer))
                .setSaveConsumer(integer -> ModConfig.INSTANCE.ironBuildingWandRadius = integer)
                .build();
        IntegerSliderEntry goldWandRadius = entryBuilder.startIntSlider(FabsBnB.translatable("config.wand-radius.gold"), ModConfig.INSTANCE.goldBuildingWandRadius, 1, 30)
                .setDefaultValue(5)
                .setTooltip(FabsBnB.translatable("config.wand-radius.gold.tooltip"))
                .setTextGetter(integer -> FabsBnB.translatable("value.blocks", integer))
                .setSaveConsumer(integer -> ModConfig.INSTANCE.goldBuildingWandRadius = integer)
                .build();
        IntegerSliderEntry diamondWandRadius = entryBuilder.startIntSlider(FabsBnB.translatable("config.wand-radius.diamond"), ModConfig.INSTANCE.diamondBuildingWandRadius, 1, 30)
                .setDefaultValue(9)
                .setTooltip(FabsBnB.translatable("config.wand-radius.diamond.tooltip"))
                .setTextGetter(integer -> FabsBnB.translatable("value.blocks", integer))
                .setSaveConsumer(integer -> ModConfig.INSTANCE.diamondBuildingWandRadius = integer)
                .build();
        IntegerSliderEntry netheriteWandRadius = entryBuilder.startIntSlider(FabsBnB.translatable("config.wand-radius.netherite"), ModConfig.INSTANCE.netheriteBuildingWandRadius, 1, 30)
                .setDefaultValue(15)
                .setTooltip(FabsBnB.translatable("config.wand-radius.netherite.tooltip"))
                .setTextGetter(integer -> FabsBnB.translatable("value.blocks", integer))
                .setSaveConsumer(integer -> ModConfig.INSTANCE.netheriteBuildingWandRadius = integer)
                .build();

        DoubleListEntry whooshWandMultiplier = entryBuilder.startDoubleField(FabsBnB.translatable("config.whoosh-wand.multiplier"), ModConfig.INSTANCE.whooshWandMultiplier)
                .setDefaultValue(3.0)
                .setTooltip(FabsBnB.translatable("config.whoosh-wand.multiplier.tooltip"))
                .setSaveConsumer(aDouble -> ModConfig.INSTANCE.whooshWandMultiplier = aDouble)
                .build();

        DoubleListEntry entityMoverBlockSpeed = entryBuilder.startDoubleField(FabsBnB.translatable("config.pusher.speed"), ModConfig.INSTANCE.entityMoverBlockSpeed)
                .setDefaultValue(0.3)
                .setTooltip(FabsBnB.translatable("config.pusher.speed.tooltip"))
                .setSaveConsumer(aDouble -> ModConfig.INSTANCE.entityMoverBlockSpeed = aDouble)
                .build();

        general.addEntry(woodenWandRadius);
        general.addEntry(stoneWandRadius);
        general.addEntry(ironWandRadius);
        general.addEntry(goldWandRadius);
        general.addEntry(diamondWandRadius);
        general.addEntry(netheriteWandRadius);
        general.addEntry(whooshWandMultiplier);
        general.addEntry(entityMoverBlockSpeed);

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
