package com.fabbe50.fabsbnb.mixin;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComposterBlock.class)
public abstract class ComposterBlockMixin {
    @Shadow
    private static void add(float f, ItemLike itemLike) {
    }

    @Inject(method = "bootStrap", at = @At("TAIL"))
    private static void injectBootstrap(CallbackInfo ci) {
        add(0.5f, Items.ROTTEN_FLESH);
        add(0.3f, Items.BAMBOO);
        add(1f, Items.POISONOUS_POTATO);
        add(0.3f, Items.SPIDER_EYE);
        add(0.3f, Items.CHORUS_FRUIT);
        add(0.3f, Items.CHORUS_FLOWER);
    }
}
