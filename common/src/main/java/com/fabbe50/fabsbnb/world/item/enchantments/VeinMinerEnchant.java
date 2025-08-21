package com.fabbe50.fabsbnb.world.item.enchantments;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.util.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
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
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class VeinMinerEnchant implements IEnchantment {
    private final ResourceKey<Enchantment> enchantmentKey;
    private final TagKey<Block> blockFilter;
    private final TagKey<Item> supportedTools;
    private final TagKey<Item> primaryTools;
    private final int miningLimit;
    private final int scanRange;
    private final boolean hasRequiredAttachments;
    private final TagKey<Block> requiredAttachments;
    private final boolean fuzzy;
    private final boolean breakAttachments;

    public VeinMinerEnchant(ResourceKey<Enchantment> enchantmentKey, TagKey<Block> blockFilter, TagKey<Item> supportedTools, TagKey<Item> primaryTools, int miningLimit, int scanRange) {
        this(enchantmentKey, blockFilter, supportedTools, primaryTools, miningLimit, scanRange, null);
    }

    public VeinMinerEnchant(ResourceKey<Enchantment> enchantmentKey, TagKey<Block> blockFilter, TagKey<Item> supportedTools, TagKey<Item> primaryTools, int miningLimit, int scanRange, TagKey<Block> requiredAttachments) {
        this(enchantmentKey, blockFilter, supportedTools, primaryTools, miningLimit, scanRange, requiredAttachments, false);
    }

    public VeinMinerEnchant(ResourceKey<Enchantment> enchantmentKey, TagKey<Block> blockFilter, TagKey<Item> supportedTools, TagKey<Item> primaryTools, int miningLimit, int scanRange, TagKey<Block> requiredAttachments, boolean fuzzy) {
        this(enchantmentKey, blockFilter, supportedTools, primaryTools, miningLimit, scanRange, requiredAttachments, fuzzy, false);
    }

    public VeinMinerEnchant(ResourceKey<Enchantment> enchantmentKey, TagKey<Block> blockFilter, TagKey<Item> supportedTools, TagKey<Item> primaryTools, int miningLimit, int scanRange, TagKey<Block> requiredAttachments, boolean fuzzy, boolean breakAttachments) {
        this.enchantmentKey = enchantmentKey;
        this.blockFilter = blockFilter;
        this.supportedTools = supportedTools;
        this.primaryTools = primaryTools;
        this.miningLimit = miningLimit;
        this.scanRange = scanRange;
        this.requiredAttachments = requiredAttachments;
        this.hasRequiredAttachments = this.requiredAttachments != null;
        this.fuzzy = fuzzy;
        this.breakAttachments = breakAttachments;
    }

    @Override
    public Enchantment.EnchantmentDefinition getEnchantmentDefinition(HolderGetter<Item> itemHolder) {
        return Enchantment.definition(itemHolder.getOrThrow(supportedTools), itemHolder.getOrThrow(primaryTools), 1, 1, Enchantment.constantCost(15), Enchantment.constantCost(65), 10, EquipmentSlotGroup.HAND);
    }

    public boolean handleEvent(Level level, BlockPos blockPos, BlockState blockState, @Nullable ServerPlayer serverPlayer, ItemStack stack) {
        FabsBnB.debug("Running Vein-miner of type: " + enchantmentKey);
        if (serverPlayer != null) {
            if (serverPlayer.isShiftKeyDown()) {
                FabsBnB.debug("Player is holding shift key, aborting event.");
                return false;
            }
        }
        if (stack.is(primaryTools) || stack.is(supportedTools)) {
            Holder<Enchantment> enchantmentHolder = Utilities.getHolder(level, enchantmentKey);
            if (EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack) > 0) {
                if (!blockState.is(blockFilter)) {
                    FabsBnB.debug("Block isn't on vein-miner whitelist. Aborting...");
                    return false;
                }
                int range = scanRange;
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
                                if (hasRequiredAttachments) {
                                    if (state.is(this.requiredAttachments)) {
                                        valid.set(true);
                                        if (this.breakAttachments) {
                                            return true;
                                        }
                                    }
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
                            FabsBnB.debug("Reached vein-mining limit.");
                            if (valid.get()) {
                                FabsBnB.debug("Vein-mining conditions valid. Attempting to break blocks...");
                                breakBlocks(level, blockPos, found, serverPlayer, stack);
                                return true;
                            }
                        }
                    }
                }
                if (valid.get()) {
                    FabsBnB.debug("Vein-mining conditions valid. Attempting to break blocks...");
                    breakBlocks(level, blockPos, found, serverPlayer, stack);
                    return true;
                }
            }
        }
        FabsBnB.debug("Incorrect tool for vein-mining. Aborting...");
        return false;
    }

    private void breakBlocks(Level level, BlockPos dropPos, Set<BlockPos> positions, ServerPlayer player, ItemStack stack) {
        List<ItemStack> drops = new ArrayList<>();
        for (BlockPos pos : positions) {
            BlockState state = level.getBlockState(pos);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            Block block = state.getBlock();
            if (checkGameMasterCondition(block, player)) {
                level.sendBlockUpdated(pos, state, state, 3);
                continue;
            }
            if (checkGameTypeCondition(player, level, pos)) {
                continue;
            }
            if (!isPlayerInstaBuild(player)) {
                drops.addAll(state.getDrops(new LootParams.Builder((ServerLevel) level).withParameter(LootContextParams.TOOL, stack).withParameter(LootContextParams.ORIGIN, pos.getCenter())));
                Utilities.hurtItem(1, (ServerLevel) level, stack, pos);
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

    private boolean checkGameMasterCondition(Block block, ServerPlayer player) {
        if (block instanceof GameMasterBlock) {
            if (player == null) {
                return false;
            }
            return !player.canUseGameMasterBlocks();
        }
        return false;
    }

    private boolean checkGameTypeCondition(ServerPlayer player, Level level, BlockPos pos) {
        if (player == null) {
            return false;
        }
        GameType type = isPlayerInstaBuild(player) ? GameType.CREATIVE : GameType.SURVIVAL;
        return player.blockActionRestricted(level, pos, type);
    }

    private boolean isPlayerInstaBuild(ServerPlayer player) {
        if (player == null) {
            return false;
        }
        return player.getAbilities().instabuild;
    }
}
