package com.fabbe50.fabsbnb.fabric.mixin;

import com.fabbe50.fabsbnb.Platform;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public class PiglinAIMixin {
    @Inject(method = "isWearingSafeArmor", at = @At("TAIL"), cancellable = true)
    private static void injectIsWearingSafeArmor(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (Platform.isWearingNecklace(livingEntity)) {
            cir.setReturnValue(true);
        }
    }
}
