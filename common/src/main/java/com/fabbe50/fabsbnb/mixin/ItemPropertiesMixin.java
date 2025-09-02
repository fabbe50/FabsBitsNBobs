package com.fabbe50.fabsbnb.mixin;

import com.fabbe50.fabsbnb.data.ToolMaterialScanRange;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.Properties.class)
public class ItemPropertiesMixin {
    @Inject(method = "tool", at = @At("HEAD"))
    private void injectTool(ToolMaterial toolMaterial, TagKey<Block> tagKey, float f, float g, float h, CallbackInfoReturnable<Item.Properties> cir) {
        Item.Properties properties = (Item.Properties) (Object) this;
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("scanRange", ToolMaterialScanRange.getScanRangeFromToolTier(toolMaterial).getScanRange());
        properties.component(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
    }
}
