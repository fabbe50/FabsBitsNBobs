package com.fabbe50.fabsbnb.data;

import com.fabbe50.fabsbnb.util.Utilities;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueInputContextHelper;

import java.util.ArrayList;
import java.util.List;

public class YoinkerData {
    private static final String YOINKED_BLOCK_KEY = "yoinkBlock";
    private static final String YOINKED_BLOCK_DATA_KEY = "yoinkBlockData";

    public static void clearData(ItemStack yoinker) {
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
        saveCompoundTag(yoinker, tag);
        setBlockState(yoinker, Blocks.AIR.defaultBlockState());
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
            CompoundTag compoundTag = tag.getCompound(YOINKED_BLOCK_DATA_KEY).orElse(null);
            if (compoundTag != null) {
                return NbtUtils.readBlockState(Utilities.getBlockRegistryLookup(provider), compoundTag);
            }
        }
        return Blocks.AIR.defaultBlockState();
    }

    public static boolean doesYoinkerContainBlock(ItemStack yoinker) {
        CompoundTag tag = getCompoundTag(yoinker);
        String blockKey = tag.getString(YOINKED_BLOCK_KEY).orElse("minecraft:air");
        return !blockKey.equals("minecraft:air");
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
        return tag.getCompound("blockEntity").orElse(new CompoundTag());
    }

    public static boolean hasBlockEntityData(ItemStack yoinker) {
        CompoundTag tag = getCompoundTag(yoinker);
        return tag.contains("blockEntity");
    }

    public static void setBlockContainerData(ItemStack yoinker, Container container) {
        CompoundTag tag = saveAllItems(getCompoundTag(yoinker), getListOfItemsFromContainer(container));
        saveCompoundTag(yoinker, tag);
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
        loadAllItems(getCompoundTag(yoinker), containerItems);
        return containerItems;
    }

    public static CompoundTag saveAllItems(CompoundTag compoundTag, NonNullList<ItemStack> nonNullList) {
        List<ItemStackWithSlot> itemStackWithSlots = new ArrayList<>();
        for (int i = 0; i < nonNullList.size(); i++) {
            ItemStack stack = nonNullList.get(i);
            itemStackWithSlots.add(new ItemStackWithSlot(i, stack));
        }
        compoundTag.store("Items", Codec.list(ItemStackWithSlot.CODEC), itemStackWithSlots);
        return compoundTag;
    }

    public static void loadAllItems(CompoundTag compoundTag, NonNullList<ItemStack> nonNullList) {
        for(ItemStackWithSlot itemStackWithSlot : compoundTag.read("Items", Codec.list(ItemStackWithSlot.CODEC)).orElse(List.of())) {
            if (itemStackWithSlot.isValidInContainer(nonNullList.size())) {
                nonNullList.set(itemStackWithSlot.slot(), itemStackWithSlot.stack());
            }
        }

    }

    private static ValueInput getValueInput(HolderLookup.Provider provider) {
        return new ValueInputContextHelper(provider, NbtOps.INSTANCE).empty();
    }

    private static CompoundTag getCompoundTag(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    private static void saveCompoundTag(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
}
