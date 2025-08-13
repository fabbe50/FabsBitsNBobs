package com.fabbe50.fabsbnb.world.entity.ai;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class AvoidEntityWithPotionEffectGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
    private final Holder<MobEffect> mobEffect;

    public AvoidEntityWithPotionEffectGoal(PathfinderMob pathfinderMob, Class<T> class_, Holder<MobEffect> mobEffect, float f, double d, double e) {
        super(pathfinderMob, class_, f, d, e);
        this.mobEffect = mobEffect;
    }

    @Override
    public boolean canUse() {
        boolean superResult = super.canUse();
        if (this.toAvoid == null) {
            return false;
        }
        return superResult && this.toAvoid.hasEffect(mobEffect);
    }
}
