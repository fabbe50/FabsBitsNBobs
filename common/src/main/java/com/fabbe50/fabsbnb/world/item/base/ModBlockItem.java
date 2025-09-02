package com.fabbe50.fabsbnb.world.item.base;

import com.fabbe50.fabsbnb.world.block.interfaces.ITooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class ModBlockItem extends BlockItem {
    public ModBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        if (this.getBlock() instanceof ITooltip tooltipBlock) {
            tooltipBlock.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
        }
    }
}
