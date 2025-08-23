package com.fabbe50.fabsbnb.mixin;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "getComponentType", at = @At("RETURN"), cancellable = true)
    private static void injectGetComponentType(ItemStack itemStack, CallbackInfoReturnable<DataComponentType<ItemEnchantments>> cir) {
        if (itemStack.is(ModRegistries.EXT_ENCHANTED_BOOK)) {
            cir.setReturnValue(DataComponents.STORED_ENCHANTMENTS);
        }
    }
}
