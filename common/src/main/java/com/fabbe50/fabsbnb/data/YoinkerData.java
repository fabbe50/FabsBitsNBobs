package com.fabbe50.fabsbnb.data;

import com.fabbe50.fabsbnb.Utilities;
import net.minecraft.Util;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.BlockStateData;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class YoinkerData {
    private static final String YOINKED_BLOCK_KEY = "yoinkBlock";
    private static final String YOINKED_BLOCK_DATA_KEY = "yoinkBlockData";

    public static void clearData(ItemStack yoinker) {
        setBlockState(yoinker, Blocks.AIR.defaultBlockState());
//        setBlockEntity(yoinker, CustomData.EMPTY);
        CompoundTag tag = yoinker.getOrCreateTag();
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
        CompoundTag tag = yoinker.getOrCreateTag();
        ResourceLocation location = blockState.getBlock().arch$registryName();
        if (location != null) {
            tag.putString(YOINKED_BLOCK_KEY, location.toString());
            CompoundTag blockData = NbtUtils.writeBlockState(blockState);
            tag.put(YOINKED_BLOCK_DATA_KEY, blockData);
            yoinker.setTag(tag);
        }
//        yoinker.set(DataComponents.YOINK_BLOCKSTATE.get(), blockState);
    }

    public static BlockState getBlockState(RegistryAccess registryAccess, ItemStack yoinker) {
        CompoundTag tag = yoinker.getOrCreateTag();
        Holder.Reference<Block> blockReference = Utilities.parseBlockReference(registryAccess, yoinker, YOINKED_BLOCK_KEY);
        if (blockReference != null) {
            if (tag.contains(YOINKED_BLOCK_DATA_KEY)) {
                return NbtUtils.readBlockState(Utilities.getBlockRegistryLookup(registryAccess), tag.getCompound(YOINKED_BLOCK_DATA_KEY));
            }
        }
        return Blocks.AIR.defaultBlockState();
//        return yoinker.getOrDefault(DataComponents.YOINK_BLOCKSTATE.get(), Blocks.AIR.defaultBlockState());
    }

    public static boolean hasData(ItemStack yoinker) {
        CompoundTag tag = yoinker.getOrCreateTag();
        return tag.contains(YOINKED_BLOCK_KEY);
    }

    public static void setBlockEntity(ItemStack yoinker, BlockEntity blockEntity) {
        CompoundTag tag = yoinker.getOrCreateTag();
        CompoundTag blockEntityData = blockEntity.saveWithFullMetadata();
        tag.put("blockEntity", blockEntityData);
        yoinker.setTag(tag);
//        setBlockEntity(yoinker, CustomData.of(blockEntity.saveCustomAndMetadata(provider)));
    }

    public static CompoundTag getBlockEntityData(ItemStack yoinker) {
        CompoundTag tag = yoinker.getOrCreateTag();
        if (tag.contains("blockEntity")) {
            return tag.getCompound("blockEntity");
        }
        return new CompoundTag();
//        return yoinker.getOrDefault(DataComponents.YOINK_BLOCKENTITY.get(), CustomData.EMPTY);
    }

    public static boolean hasBlockEntityData(ItemStack yoinker) {
        CompoundTag tag = yoinker.getOrCreateTag();
        return tag.contains("blockEntity");
    }

    public static void setBlockContainerData(ItemStack yoinker, Container container) {
        CompoundTag tag = ContainerHelper.saveAllItems(yoinker.getOrCreateTag(), getListOfItemsFromContainer(container));
        yoinker.setTag(tag);
        /*NonNullList<ItemStack> itemStacks = container.getItems();
        yoinker.set(DataComponents.YOINK_BLOCKCONTAINER.get(), ItemContainerContents.fromItems(itemStacks));*/
    }

    public static NonNullList<ItemStack> getListOfItemsFromContainer(Container container) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int i = 0; i < container.getContainerSize(); i++) {
            stacks.add(container.getItem(i));
        }
        return stacks;
    }

    public static void setContainerItems(Container container, ItemStack yoinker) {
        NonNullList<ItemStack> itemStacks = YoinkerData.getContainerItems(yoinker);
        for (int i = 0; i < container.getContainerSize(); i++) {
            container.setItem(i, itemStacks.get(i));
        }
    }

    public static NonNullList<ItemStack> getContainerItems(ItemStack yoinker) {
        NonNullList<ItemStack> containerItems = NonNullList.create();
        ContainerHelper.loadAllItems(yoinker.getOrCreateTag(), containerItems);
        return containerItems;
        /*ItemContainerContents containerContents = yoinker.getOrDefault(DataComponents.YOINK_BLOCKCONTAINER.get(), ItemContainerContents.EMPTY);
        NonNullList<ItemStack> itemStacks = NonNullList.create();
        containerContents.copyInto(itemStacks);
        return itemStacks;*/
    }
}
