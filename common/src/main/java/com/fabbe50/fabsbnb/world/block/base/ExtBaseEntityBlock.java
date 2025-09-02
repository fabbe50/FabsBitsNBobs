package com.fabbe50.fabsbnb.world.block.base;

import com.fabbe50.fabsbnb.util.LangUtils;
import com.fabbe50.fabsbnb.world.block.interfaces.IDropSelf;
import com.fabbe50.fabsbnb.world.block.interfaces.ITooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.BaseEntityBlock;

import java.util.function.Consumer;

public abstract class ExtBaseEntityBlock extends BaseEntityBlock implements IDropSelf, ITooltip {
    private final int tooltipLines;

    public ExtBaseEntityBlock(Properties properties) {
        this(0, properties);
    }

    public ExtBaseEntityBlock(int tooltipLines, Properties properties) {
        super(properties);
        this.tooltipLines = tooltipLines;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        if (tooltipLines == 1) {
            consumer.accept(LangUtils.getDescription(this));
        } else if (tooltipLines > 1) {
            for (Component component : LangUtils.getDescription(this, tooltipLines)) {
                consumer.accept(component);
            }
        }
    }
}
