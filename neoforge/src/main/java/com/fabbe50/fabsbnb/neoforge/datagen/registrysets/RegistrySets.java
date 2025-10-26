package com.fabbe50.fabsbnb.neoforge.datagen.registrysets;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.neoforge.datagen.registrysets.sets.CauldronConversions;
import com.fabbe50.fabsbnb.neoforge.datagen.registrysets.sets.Enchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class RegistrySets extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, Enchantments::bootstrap);

    public RegistrySets(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Collections.singleton(FabsBnB.MOD_ID));
    }

    public static HolderLookup.Provider createLookup() {
        RegistryAccess.Frozen frozenRegistryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        return BUILDER.build(frozenRegistryAccess);
    }
}
