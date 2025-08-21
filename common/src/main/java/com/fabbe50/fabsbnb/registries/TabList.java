package com.fabbe50.fabsbnb.registries;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.world.item.enchantments.IEnchantment;
import com.mojang.datafixers.util.Pair;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TabList<R, T extends TabList.TabReg<R>> {
    private final List<Pair<String, Item>> potionTypes = List.of(Pair.of("potion", Items.POTION), Pair.of("splash_potion", Items.SPLASH_POTION), Pair.of("lingering_potion", Items.LINGERING_POTION), Pair.of("tipped_arrow", Items.TIPPED_ARROW));

    public void registerTab(T event, HolderLookup.Provider provider) {
        FabsBnB.log("Setting up creative tab...");
        for (RegistrySupplier<Item> item : ModRegistries.ITEM_LIST) {
            ItemStack stack = new ItemStack(item.get());
            if (!stack.isEmpty()) {
                event.accept(stack);
            }
        }
        if (provider != null) {
            HolderLookup.RegistryLookup<Enchantment> enchantmentLookup = provider.lookup(Registries.ENCHANTMENT).orElse(null);
            if (enchantmentLookup != null) {
                for (IEnchantment enchantment : ModRegistries.ENCHANTMENT_LIST) {
                    Holder.Reference<Enchantment> enchantmentReference = enchantmentLookup.get(enchantment.getResourceKey()).orElse(null);
                    if (enchantmentReference != null) {
                        for (int i = 1; i <= enchantment.getMaxLevel(); i++) {
                            event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantmentReference, i)));
                        }
                    }
                }
            }
        }
        Map<String, List<ItemStack>> potions = new LinkedHashMap<>();
        for (RegistrySupplier<Potion> potion : ModRegistries.POTION_LIST) {
            for (Pair<String, Item> pair : potionTypes) {
                List<ItemStack> temp = potions.getOrDefault(pair.getFirst(), new ArrayList<>());
                temp.add(PotionContents.createItemStack(pair.getSecond(), ModRegistries.getPotionReference(potion)));
                potions.put(pair.getFirst(), temp);
            }
        }
        for (String entry : potions.keySet()) {
            for (ItemStack potionStack : potions.get(entry)) {
                event.accept(potionStack);
            }
        }
    }

    public abstract static class TabReg<B> implements ITabRegistration<B> {
        protected B regHandler;

        public TabReg(B regHandler) {
            this.regHandler = regHandler;
        }

        @Override
        public B getRegHandler() {
            return regHandler;
        }
    }

    private interface ITabRegistration<R> {
        R getRegHandler();

        void accept(ItemStack stack);
    }
}
