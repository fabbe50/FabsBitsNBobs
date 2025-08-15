package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
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
    }
}
