package com.fabbe50.fabsbnb.world.item.enchantments;

import com.fabbe50.fabsbnb.data.ToolTierScanRange;
import com.fabbe50.fabsbnb.util.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record TillingEnchant(ResourceKey<Enchantment> enchantmentKey, TagKey<Item> supportedTools, TagKey<Item> primaryTools) implements IEnchantment {
    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public Enchantment.Cost getMinCost() {
        return Enchantment.dynamicCost(15, 0);
    }

    @Override
    public Enchantment.Cost getMaxCost() {
        return Enchantment.dynamicCost(65, 0);
    }

    @Override
    public int getAnvilCost() {
        return 4;
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public ResourceKey<Enchantment> getResourceKey() {
        return enchantmentKey;
    }

    @Override
    public TagKey<Item> getSupportedItems() {
        return supportedTools;
    }

    @Override
    public TagKey<Item> getPrimaryItems() {
        return primaryTools;
    }

    @Override
    public boolean handleEvent(Object... objects) {
        Level level = (Level) objects[0];
        BlockPos blockPos = (BlockPos) objects[1];
        ItemStack stack = (ItemStack) objects[2];

        BlockState blockState = level.getBlockState(blockPos);

        if (stack.is(primaryTools) || stack.is(supportedTools)) {
            Holder<Enchantment> enchantmentHolder = Utilities.getHolder(level, enchantmentKey);
            if (EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack) > 0) {
                if (!blockState.is(BlockTags.DIRT)) {
                    return false;
                }
                if (stack.getItem() instanceof TieredItem tieredItem) {
                    Utilities.getBlocksInRadius(blockPos, ToolTierScanRange.getScanRangeFromToolTier((Tiers) tieredItem.getTier()).getScanRange())
                            .forEach(blockPos1 -> {
                                BlockState state = level.getBlockState(blockPos1);
                                if (state.is(BlockTags.DIRT) && level.getBlockState(blockPos1.above()).isAir()) {
                                    level.setBlockAndUpdate(blockPos1, Blocks.FARMLAND.defaultBlockState());
                                }
                            });
                }
            }
        }

        return false;
    }
}
