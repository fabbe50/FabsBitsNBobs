package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTags extends BlockTagsProvider {
    private final TagKey<Block> COMMON_ORE_TAG = TagKey.create(Registries.BLOCK, FabsBnB.location("c", "ores"));
    private final TagKey<Block> COMMON_CLUSTERS_TAG = TagKey.create(Registries.BLOCK, FabsBnB.location("c", "clusters"));
    private final TagKey<Block> FORGE_ORE_TAG = TagKey.create(Registries.BLOCK, FabsBnB.location("forge", "ores"));
    private final TagKey<Block> FORGE_CLUSTERS_TAG = TagKey.create(Registries.BLOCK, FabsBnB.location("forge", "clusters"));

    public ModBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, FabsBnB.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ModRegistries.BLOCK_YOINKER_BLACKLIST)
                .add(Blocks.BEDROCK)
                .add(Blocks.END_PORTAL_FRAME)
                .addTag(BlockTags.PORTALS)
                .addTag(BlockTags.BEDS)
                .addTag(BlockTags.DOORS);

        tag(ModRegistries.ORE_MINER_WHITELIST)
                .addOptionalTag(COMMON_ORE_TAG)
                .addOptionalTag(COMMON_CLUSTERS_TAG)
                .addOptionalTag(FORGE_ORE_TAG)
                .addOptionalTag(FORGE_CLUSTERS_TAG);

        tag(ModRegistries.TREE_CHOPPER_WHITELIST)
                .addTag(BlockTags.LOGS)
                .add(Blocks.MUSHROOM_STEM)
                .add(Blocks.MANGROVE_ROOTS)
                .add(Blocks.RED_MUSHROOM_BLOCK)
                .add(Blocks.BROWN_MUSHROOM_BLOCK);

        tag(ModRegistries.TREE_CHOPPER_ATTACHMENTS)
                .addTag(BlockTags.LEAVES)
                .add(Blocks.RED_MUSHROOM_BLOCK)
                .add(Blocks.BROWN_MUSHROOM_BLOCK);

        tag(ModRegistries.LEAF_BREAKER_WHITELIST)
                .addTag(BlockTags.LEAVES);

        tag(ModRegistries.SCYTHE_ABLE)
                .addTag(BlockTags.FLOWERS)
                .add(Blocks.SHORT_GRASS)
                .add(Blocks.TALL_GRASS)
                .add(Blocks.SEAGRASS)
                .add(Blocks.TALL_SEAGRASS);

        tag(ModRegistries.SPIDER_NOT_CLIMBABLE)
                .addTag(BlockTags.ICE)
                .add(
                        Blocks.WHITE_GLAZED_TERRACOTTA,
                        Blocks.ORANGE_GLAZED_TERRACOTTA,
                        Blocks.MAGENTA_GLAZED_TERRACOTTA,
                        Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA,
                        Blocks.YELLOW_GLAZED_TERRACOTTA,
                        Blocks.LIME_GLAZED_TERRACOTTA,
                        Blocks.PINK_GLAZED_TERRACOTTA,
                        Blocks.GRAY_GLAZED_TERRACOTTA,
                        Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA,
                        Blocks.CYAN_GLAZED_TERRACOTTA,
                        Blocks.PURPLE_GLAZED_TERRACOTTA,
                        Blocks.BLUE_GLAZED_TERRACOTTA,
                        Blocks.BROWN_GLAZED_TERRACOTTA,
                        Blocks.GREEN_GLAZED_TERRACOTTA,
                        Blocks.RED_GLAZED_TERRACOTTA,
                        Blocks.BLACK_GLAZED_TERRACOTTA
                );

        tag(TagKey.create(Registries.BLOCK, FabsBnB.location("c", "clusters")))
                .add(Blocks.AMETHYST_CLUSTER);

        tag(BlockTags.MINEABLE_WITH_HOE)
                .add(ModRegistries.LAVA_SPONGE.get())
                .add(ModRegistries.LAVA_SPONGE_USED.get())
                .add(Blocks.CACTUS);

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModRegistries.PUSHER_BLOCK.get())
                .add(ModRegistries.THIN_LIGHT.get())
                .add(ModRegistries.POWERED_THIN_LIGHT.get())
                .add(ModRegistries.BLOCK_BREAKER.get())
                .add(ModRegistries.BLOCK_PLACER.get())
                .add(ModRegistries.BLOCK_DETECTOR.get())
                .add(ModRegistries.XP_HOLDER.get())
                .add(ModRegistries.STRUCTURAL_GLASS.get())
                .add(Blocks.GLOWSTONE);

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModRegistries.SLIME_SAND.get());

        tag(BlockTags.WITHER_IMMUNE)
                .add(ModRegistries.STRUCTURAL_GOOP.get())
                .add(ModRegistries.STRUCTURAL_GLASS.get());
    }
}
