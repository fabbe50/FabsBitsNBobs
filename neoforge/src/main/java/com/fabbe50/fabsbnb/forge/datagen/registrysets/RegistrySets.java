package com.fabbe50.fabsbnb.forge.datagen.registrysets;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.forge.datagen.registrysets.sets.Enchantments;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class RegistrySets extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, Enchantments::bootstrap);

    public RegistrySets(PackOutput output, CompletableFuture<RegistrySetBuilder.PatchedRegistries> registries) {
        super(output, registries, Collections.singleton(FabsBnB.MOD_ID));
    }
}
