package com.fabbe50.fabsbnb.world.item.enchantments;

import com.fabbe50.fabsbnb.util.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class VeinMinerEnchant implements IEnchantment {
    private final ResourceKey<Enchantment> enchantmentKey;
    private final TagKey<Block> blockFilter;
    private final TagKey<Item> supportedTools;
    private final TagKey<Item> primaryTools;
    private final int miningLimit;
    private final boolean hasRequiredAttachments;
    private final TagKey<Block> requiredAttachments;
    private final boolean fuzzy;

    public VeinMinerEnchant(ResourceKey<Enchantment> enchantmentKey, TagKey<Block> blockFilter, TagKey<Item> supportedTools, TagKey<Item> primaryTools, int miningLimit) {
        this(enchantmentKey, blockFilter, supportedTools, primaryTools, miningLimit, null);
    }

    public VeinMinerEnchant(ResourceKey<Enchantment> enchantmentKey, TagKey<Block> blockFilter, TagKey<Item> supportedTools, TagKey<Item> primaryTools, int miningLimit, TagKey<Block> requiredAttachments) {
        this(enchantmentKey, blockFilter, supportedTools, primaryTools, miningLimit, requiredAttachments, false);
    }

    public VeinMinerEnchant(ResourceKey<Enchantment> enchantmentKey, TagKey<Block> blockFilter, TagKey<Item> supportedTools, TagKey<Item> primaryTools, int miningLimit, TagKey<Block> requiredAttachments, boolean fuzzy) {
        this.enchantmentKey = enchantmentKey;
        this.blockFilter = blockFilter;
        this.supportedTools = supportedTools;
        this.primaryTools = primaryTools;
        this.miningLimit = miningLimit;
        this.requiredAttachments = requiredAttachments;
        this.hasRequiredAttachments = this.requiredAttachments != null;
        this.fuzzy = fuzzy;
    }

    @Override
    public Enchantment.EnchantmentDefinition getEnchantmentDefinition(HolderGetter<Item> itemHolder) {
        return Enchantment.definition(itemHolder.getOrThrow(supportedTools), itemHolder.getOrThrow(primaryTools), 1, 1, Enchantment.constantCost(15), Enchantment.constantCost(65), 10, EquipmentSlotGroup.HAND);
    }

    public boolean handleEvent(Level level, BlockPos blockPos, BlockState blockState, ServerPlayer serverPlayer) {
        if (serverPlayer.isShiftKeyDown()) {
            return false;
        }
        ItemStack stack = serverPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.is(primaryTools) || stack.is(supportedTools)) {
            Holder<Enchantment> enchantmentHolder = Utilities.getHolder(level, enchantmentKey);
            if (EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack) > 0) {
                if (!blockState.is(blockFilter)) {
                    return false;
                }
                int range = 2;
                Set<BlockPos> found = new HashSet<>();
                Set<BlockPos> checked = new HashSet<>();
                Queue<BlockPos> toCheck = new LinkedList<>();
                AtomicBoolean valid = new AtomicBoolean(!this.hasRequiredAttachments);

                found.add(blockPos);
                toCheck.add(blockPos);

                while (!toCheck.isEmpty()) {
                    BlockPos pos = toCheck.poll();
                    if (!checked.add(pos)) {
                        continue;
                    }
                    Set<BlockPos> matched = BlockPos.betweenClosedStream(pos.offset(-range, -range, -range), pos.offset(range, range, range))
                            .filter(aPos -> {
                                BlockState state = level.getBlockState(aPos);
                                if (state.is(this.requiredAttachments)) {
                                    valid.set(true);
                                }
                                if (this.fuzzy) {
                                    return state.is(blockFilter);
                                } else {
                                    return state.is(blockState.getBlock());
                                }
                            })
                            .map(BlockPos::immutable)
                            .collect(Collectors.toSet());

                    for (BlockPos match : matched) {
                        if (found.size() < this.miningLimit) {
                            found.add(match);
                            if (!checked.contains(match)) {
                                toCheck.add(match);
                            }
                        } else {
                            if (valid.get()) {
                                breakBlocks(level, blockPos, found, serverPlayer, stack);
                                return true;
                            }
                        }
                    }
                }
                if (valid.get()) {
                    breakBlocks(level, blockPos, found, serverPlayer, stack);
                    return true;
                }
            }
        }
        return false;
    }

    private void breakBlocks(Level level, BlockPos dropPos, Set<BlockPos> positions, ServerPlayer player, ItemStack stack) {
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
                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
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
