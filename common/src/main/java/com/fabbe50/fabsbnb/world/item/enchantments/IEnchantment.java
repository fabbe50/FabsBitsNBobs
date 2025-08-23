package com.fabbe50.fabsbnb.world.item.enchantments;

import com.fabbe50.fabsbnb.FabsBnB;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EquipmentSlotGroup;
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

    default EnchantmentDefinition getEnchantmentDefinition(HolderGetter<Item> itemHolder) {
        return Enchantment.definition(itemHolder.getOrThrow(getSupportedItems()), itemHolder.getOrThrow(getPrimaryItems()), getWeight(), getMaxLevel(), getMinCost(), getMaxCost(), getAnvilCost(), getEquipmentSlotGroup());
    }

    default HolderSet<Enchantment> exclusiveWith(HolderGetter<Enchantment> enchantmentHolder) {
        return null;
    }

    int getWeight();
    int getMaxLevel();

    Enchantment.Cost getMinCost();
    Enchantment.Cost getMaxCost();
    int getAnvilCost();

    EquipmentSlotGroup getEquipmentSlotGroup();

    ResourceKey<Enchantment> getResourceKey();

    TagKey<Item> getSupportedItems();
    TagKey<Item> getPrimaryItems();

    boolean handleEvent(Object... objects);
}
