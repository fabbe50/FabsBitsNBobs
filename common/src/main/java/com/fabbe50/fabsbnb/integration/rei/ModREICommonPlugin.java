package com.fabbe50.fabsbnb.integration.rei;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.api.common.registry.display.ServerDisplayRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Objects;
import java.util.function.Function;

public class ModREICommonPlugin implements REICommonPlugin {
    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
        Function<ItemStack, ItemEnchantments> enchantmentTag = stack -> {
            if (stack.has(DataComponents.STORED_ENCHANTMENTS)) {
                return stack.get(DataComponents.STORED_ENCHANTMENTS);
            }
            return stack.get(DataComponents.ENCHANTMENTS);
        };
        registry.register((context, stack) -> Objects.hashCode(enchantmentTag.apply(stack)), ModRegistries.EXT_ENCHANTED_BOOK.get());
        registry.registerComponents(ModRegistries.OWN_POTION_ITEM.get());
        registry.registerComponents(ModRegistries.OWN_SPLASH_POTION_ITEM.get());
        registry.registerComponents(ModRegistries.OWN_LINGERING_POTION_ITEM.get());
        registry.registerComponents(ModRegistries.OWN_TIPPED_ARROW_ITEM.get());
    }

    @Override
    public void registerDisplays(ServerDisplayRegistry registry) {
        new OwnTippedArrowRecipeFiller().registerDisplays(registry);
    }
}
