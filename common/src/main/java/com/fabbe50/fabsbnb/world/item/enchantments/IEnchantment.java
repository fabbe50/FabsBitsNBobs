package com.fabbe50.fabsbnb.world.item.enchantments;

import com.fabbe50.fabsbnb.FabsBnB;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.EnchantmentDefinition;
import net.minecraft.world.level.block.Block;

public interface IEnchantment {
    default Enchantment getEnchantment(String location, HolderGetter<DamageType> damageTypeHolder, HolderGetter<Enchantment> enchantmentHolder, HolderGetter<Item> itemHolder, HolderGetter<Block> blockHolder) {
        HolderSet<Enchantment> exclusiveWith = exclusiveWith(enchantmentHolder);
        if (exclusiveWith == null) {
            return Enchantment.enchantment(getEnchantmentDefinition(itemHolder)).build(FabsBnB.location(location));
        }
        return Enchantment.enchantment(getEnchantmentDefinition(itemHolder)).exclusiveWith(exclusiveWith(enchantmentHolder)).build(FabsBnB.location(location));
    }

    EnchantmentDefinition getEnchantmentDefinition(HolderGetter<Item> itemHolder);

    default HolderSet<Enchantment> exclusiveWith(HolderGetter<Enchantment> enchantmentHolder) {
        return null;
    }
}
