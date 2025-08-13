package com.fabbe50.fabsbnb.forge;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.Platform;
import com.fabbe50.fabsbnb.forge.integration.Curios;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

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
            Curios.registerCurios();
        }
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
