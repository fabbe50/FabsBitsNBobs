package com.fabbe50.fabsbnb.neoforge.datagen.registrysets.sets;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.data.CauldronConversionData;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

public class CauldronConversions extends JsonCodecProvider<CauldronConversionData> {
    public CauldronConversions(PackOutput output, PackOutput.Target target, Codec<CauldronConversionData> codec, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, target, FabsBnB.location("cauldron_conversion").getPath(), codec, lookupProvider, FabsBnB.MOD_ID);
    }

    @Override
    protected void gather() {
        this.unconditional(FabsBnB.location("test_data"), new CauldronConversionData(ModRegistries.ITEMS.getId(Items.STICK), ModRegistries.ITEMS.getId(Items.DIAMOND)));
    }
}
