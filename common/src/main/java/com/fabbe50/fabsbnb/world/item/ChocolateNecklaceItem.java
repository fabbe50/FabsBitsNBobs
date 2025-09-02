package com.fabbe50.fabsbnb.world.item;

import com.fabbe50.fabsbnb.ModConfig;
import com.fabbe50.fabsbnb.util.Utilities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ChocolateNecklaceItem extends Item {
    public ChocolateNecklaceItem(Properties properties) {
        super(properties.stacksTo(1).durability(ModConfig.necklaceDurability.getValue()).enchantable(15));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel serverLevel, Entity entity, @Nullable EquipmentSlot equipmentSlot) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.isHolding(stack -> stack.equals(itemStack)) || ModConfig.necklaceWorksInInventory.getValue()) {
                if (Utilities.clearMobEffects(livingEntity, false)) {
                    Utilities.hurtItem(livingEntity, itemStack);
                }
            }
        }
    }
}
