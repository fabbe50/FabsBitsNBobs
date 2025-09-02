package com.fabbe50.fabsbnb.mixin;

import com.fabbe50.fabsbnb.events.ItemStackEvent;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V", at = @At("TAIL"))
    private void injectInit(ItemLike itemLike, int i, PatchedDataComponentMap patchedDataComponentMap, CallbackInfo ci) {
        ItemStackEvent.CREATED.invoker().onStackCreated((ItemStack) (Object) this);
    }

    @Inject(method = "<init>(Ljava/lang/Void;)V", at = @At("TAIL"))
    private void injectInit(Void void_, CallbackInfo ci) {
        ItemStackEvent.CREATED.invoker().onStackCreated((ItemStack) (Object) this);
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void injectInventoryTick(Level level, Entity entity, EquipmentSlot equipmentSlot, CallbackInfo ci) {
        ItemStackEvent.INVENTORY_TICK.invoker().inventoryTick((ItemStack) (Object) this, level, entity);
    }
}
