package com.fabbe50.fabsbnb.neoforge;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.Platform;
import com.fabbe50.fabsbnb.data.DataFixer;
import com.fabbe50.fabsbnb.neoforge.integration.Curios;
import com.fabbe50.fabsbnb.registries.PotionBrewingRecipes;
import com.google.common.eventbus.Subscribe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.IRegistryExtension;

@Mod(FabsBnB.MOD_ID)
public final class FabsBnBNeoForge {
    public FabsBnBNeoForge(IEventBus eventBus, ModContainer modContainer) {
        // Run our common setup.
        FabsBnB.init();

        eventBus.addListener(this::onCommonSetup);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        if (Platform.isModLoaded("curios")) {
            FabsBnB.log("Curios loaded! Registering curios...");
            Curios.registerCurios();
        }
    }

    @EventBusSubscriber(modid = FabsBnB.MOD_ID)
    public static class Events {
        @SubscribeEvent
        public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
            PotionBrewingRecipes.register(event.getBuilder());
        }
    }
}
