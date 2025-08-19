package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTags extends ItemTagsProvider {
    public ModItemTags(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, CompletableFuture<TagLookup<Block>> completableFuture2, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, completableFuture2, FabsBnB.MOD_ID, existingFileHelper);
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

        tag(ModRegistries.IMMUNE_TO_CACTUS_DAMAGE)
                .add(Items.CACTUS)
                .addTag(ModRegistries.NETHERITE_ITEMS);
    }
}
