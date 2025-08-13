package com.fabbe50.fabsbnb.mixin;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.world.entity.ai.AvoidEntityWithPotionEffectGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public class CreeperMixin extends Monster {
    protected CreeperMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), method = "registerGoals")
    private void injectRegisterGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(3, new AvoidEntityWithPotionEffectGoal<>(this, LivingEntity.class, ModRegistries.FELINE_AURA, 6.0F, 1.0F, 1.2));
    }
}
