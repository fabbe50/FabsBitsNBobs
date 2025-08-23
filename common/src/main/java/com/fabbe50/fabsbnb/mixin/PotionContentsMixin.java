package com.fabbe50.fabsbnb.mixin;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionContents.class)
public class PotionContentsMixin {
    @Inject(method = "createItemStack", at = @At("HEAD"), cancellable = true)
    private static void injectCreateItemStack(Item item, Holder<Potion> holder, CallbackInfoReturnable<ItemStack> cir) {
        if (holder.is(key -> key.location().getNamespace().equals(FabsBnB.MOD_ID))) {
            ResourceLocation location = item.arch$registryName();
            if (location != null) {
                ItemStack stack = switch (location.getPath()){
                    case "potion" -> new ItemStack(ModRegistries.OWN_POTION_ITEM);
                    case "splash_potion" -> new ItemStack(ModRegistries.OWN_SPLASH_POTION_ITEM);
                    case "lingering_potion" -> new ItemStack(ModRegistries.OWN_LINGERING_POTION_ITEM);
                    case "tipped_arrow" -> new ItemStack(ModRegistries.OWN_TIPPED_ARROW_ITEM);
                    default -> null;
                };
                if (stack != null) {
                    stack.set(DataComponents.POTION_CONTENTS, new PotionContents(holder));
                    cir.setReturnValue(stack);
                }
            }
        }
    }
}
