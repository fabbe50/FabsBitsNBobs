package com.fabbe50.fabsbnb;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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

    public static int getTotalExperienceForLevel(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        } else if (level <= 31) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            long totalXp = (long) (4.5 * level * level - 162.5 * level + 2220);
            return (int) Math.min(totalXp, Integer.MAX_VALUE);
        }
    }

    public static int getLevelFromTotalExperience(int totalExp) {
        if (totalExp < getTotalExperienceForLevel(16)) {
            return (int) Math.floor((-6 + Math.sqrt(36 + 4 * totalExp)) / 2);
        } else if (totalExp < getTotalExperienceForLevel(31)) {
            return (int) Math.floor((40.5 + Math.sqrt(-40.5 * -40.5 - 4 * 2.5 * (360 - totalExp))) / (2 * 2.5));
        } else {
            return (int) Math.floor((162.5 + Math.sqrt(-162.5 * -162.5 - 4 * 4.5 * (2220 - totalExp))) / (2 * 4.5));
        }
    }

    public static int removeLevels(Player player, int levelsToRemove) {
        int currentTotalExp = getPlayerTotalExperience(player);
        int targetLevel = Math.max(0, player.experienceLevel - levelsToRemove);
        int targetTotalExp = getTotalExperienceForLevel(targetLevel);
        int expToRemove = currentTotalExp - targetTotalExp;
        player.giveExperienceLevels(-levelsToRemove);
        return expToRemove;
    }

    public static int getExperienceForNextLevel(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        } else {
            return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
        }
    }

    public static int getPlayerTotalExperience(Player player) {
        int exp = getTotalExperienceForLevel(player.experienceLevel);
        long totalXp = (long) exp + (long) Math.round(player.experienceProgress * player.getXpNeededForNextLevel());
        return (int) Math.min(totalXp, Integer.MAX_VALUE);
    }

    public static int getExpNeededForNextLevel(Player player) {
        return player.getXpNeededForNextLevel() - (int) (player.experienceProgress * player.getXpNeededForNextLevel());
    }

    public static float getProgressToNextLevel(int totalExp) {
        int level = getLevelFromTotalExperience(totalExp);
        int expForCurrentLevel = getTotalExperienceForLevel(level);
        int expForNextLevel = getExperienceForNextLevel(level);
        int expAfterFullLevels = totalExp - expForCurrentLevel;
        return (float) expAfterFullLevels / (float) expForNextLevel;
    }

    public static int removePoints(Player player, int pointsToRemove) {
        int currentTotalExp = getPlayerTotalExperience(player);
        int expToRemove = Math.min(currentTotalExp, pointsToRemove);
        player.giveExperiencePoints(-expToRemove);
        return expToRemove;  // Amount of exp removed
    }
}
