package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.tags.EnchantmentTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTags extends EnchantmentTagsProvider {
    public ModEnchantmentTags(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, completableFuture, FabsBnB.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(EnchantmentTags.IN_ENCHANTING_TABLE)
                .addOptional(ModRegistries.ORE_MINER.location())
                .addOptional(ModRegistries.TREE_CHOPPER.location())
                .addOptional(ModRegistries.LEAF_BREAKER.location())
                .addOptional(ModRegistries.CAPTURING.location());
    }
}
