package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.color.item.Potion;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput arg) {
        super(arg, FabsBnB.MOD_ID);
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        for (RegistrySupplier<Block> block : ModRegistries.NORMAL_BLOCK_LIST) {
            blockModels.createTrivialCube(block.get());
        }
        blockModels.createNonTemplateModelBlock(ModRegistries.XP_HOLDER.get());
        blockModels.createNonTemplateHorizontalBlock(ModRegistries.SLOW_PUSHER_BLOCK.get());
        blockModels.createNonTemplateHorizontalBlock(ModRegistries.NORMAL_PUSHER_BLOCK.get());
        blockModels.createNonTemplateHorizontalBlock(ModRegistries.FAST_PUSHER_BLOCK.get());

        for (RegistrySupplier<Item> itemBlock : ModRegistries.BLOCK_ITEM_LIST) {
            createBlockItemModel(blockModels, itemBlock);
        }

        for (RegistrySupplier<Item> item : ModRegistries.NORMAL_ITEM_LIST) {
            Item item1 = item.get();
            if (!(item1 instanceof BlockItem)) {
                itemModels.generateFlatItem(item1, ModelTemplates.FLAT_ITEM);
            }
        }
        createItemModel("minecraft", itemModels, ModRegistries.EXT_ENCHANTED_BOOK);
        createPotionModel("minecraft", itemModels, ModRegistries.OWN_POTION_ITEM);
        createPotionModel("minecraft", itemModels, ModRegistries.OWN_SPLASH_POTION_ITEM);
        createPotionModel("minecraft", itemModels, ModRegistries.OWN_LINGERING_POTION_ITEM);
        createPotionModel("minecraft", itemModels, ModRegistries.OWN_TIPPED_ARROW_ITEM);
    }

    private void createBlockItemModel(BlockModelGenerators blockModels, RegistrySupplier<Item> blockSupplier) {
        blockModels.itemModelOutput.accept(blockSupplier.get(), ItemModelUtils.plainModel(getBlockModelLocation(blockSupplier)));
    }

    private void createPotionModel(String namespace, ItemModelGenerators itemModels, RegistrySupplier<Item> itemSupplier) {
        itemModels.itemModelOutput.accept(itemSupplier.get(), ItemModelUtils.tintedModel(getItemModelLocation(namespace, itemSupplier), new Potion()));
    }

    private void createItemModel(ItemModelGenerators itemModels, RegistrySupplier<Item> itemSupplier) {
        itemModels.itemModelOutput.accept(itemSupplier.get(), ItemModelUtils.plainModel(getItemModelLocation(itemSupplier)));
    }

    private void createItemModel(String namespace, ItemModelGenerators itemModels, RegistrySupplier<Item> itemSupplier) {
        itemModels.itemModelOutput.accept(itemSupplier.get(), ItemModelUtils.plainModel(getItemModelLocation(namespace, itemSupplier)));
    }

    private ResourceLocation getBlockModelLocation(RegistrySupplier<Item> itemSupplier) {
        return FabsBnB.location("block/" + itemSupplier.getId().getPath());
    }

    private ResourceLocation getItemModelLocation(RegistrySupplier<Item> itemSupplier) {
        return FabsBnB.location("item/" + itemSupplier.getId().getPath());
    }

    private ResourceLocation getItemModelLocation(String namespace, RegistrySupplier<Item> itemSupplier) {
        return FabsBnB.location(namespace, "item/" + itemSupplier.getId().getPath());
    }

    @Override
    protected @NotNull Stream<? extends Holder<Block>> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.listElements().filter(blockReference -> ModRegistries.BLOCK_LIST.contains(blockReference.value()));
    }

    @Override
    protected @NotNull Stream<? extends Holder<Item>> getKnownItems() {
        return BuiltInRegistries.ITEM.listElements().filter(itemReference -> ModRegistries.ITEM_LIST.contains(itemReference.value()));
    }
}
