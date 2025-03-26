package com.fabbe50.fabsbnb;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class Utilities {
    public static int getRadiusFromTier(Tier tier) {
        if (tier.equals(Tiers.WOOD)) {
            return ModConfig.INSTANCE.woodenBuildingWandRadius;
        } else if (tier.equals(Tiers.STONE)) {
            return ModConfig.INSTANCE.stoneBuildingWandRadius;
        } else if (tier.equals(Tiers.IRON)) {
            return ModConfig.INSTANCE.ironBuildingWandRadius;
        } else if (tier.equals(Tiers.GOLD)) {
            return ModConfig.INSTANCE.goldBuildingWandRadius;
        } else if (tier.equals(Tiers.DIAMOND)) {
            return ModConfig.INSTANCE.diamondBuildingWandRadius;
        } else if (tier.equals(Tiers.NETHERITE)) {
            return ModConfig.INSTANCE.netheriteBuildingWandRadius;
        } else {
            return 1;
        }
    }

    public static int square(int value) {
        return value * value;
    }

    /**
     * @param registryAccess Registry Access
     * @param stack The itemstack to parse the block from.
     * @param key The key the block is saved under.
     * @return Returns a reference of the block in the registry, or null if the block doesn't exist or is invalid.
     */
    public static Holder.Reference<Block> parseBlockReference(RegistryAccess registryAccess, ItemStack stack, String key) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains(key)) {
            String sLocation = tag.getString(key);
            ResourceLocation location = ResourceLocation.tryParse(sLocation);
            if (location != null) {
                return Optional.of(location)
                        .map(location1 -> ResourceKey.create(Registries.BLOCK, location1))
                        .flatMap(resourceKey -> registryAccess.registryOrThrow(Registries.BLOCK).getHolder(resourceKey))
                        .orElse(null);
            }
        }
        return null;
    }

    public static HolderLookup<Block> getBlockRegistryLookup(RegistryAccess registryAccess) {
        return registryAccess.lookup(Registries.BLOCK).orElseThrow();
    }

    public static ToIntFunction<BlockState> litBlockEmission(int i) {
        return (blockState) -> (Boolean)blockState.getValue(BlockStateProperties.LIT) ? i : 0;
    }
}
