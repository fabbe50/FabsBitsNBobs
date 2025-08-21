package com.fabbe50.fabsbnb.mixin;

import com.fabbe50.fabsbnb.FabsBnB;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.tags.TagLoader.EntryWithSource;
import net.minecraft.tags.TagLoader.SortingEntry;
import net.minecraft.util.DependencySorter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(TagLoader.class)
public class TagLoaderMixin {
    @Unique
    private static final TagKey<Item> SPAWN_EGGS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "spawn_eggs"));

    @Inject(method = "build(Ljava/util/Map;)Ljava/util/Map;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/DependencySorter;orderByDependencies(Ljava/util/function/BiConsumer;)V"))
    private <T> void injectBuild(Map<ResourceLocation, List<EntryWithSource>> map, CallbackInfoReturnable<Map<ResourceLocation, Collection<T>>> cir, @Local DependencySorter<ResourceLocation, SortingEntry> dependencySorter) {
        List<EntryWithSource> spawnEggItems = new ArrayList<>();
        SpawnEggItem.eggs().forEach(spawnEggItem -> spawnEggItems.add(new EntryWithSource(TagEntry.optionalElement(spawnEggItem.arch$registryName()), FabsBnB.MOD_ID)));
        dependencySorter.addEntry(SPAWN_EGGS.location(), new SortingEntry(spawnEggItems));
    }
}
