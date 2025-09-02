package com.fabbe50.fabsbnb.world.item;

import com.fabbe50.fabsbnb.data.ItemStackData;
import com.fabbe50.fabsbnb.world.item.base.ModItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class HolderWandItem extends ModItem {
    public HolderWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        CompoundTag compoundTag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        NonNullList<ItemStackData> itemStackData = ItemStackData.loadAllItems(compoundTag);
        for (ItemStackData data : itemStackData) {
            consumer.accept(Component.literal(data.amount() + "x " + data.item().getRegisteredName()));
        }
    }
}
