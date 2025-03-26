package com.fabbe50.fabsbnb.mixin;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Phantom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.monster.Phantom$PhantomSweepAttackGoal")
public class PhantomSweepAttackGoalMixin {
    @Unique
    private Phantom fabsbnb$phantom;

    @Inject(at = @At("TAIL"), method = "<init>")
    private void injectInit(Phantom phantom, CallbackInfo ci) {
        this.fabsbnb$phantom = phantom;
    }

    @Inject(at = @At("HEAD"), method = "canContinueToUse", cancellable = true)
    private void injectCanContinueToUse(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity target = fabsbnb$phantom.getTarget();
        if (target != null) {
            if (target.hasEffect(ModRegistries.FELINE_AURA.get())) {
                if (target.level() instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, target.blockPosition(), SoundEvents.CAT_HISS, SoundSource.NEUTRAL);
                }
                cir.setReturnValue(false);
            }
        }
    }
}
