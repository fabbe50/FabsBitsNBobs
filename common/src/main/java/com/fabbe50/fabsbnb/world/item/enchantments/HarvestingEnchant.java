package com.fabbe50.fabsbnb.world.item.enchantments;

import com.fabbe50.fabsbnb.data.ToolMaterialScanRange;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.world.item.base.ModTieredItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

public record HarvestingEnchant(ResourceKey<Enchantment> enchantmentKey, TagKey<Item> supportedTools, TagKey<Item> primaryTools) implements IEnchantment {
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
                if (!blockState.is(BlockTags.CROPS)) {
                    return false;
                }
                CompoundTag compoundTag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                int scanRange = compoundTag.getInt("scanRange").orElse(1);
                Utilities.getBlocksInRadius(blockPos, scanRange)
                        .forEach(blockPos1 -> {
                            BlockState state = level.getBlockState(blockPos1);
                            if (state.getBlock() instanceof CropBlock cropBlock && cropBlock.isMaxAge(state)) {
                                List<ItemStack> stacks = state.getDrops(new LootParams.Builder((ServerLevel) level).withParameter(LootContextParams.TOOL, stack).withParameter(LootContextParams.ORIGIN, blockPos1.getCenter()));
                                for (ItemStack dropStack : stacks) {
                                    ItemEntity itemEntity = new ItemEntity(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), dropStack);
                                    level.addFreshEntity(itemEntity);
                                }
                                level.setBlockAndUpdate(blockPos1, cropBlock.getStateForAge(0));
                            }
                        });

            }
        }

        return false;
    }
}
