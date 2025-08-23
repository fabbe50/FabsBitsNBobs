package com.fabbe50.fabsbnb.mixin;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {
    @Inject(method = "createForEnchantment", at = @At("HEAD"), cancellable = true)
    private static void injectCreateForEnchantment(EnchantmentInstance enchantmentInstance, CallbackInfoReturnable<ItemStack> cir) {
        if (enchantmentInstance.enchantment.is(key -> key.location().getNamespace().equals(FabsBnB.MOD_ID))) {
            ItemStack itemStack = new ItemStack(ModRegistries.EXT_ENCHANTED_BOOK.get());
            itemStack.enchant(enchantmentInstance.enchantment, enchantmentInstance.level);
            cir.setReturnValue(itemStack);
        }
    }
}
