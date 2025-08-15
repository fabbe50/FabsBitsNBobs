package com.fabbe50.fabsbnb.registries;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.world.block.interfaces.ILeftClickable;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class EventRegistry {
    public static void register() {
        InteractionEvent.INTERACT_ENTITY.register((player, entity, interactionHand) -> {
            ItemStack usedStack = player.getItemInHand(interactionHand);
            if (entity instanceof Cat cat && usedStack.is(Items.SHEARS)) {
                if (!player.getAbilities().instabuild) {
                    usedStack.hurtAndBreak(1, player, Utilities.convertInteractionHandToEquipmentSlot(interactionHand));
                }
                ItemStack stack = new ItemStack(ModRegistries.CAT_CLAW.get(), 1);
                ItemEntity drop = new ItemEntity(cat.level(), cat.getX(), cat.getY(), cat.getZ(), stack);
                cat.level().addFreshEntity(drop);
                return EventResult.interruptTrue();
            }
            return EventResult.pass();
        });
        InteractionEvent.LEFT_CLICK_BLOCK.register((player, interactionHand, blockPos, direction) -> {
            Level level = player.level();
            if (level.getBlockState(blockPos).getBlock() instanceof ILeftClickable clickableBlock && player.getMainHandItem().isEmpty() && player.getOffhandItem().isEmpty()) {
                if (level.isClientSide) {
                    return EventResult.interruptTrue();
                }
                return clickableBlock.onLeftClick(level, blockPos, player, interactionHand, direction);
            }
            return EventResult.pass();
        });
        EntityEvent.LIVING_HURT.register((livingEntity, damageSource, v) -> {
            if (damageSource.is(DamageTypes.FLY_INTO_WALL) || damageSource.is(DamageTypes.FALL)) {
                ItemStack mainHand = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack offHand = livingEntity.getItemInHand(InteractionHand.OFF_HAND);
                if (mainHand.is(ModRegistries.WHOOSH_WAND.get())) {
                    mainHand.hurtAndBreak((int)(livingEntity.fallDistance / 2), livingEntity, EquipmentSlot.MAINHAND);
                    return EventResult.interruptFalse();
                }
                if (offHand.is(ModRegistries.WHOOSH_WAND.get())) {
                    offHand.hurtAndBreak((int)(livingEntity.fallDistance / 2), livingEntity, EquipmentSlot.OFFHAND);
                    return EventResult.interruptFalse();
                }
            }
            return EventResult.pass();
        });
        BlockEvent.BREAK.register((level, blockPos, blockState, serverPlayer, intValue) -> {
            if (ModRegistries.VEIN_MINER_ENCHANT.handleEvent(level, blockPos, blockState, serverPlayer)) {
                return EventResult.interruptTrue();
            }
            if (ModRegistries.TREE_CHOPPER_ENCHANT.handleEvent(level, blockPos, blockState, serverPlayer)) {
                return EventResult.interruptTrue();
            }
            return EventResult.pass();
        });
    }
}
