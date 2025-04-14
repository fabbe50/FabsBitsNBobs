package com.fabbe50.fabsbnb.registries;

import com.fabbe50.fabsbnb.Platform;
import com.fabbe50.fabsbnb.world.block.interfaces.ILeftClickable;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRecipeUpdateEvent;
import dev.architectury.event.events.common.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

public class EventRegistry {
    public static void register() {
        InteractionEvent.INTERACT_ENTITY.register((player, entity, interactionHand) -> {
            ItemStack usedStack = player.getItemInHand(interactionHand);
            if (entity instanceof Cat cat && usedStack.is(Items.SHEARS)) {
                if (!player.getAbilities().instabuild) {
                    usedStack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(interactionHand));
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
                    mainHand.hurtAndBreak((int)(livingEntity.fallDistance / 2), livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                    return EventResult.interruptFalse();
                }
                if (offHand.is(ModRegistries.WHOOSH_WAND.get())) {
                    offHand.hurtAndBreak((int)(livingEntity.fallDistance / 2), livingEntity, livingEntity1 -> livingEntity1.broadcastBreakEvent(EquipmentSlot.OFFHAND));
                    return EventResult.interruptFalse();
                }
            }
            return EventResult.pass();
        });
        LifecycleEvent.SERVER_STARTING.register(minecraftServer -> {
            PotionBrewing.addMix(Potions.AWKWARD, ModRegistries.CAT_CLAW.get(), ModRegistries.FELINE_AURA_POTION_SHORT.get());
            PotionBrewing.addMix(ModRegistries.FELINE_AURA_POTION_SHORT.get(), Items.REDSTONE, ModRegistries.FELINE_AURA_POTION_LONG.get());
        });
        BlockEvent.BREAK.register((level, blockPos, blockState, serverPlayer, intValue) -> {
            ItemStack stack = serverPlayer.getItemInHand(InteractionHand.MAIN_HAND);
            if (stack.getItem() instanceof DiggerItem) {
                if (EnchantmentHelper.getEnchantments(stack).containsKey(ModRegistries.VEIN_MINER.get())) {
                    if (!blockState.is(Platform.getOresTag())) {
                        return EventResult.pass();
                    }
                    int range = 2;
                    Set<BlockPos> found = new HashSet<>();
                    Set<BlockPos> checked = new HashSet<>();
                    Queue<BlockPos> toCheck = new LinkedList<>();

                    found.add(blockPos);
                    toCheck.add(blockPos);

                    while (!toCheck.isEmpty()) {
                        BlockPos pos = toCheck.poll();
                        if (!checked.add(pos)) {
                            continue;
                        }
                        Set<BlockPos> matched = BlockPos.betweenClosedStream(pos.offset(-range, -range, -range), pos.offset(range, range, range))
                                .filter(aPos -> level.getBlockState(aPos).is(blockState.getBlock()))
                                .map(BlockPos::immutable)
                                .collect(Collectors.toSet());

                        for (BlockPos match : matched) {
                            if (found.size() < 256) {
                                found.add(match);
                                if (!checked.contains(match)) {
                                    toCheck.add(match);
                                }
                            } else {
                                breakBlocks(level, blockPos, found, serverPlayer, stack);
                                return EventResult.interruptTrue();
                            }
                        }
                    }
                    breakBlocks(level, blockPos, found, serverPlayer, stack);
                    return EventResult.interruptTrue();
                }
            }
            return EventResult.pass();
        });
    }

    private static void breakBlocks(Level level, BlockPos dropPos, Set<BlockPos> positions, ServerPlayer player, ItemStack stack) {
        List<ItemStack> drops = new ArrayList<>();
        for (BlockPos pos : positions) {
            BlockState state = level.getBlockState(pos);
            GameType type = player.getAbilities().instabuild ? GameType.CREATIVE : GameType.SURVIVAL;
            BlockEntity blockEntity = level.getBlockEntity(pos);
            Block block = state.getBlock();
            if (block instanceof GameMasterBlock && !player.canUseGameMasterBlocks()) {
                level.sendBlockUpdated(pos, state, state, 3);
                continue;
            }
            if (player.blockActionRestricted(level, pos, type)) {
                continue;
            }
            if (!player.getAbilities().instabuild) {
                drops.addAll(state.getDrops(new LootParams.Builder((ServerLevel) level).withParameter(LootContextParams.TOOL, stack).withParameter(LootContextParams.ORIGIN, pos.getCenter())));
                stack.hurtAndBreak(1, player, serverPlayer -> serverPlayer.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
            if (blockEntity != null) {
                level.removeBlockEntity(pos);
            }
            level.removeBlock(pos, false);
        }
        Vec3 newDropPos = dropPos.getCenter();
        for (ItemStack drop : drops) {
            ItemEntity itemEntity = new ItemEntity(level, newDropPos.x(), newDropPos.y(), newDropPos.z(), drop);
            itemEntity.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(itemEntity);
        }
    }
}
