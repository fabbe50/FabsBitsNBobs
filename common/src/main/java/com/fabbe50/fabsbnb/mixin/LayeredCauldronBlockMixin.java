package com.fabbe50.fabsbnb.mixin;

import com.fabbe50.fabsbnb.data.CauldronConversionData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayeredCauldronBlock.class)
public abstract class LayeredCauldronBlockMixin {
    @Shadow public abstract boolean isFull(BlockState arg);

    @Inject(at = @At("TAIL"), method = "entityInside")
    public void injectEntityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, CallbackInfo ci) {
        if (this.isFull(blockState)) {
            if (entity instanceof ItemEntity itemEntity) {
                ItemStack inputStack = itemEntity.getItem();
                for (Item ingredient : CauldronConversionData.getConversionMap().keySet()) {
                    if (inputStack.is(ingredient)) {
                        itemEntity.setItem(new ItemStack(CauldronConversionData.getConversionMap().get(ingredient), inputStack.getCount()));
                    }
                }
            }
        }
    }
}
