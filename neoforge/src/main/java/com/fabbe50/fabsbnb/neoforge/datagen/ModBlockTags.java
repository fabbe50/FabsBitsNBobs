package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTags extends BlockTagsProvider {
    public ModBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, FabsBnB.MOD_ID, existingFileHelper);
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
                .addOptionalTag(FabsBnB.location("c", "ores"))
                .addOptionalTag(FabsBnB.location("c", "clusters"))
                .addOptionalTag(FabsBnB.location("forge", "ores"))
                .addOptionalTag(FabsBnB.location("forge", "clusters"));

        tag(ModRegistries.TREE_CHOPPER_WHITELIST)
                .addTag(BlockTags.LOGS)
                .add(Blocks.MUSHROOM_STEM)
                .add(Blocks.MANGROVE_ROOTS);

        tag(ModRegistries.TREE_CHOPPER_ATTACHMENTS)
                .addTag(BlockTags.LEAVES)
                .add(Blocks.RED_MUSHROOM_BLOCK)
                .add(Blocks.BROWN_MUSHROOM_BLOCK);

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
                .add(Blocks.GLOWSTONE);
    }
}
