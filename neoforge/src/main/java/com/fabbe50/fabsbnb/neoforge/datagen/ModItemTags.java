package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTags extends ItemTagsProvider {
    public ModItemTags(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(arg, completableFuture, FabsBnB.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ModRegistries.BUILDING_WANDS)
                .add(ModRegistries.WOODEN_BUILDING_WAND.get())
                .add(ModRegistries.STONE_BUILDING_WAND.get())
                .add(ModRegistries.IRON_BUILDING_WAND.get())
                .add(ModRegistries.GOLD_BUILDING_WAND.get())
                .add(ModRegistries.DIAMOND_BUILDING_WAND.get())
                .add(ModRegistries.NETHERITE_BUILDING_WAND.get());

        tag(ModRegistries.NETHERITE_ITEMS)
                .add(
                        Items.NETHERITE_INGOT,
                        Items.NETHERITE_BLOCK,
                        Items.NETHERITE_HELMET,
                        Items.NETHERITE_CHESTPLATE,
                        Items.NETHERITE_LEGGINGS,
                        Items.NETHERITE_BOOTS,
                        Items.NETHERITE_SWORD,
                        Items.NETHERITE_PICKAXE,
                        Items.NETHERITE_SHOVEL,
                        Items.NETHERITE_AXE,
                        Items.NETHERITE_HOE,
                        Items.NETHERITE_SCRAP
                );

        tag(TagKey.create(Registries.ITEM, FabsBnB.location("c", "foods")))
                .add(Items.CACTUS)
                .add(Items.GLISTERING_MELON_SLICE);

        tag(ModRegistries.DIGGING_TOOLS)
                .addTag(ItemTags.SHOVELS)
                .addTag(ItemTags.PICKAXES);

        tag(ModRegistries.IMMUNE_TO_CACTUS_DAMAGE)
                .add(Items.CACTUS)
                .addTag(ModRegistries.NETHERITE_ITEMS);

        tag(ModRegistries.VAULT_UNLOCKERS)
                .add(Items.TRIAL_KEY)
                .add(Items.OMINOUS_TRIAL_KEY)
                .add(Items.DIAMOND_BLOCK);

        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(ModRegistries.WHOOSH_WAND.get())
                .add(ModRegistries.CHOCOLATE_NECKLACE.get());

        tag(Tags.Items.POTIONS_BOTTLE)
                .add(ModRegistries.OWN_POTION_ITEM.get())
                .add(ModRegistries.OWN_SPLASH_POTION_ITEM.get())
                .add(ModRegistries.OWN_LINGERING_POTION_ITEM.get());

        tag(ModRegistries.WAND_OF_HOLDING_ACCEPTS)
                .add(Items.STONE);
    }
}
