package com.fabbe50.fabsbnb.registries;

import com.fabbe50.fabsbnb.FabsBnB;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class TabList<R, T extends TabList.TabReg<R>> {
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
                Holder.Reference<Enchantment> ore_miner_holder = enchantmentLookup.get(ModRegistries.VEIN_MINER).orElse(null);
                Holder.Reference<Enchantment> tree_chopper_holder = enchantmentLookup.get(ModRegistries.TREE_CHOPPER).orElse(null);
                Holder.Reference<Enchantment> leaf_breaker_holder = enchantmentLookup.get(ModRegistries.LEAF_BREAKER).orElse(null);
                Holder.Reference<Enchantment> capturing_holder = enchantmentLookup.get(ModRegistries.CAPTURING).orElse(null);
                if (ore_miner_holder != null) {
                    event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(ore_miner_holder, 1)));
                }
                if (tree_chopper_holder != null) {
                    event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(tree_chopper_holder, 1)));
                }
                if (leaf_breaker_holder != null) {
                    event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(leaf_breaker_holder, 1)));
                }
                if (capturing_holder != null) {
                    event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(capturing_holder, 1)));
                    event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(capturing_holder, 2)));
                    event.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(capturing_holder, 3)));
                }
            }
        }
        event.accept(PotionContents.createItemStack(Items.POTION, ModRegistries.getPotionReference(ModRegistries.FELINE_AURA_POTION_SHORT)));
        event.accept(PotionContents.createItemStack(Items.POTION, ModRegistries.getPotionReference(ModRegistries.FELINE_AURA_POTION_LONG)));
        event.accept(PotionContents.createItemStack(Items.SPLASH_POTION, ModRegistries.getPotionReference(ModRegistries.FELINE_AURA_POTION_SHORT)));
        event.accept(PotionContents.createItemStack(Items.SPLASH_POTION, ModRegistries.getPotionReference(ModRegistries.FELINE_AURA_POTION_LONG)));
        event.accept(PotionContents.createItemStack(Items.LINGERING_POTION, ModRegistries.getPotionReference(ModRegistries.FELINE_AURA_POTION_SHORT)));
        event.accept(PotionContents.createItemStack(Items.LINGERING_POTION, ModRegistries.getPotionReference(ModRegistries.FELINE_AURA_POTION_LONG)));
        event.accept(PotionContents.createItemStack(Items.TIPPED_ARROW, ModRegistries.getPotionReference(ModRegistries.FELINE_AURA_POTION_SHORT)));
        event.accept(PotionContents.createItemStack(Items.TIPPED_ARROW, ModRegistries.getPotionReference(ModRegistries.FELINE_AURA_POTION_LONG)));
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
