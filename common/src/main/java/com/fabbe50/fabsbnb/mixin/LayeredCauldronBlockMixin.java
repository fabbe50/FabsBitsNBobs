package com.fabbe50.fabsbnb.mixin;

import com.fabbe50.fabsbnb.data.CauldronConversionData;
import com.fabbe50.fabsbnb.loaders.CauldronConversionDataLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
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
    public void injectEntityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier, CallbackInfo ci) {
        if (this.isFull(blockState)) {
            if (entity instanceof ItemEntity itemEntity) {
                ItemStack inputStack = itemEntity.getItem();
                for (CauldronConversionData conversionData : CauldronConversionDataLoader.INSTANCE.getDataMap().values()) {
                    ResourceLocation inputLocation = conversionData.input();
                    ResourceLocation outputLocation = conversionData.output();
                    if (inputLocation != null && outputLocation != null) {
                        ResourceLocation itemInside = inputStack.getItem().arch$registryName();
                        if (itemInside != null) {
                            if (itemInside.toString().equals(inputLocation.toString())) {
                                itemEntity.setItem(new ItemStack(CauldronConversionData.getConversionMap().get(level.registryAccess().lookup(Registries.ITEM).orElseThrow().getValue(outputLocation)), inputStack.getCount()));
                            }
                        }
                    }
                }
            }
        }
    }
}
