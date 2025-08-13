package com.fabbe50.fabsbnb.neoforge.datagen.registrysets.sets;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public class Enchantments {
    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<DamageType> damageTypeHolder = context.lookup(Registries.DAMAGE_TYPE);
        HolderGetter<Enchantment> enchantmentHolder = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> itemHolder = context.lookup(Registries.ITEM);
        HolderGetter<Block> blockHolder = context.lookup(Registries.BLOCK);

        context.register(ModRegistries.VEIN_MINER, ModRegistries.VEIN_MINER_ENCHANT.getEnchantment("vein_miner", damageTypeHolder, enchantmentHolder, itemHolder, blockHolder));
        context.register(ModRegistries.TREE_CHOPPER, ModRegistries.TREE_CHOPPER_ENCHANT.getEnchantment("tree_chopper", damageTypeHolder, enchantmentHolder, itemHolder, blockHolder));
    }
}
