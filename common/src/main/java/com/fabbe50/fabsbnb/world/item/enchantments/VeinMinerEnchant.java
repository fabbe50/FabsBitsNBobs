package com.fabbe50.fabsbnb.world.item.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class VeinMinerEnchant extends Enchantment {
    public VeinMinerEnchant(Rarity rarity, EquipmentSlot... equipmentSlots) {
        super(rarity, EnchantmentCategory.DIGGER, equipmentSlots);
    }

    @Override
    public int getMinCost(int i) {
        return 15 + (i - 1) * 9;
    }

    @Override
    public int getMaxCost(int i) {
        return super.getMinCost(i) + 50;
    }
}
