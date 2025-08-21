package com.fabbe50.fabsbnb.registries;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.world.item.enchantments.IEnchantment;
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
