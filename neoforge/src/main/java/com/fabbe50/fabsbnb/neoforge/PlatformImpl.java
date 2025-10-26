package com.fabbe50.fabsbnb.neoforge;

import com.fabbe50.fabsbnb.data.DataFixer;
import com.fabbe50.fabsbnb.neoforge.integration.Curios;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.IRegistryExtension;

public class PlatformImpl {
    public static TagKey<Block> getOresTag() {
        return Tags.Blocks.ORES;
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static boolean isWearingNecklace(LivingEntity entity) {
        return Curios.isWearingCurio(entity, ModRegistries.CHOCOLATE_NECKLACE.get());
    }

    public static void registerStarter(PotionBrewing.Builder builder, Item ingredient, Holder<Potion> output) {
        builder.addStartMix(ingredient, output);
    }

    public static void registerStarter(PotionBrewing.Builder builder, Item ingredient, Item output) {
        builder.addContainerRecipe(PotionContents.createItemStack(Items.POTION, Potions.AWKWARD).getItem(), ingredient, output);
        builder.addMix(Potions.WATER, ingredient, Potions.MUNDANE);
    }

    public static void registerPotion(PotionBrewing.Builder builder, Holder<Potion> input, Item ingredient, Holder<Potion> output) {
        builder.addMix(input, ingredient, output);
    }

    public static void registerPotion(PotionBrewing.Builder builder, Item input, Item ingredient, Item output) {
        builder.addContainerRecipe(input, ingredient, output);
    }

    public static void dataFix() {
        var blockReg = (IRegistryExtension<Block>) BuiltInRegistries.BLOCK;
        var itemReg = (IRegistryExtension<Item>) BuiltInRegistries.ITEM;

        DataFixer.BLOCK_FIXER.forEach(blockReg::addAlias);
        DataFixer.ITEM_FIXER.forEach(itemReg::addAlias);
    }
}
