package com.fabbe50.fabsbnb.data;

import com.fabbe50.fabsbnb.Utilities;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class YoinkerData {
    private static final String YOINKED_BLOCK_KEY = "yoinkBlock";
    private static final String YOINKED_BLOCK_DATA_KEY = "yoinkBlockData";

    public static void clearData(ItemStack yoinker) {
        setBlockState(yoinker, Blocks.AIR.defaultBlockState());
        CompoundTag tag = getCompoundTag(yoinker);
        if (tag.contains(YOINKED_BLOCK_KEY)) {
            tag.remove(YOINKED_BLOCK_KEY);
        }
        if (tag.contains("Items")) {
            tag.remove("Items");
        }
        if (tag.contains("blockEntity")) {
            tag.remove("blockEntity");
        }
    }

    public static void setBlockState(ItemStack yoinker, BlockState blockState) {
        CompoundTag tag = getCompoundTag(yoinker);
        ResourceLocation location = blockState.getBlock().arch$registryName();
        if (location != null) {
            tag.putString(YOINKED_BLOCK_KEY, location.toString());
            CompoundTag blockData = NbtUtils.writeBlockState(blockState);
            tag.put(YOINKED_BLOCK_DATA_KEY, blockData);
            saveCompoundTag(yoinker, tag);
        }
    }

    public static BlockState getBlockState(HolderLookup.Provider provider, ItemStack yoinker) {
        CompoundTag tag = yoinker.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        Holder.Reference<Block> blockReference = Utilities.parseBlockReference(provider, yoinker, YOINKED_BLOCK_KEY);
        if (blockReference != null) {
            if (tag.contains(YOINKED_BLOCK_DATA_KEY)) {
                return NbtUtils.readBlockState(Utilities.getBlockRegistryLookup(provider), tag.getCompound(YOINKED_BLOCK_DATA_KEY));
            }
        }
        return Blocks.AIR.defaultBlockState();
    }

    public static boolean hasData(ItemStack yoinker) {
        CompoundTag tag = getCompoundTag(yoinker);
        return tag.contains(YOINKED_BLOCK_KEY);
    }

    public static void setBlockEntity(HolderLookup.Provider provider, ItemStack yoinker, BlockEntity blockEntity) {
        CompoundTag tag = getCompoundTag(yoinker);
        CompoundTag blockEntityData = blockEntity.saveWithFullMetadata(provider);
        tag.put("blockEntity", blockEntityData);
        saveCompoundTag(yoinker, tag);
    }

    public static CompoundTag getBlockEntityData(ItemStack yoinker) {
        CompoundTag tag = getCompoundTag(yoinker);
        if (tag.contains("blockEntity")) {
            return tag.getCompound("blockEntity");
        }
        return new CompoundTag();
    }

    public static boolean hasBlockEntityData(ItemStack yoinker) {
        CompoundTag tag = getCompoundTag(yoinker);
        return tag.contains("blockEntity");
    }

    public static void setBlockContainerData(HolderLookup.Provider provider, ItemStack yoinker, Container container) {
        CompoundTag tag = ContainerHelper.saveAllItems(getCompoundTag(yoinker), getListOfItemsFromContainer(container), provider);
        saveCompoundTag(yoinker, tag);
    }

    public static NonNullList<ItemStack> getListOfItemsFromContainer(Container container) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int i = 0; i < container.getContainerSize(); i++) {
            stacks.add(container.getItem(i));
        }
        return stacks;
    }

    public static void setContainerItems(HolderLookup.Provider provider, Container container, ItemStack yoinker) {
        NonNullList<ItemStack> itemStacks = YoinkerData.getContainerItems(provider, yoinker);
        for (int i = 0; i < container.getContainerSize(); i++) {
            container.setItem(i, itemStacks.get(i));
        }
    }

    public static NonNullList<ItemStack> getContainerItems(HolderLookup.Provider provider, ItemStack yoinker) {
        NonNullList<ItemStack> containerItems = NonNullList.create();
        ContainerHelper.loadAllItems(getCompoundTag(yoinker), containerItems, provider);
        return containerItems;
    }

    private static CompoundTag getCompoundTag(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    private static void saveCompoundTag(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
}
