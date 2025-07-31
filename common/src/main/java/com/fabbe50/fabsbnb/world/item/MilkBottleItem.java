package com.fabbe50.fabsbnb.world.item;

import com.fabbe50.fabsbnb.Utilities;
import net.minecraft.world.entity.LivingEntity;

public class MilkBottleItem extends DrinkItem {
    public MilkBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    void applyEffects(LivingEntity livingEntity) {
        Utilities.clearMobEffects(livingEntity, true);
    }
}
