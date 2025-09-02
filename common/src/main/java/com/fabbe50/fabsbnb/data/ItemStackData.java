package com.fabbe50.fabsbnb.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public record ItemStackData(Holder<Item> item, int amount) {
    public static final Codec<ItemStackData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Item.CODEC.fieldOf("item").forGetter(ItemStackData::item),
                    Codec.INT.fieldOf("amount").forGetter(ItemStackData::amount)
            ).apply(instance, ItemStackData::new));

    public static CompoundTag saveAllItems(CompoundTag compoundTag, NonNullList<ItemStackData> nonNullList) {
        List<ItemStackData> itemStackWithSlots = new ArrayList<>(nonNullList);
        compoundTag.store("Items", Codec.list(ItemStackData.CODEC), itemStackWithSlots);
        return compoundTag;
    }

    public static NonNullList<ItemStackData> loadAllItems(CompoundTag compoundTag) {
        NonNullList<ItemStackData> stackData = NonNullList.create();
        stackData.addAll(compoundTag.read("Items", Codec.list(CODEC)).orElse(List.of()));
        return stackData;
    }
}
