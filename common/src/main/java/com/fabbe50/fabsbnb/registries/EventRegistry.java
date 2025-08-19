package com.fabbe50.fabsbnb.registries;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.data.CustomFoodData;
import com.fabbe50.fabsbnb.events.ItemStackEvent;
import com.fabbe50.fabsbnb.loaders.CustomFoodDataLoader;
import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.world.block.interfaces.ILeftClickable;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.common.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EventRegistry {
    private static final FoodProperties GLISTERING_MELON = new FoodProperties.Builder().nutrition(3).saturationModifier(0.6f).alwaysEdible().effect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1), 1).build();

    public static void register() {
        FabsBnB.log("Setting up events...");
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
        EntityEvent.LIVING_DEATH.register((livingEntity, damageSource) -> {
            if (damageSource.is(DamageTypes.PLAYER_ATTACK)) {
                ItemStack stack = damageSource.getWeaponItem();
                if (stack == null) {
                    return EventResult.pass();
                }
                ModRegistries.CAPTURING_ENCHANT.handleEvent(livingEntity, stack);
            }
            return EventResult.pass();
        });
        BlockEvent.BREAK.register((level, blockPos, blockState, serverPlayer, intValue) -> {
            if (handleCustomMiningEnchantments(level, blockPos, blockState, serverPlayer)) {
                return EventResult.interruptTrue();
            }
            return EventResult.pass();
        });
        ItemStackEvent.CREATED.register(stack -> {
            if (!stack.has(DataComponents.FOOD)) {
                for (CustomFoodData foodData : CustomFoodDataLoader.INSTANCE.getDataMap().values()) {
                    ResourceLocation itemLocation = stack.getItem().arch$registryName();
                    if (itemLocation != null) {
                        if (itemLocation.toString().equals(foodData.location().toString())) {
                            FoodProperties.Builder foodProperties = new FoodProperties.Builder()
                                    .nutrition(foodData.nutrition())
                                    .saturationModifier(foodData.saturation());
                            if (foodData.alwaysEdible()) {
                                foodProperties.alwaysEdible();
                            }
                            for (CustomFoodData.MobEffectData mobEffectData : foodData.mobEffectInstances()) {
                                foodProperties.effect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.getHolder(mobEffectData.location()).orElseThrow(), mobEffectData.duration(), mobEffectData.power()), 1);
                            }
                            stack.set(DataComponents.FOOD, foodProperties.build());
                        }
                    }
                }
            }
        });
    }

    public static boolean handleCustomMiningEnchantments(Level level, BlockPos blockPos, BlockState blockState, ServerPlayer serverPlayer) {
        ItemStack stack = serverPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        return handleCustomMiningEnchantments(level, blockPos, blockState, serverPlayer, stack);
    }

    public static boolean handleCustomMiningEnchantments(Level level, BlockPos blockPos, BlockState blockState, ItemStack stack) {
        return handleCustomMiningEnchantments(level, blockPos, blockState, null, stack);
    }

    public static boolean handleCustomMiningEnchantments(Level level, BlockPos blockPos, BlockState blockState, ServerPlayer serverPlayer, ItemStack stack) {
        return ModRegistries.TREE_CHOPPER_ENCHANT.handleEvent(level, blockPos, blockState, serverPlayer, stack) || ModRegistries.VEIN_MINER_ENCHANT.handleEvent(level, blockPos, blockState, serverPlayer, stack);
    }
}
