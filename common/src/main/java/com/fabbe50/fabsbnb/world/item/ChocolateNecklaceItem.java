package com.fabbe50.fabsbnb.world.item;

import com.fabbe50.fabsbnb.Utilities;
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
            if (Utilities.clearMobEffects(livingEntity, false)) {
                itemStack.hurtAndBreak(1, livingEntity, livingEntity.getEquipmentSlotForItem(itemStack));
            }
        }
    }
}
