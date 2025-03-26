package com.fabbe50.fabsbnb.forge;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FabsBnB.MOD_ID)
public final class FabsBnBForge {
    public FabsBnBForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(FabsBnB.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        FabsBnB.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onPopulateCreativeTab);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        FabsBnBForgeClient.init();
    }

    public void onPopulateCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab().equals(ModRegistries.TAB.get())) {
            for (RegistrySupplier<Item> item : ModRegistries.ITEM_LIST) {
                ItemStack stack = new ItemStack(item.get());
                if (!stack.isEmpty()) {
                    event.accept(stack);
                }
            }
        }
    }
}
