package com.fabbe50.fabsbnb.mixin;

import com.fabbe50.fabsbnb.FabsBnB;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandMenu.PotionSlot.class)
public class BrewingStandMenuPotionSlotMixin {
    @Inject(method = "mayPlaceItem", at = @At("RETURN"), cancellable = true)
    private static void injectMayPlaceItem(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.is(TagKey.create(Registries.ITEM, FabsBnB.location("c", "potions/bottle")))) {
            cir.setReturnValue(true);
        }
    }
}
