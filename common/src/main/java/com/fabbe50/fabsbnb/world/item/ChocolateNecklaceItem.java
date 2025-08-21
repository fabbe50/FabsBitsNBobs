package com.fabbe50.fabsbnb.world.item;

import com.fabbe50.fabsbnb.ModConfig;
import com.fabbe50.fabsbnb.util.Utilities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChocolateNecklaceItem extends Item {
    public ChocolateNecklaceItem(Properties properties) {
        super(properties.stacksTo(1).durability(256));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.isHolding(stack -> stack.equals(itemStack)) || ModConfig.necklaceWorksInInventory.getValue()) {
                if (Utilities.clearMobEffects(livingEntity, false)) {
                    Utilities.hurtItem(livingEntity, itemStack);
                }
            }
        }
    }
}
