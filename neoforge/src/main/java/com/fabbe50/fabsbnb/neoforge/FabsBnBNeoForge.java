package com.fabbe50.fabsbnb.neoforge;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.Platform;
import com.fabbe50.fabsbnb.neoforge.integration.Curios;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.registries.PotionBrewingRecipes;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@Mod(FabsBnB.MOD_ID)
public final class FabsBnBNeoForge {
    public FabsBnBNeoForge(IEventBus eventBus) {
        // Run our common setup.
        FabsBnB.init();

        eventBus.addListener(this::onCommonSetup);
        eventBus.addListener(this::onPopulateCreativeTab);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        if (Platform.isModLoaded("curios")) {
            FabsBnB.log("Curios loaded! Registering curios...");
            Curios.registerCurios();
        }
    }

    public void onPopulateCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab().equals(ModRegistries.TAB.get())) {
            FabsBnB.log("Setting up creative tab...");
            for (RegistrySupplier<Item> item : ModRegistries.ITEM_LIST) {
                ItemStack stack = new ItemStack(item.get());
                if (!stack.isEmpty()) {
                    event.accept(stack);
                }
            }
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
