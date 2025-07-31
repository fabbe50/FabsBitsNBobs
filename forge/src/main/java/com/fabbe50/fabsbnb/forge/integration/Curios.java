package com.fabbe50.fabsbnb.forge.integration;

import com.fabbe50.fabsbnb.Utilities;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class Curios {
    public static void registerCurios() {
        CuriosApi.registerCurio(ModRegistries.CHOCOLATE_NECKLACE.get(), new ChocolateNecklaceCurio());
    }

    public static class CurioItem implements ICurioItem {

    }

    public static class ChocolateNecklaceCurio extends CurioItem {
        @Override
        public boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
            return true;
        }

        @Override
        public void curioTick(SlotContext slotContext, ItemStack stack) {
            LivingEntity livingEntity = slotContext.entity();
            if (livingEntity != null) {
                Utilities.clearMobEffects(livingEntity, false);
            }
        }
    }
}
