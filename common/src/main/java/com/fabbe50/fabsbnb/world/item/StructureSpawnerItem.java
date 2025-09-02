package com.fabbe50.fabsbnb.world.item;

import com.fabbe50.fabsbnb.world.item.base.ModItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class StructureSpawnerItem extends ModItem {
    public StructureSpawnerItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        return super.useOn(useOnContext);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
    }
}
