package com.fabbe50.fabsbnb.fabric.integration;

import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Trinkets {
    public static void registerTrinkets() {
        TrinketsApi.registerTrinket(ModRegistries.CHOCOLATE_NECKLACE.get(), new ChocolateNecklaceTrinket());
    }

    public static boolean isWearingTrinket(LivingEntity entity, Item item) {
        return TrinketsApi.getTrinketComponent(entity).orElseThrow().isEquipped(item);
    }

    public static class ChocolateNecklaceTrinket implements Trinket {
        @Override
        public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
            if (entity != null) {
                if (Utilities.clearMobEffects(entity, false)) {
                    Utilities.hurtItem(entity, stack);
                }
            }
        }
    }
}
