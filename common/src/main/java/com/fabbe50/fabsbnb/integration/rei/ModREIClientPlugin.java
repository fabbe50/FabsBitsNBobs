package com.fabbe50.fabsbnb.integration.rei;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.data.CauldronConversionData;
import com.fabbe50.fabsbnb.data.CustomBrewingRecipe;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.registries.PotionBrewingRecipes;
import com.fabbe50.fabsbnb.util.LangUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.event.EventResult;
import dev.architectury.registry.registries.RegistrySupplier;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.display.visibility.DisplayVisibilityPredicate;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.brewing.BrewingRecipe;
import me.shedaniel.rei.plugin.common.displays.brewing.DefaultBrewingDisplay;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ModREIClientPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<CauldronConversionDisplay> CAULDRON_CONVERSION_DISPLAY = CategoryIdentifier.of(FabsBnB.location("cauldron_conversion"));

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        FabsBnB.log("REI is loaded! Registering plugin displays...");
        Map<Item, Item> conversionMap = CauldronConversionData.getConversionMap();
        for (Item input : conversionMap.keySet()) {
            registry.add(new CauldronConversionDisplay(List.of(EntryIngredients.of(input)), List.of(EntryIngredients.of(conversionMap.get(input)))));
        }
        for (CustomBrewingRecipe brewingRecipe : PotionBrewingRecipes.getBrewingRecipes()) {
            registry.add(new BrewingRecipe(
                    EntryIngredients.of(PotionBrewingRecipes.createItemStack(brewingRecipe.inputItem(), brewingRecipe.inputPotion())),
                    EntryIngredients.of(brewingRecipe.ingredient()),
                    EntryIngredients.of(PotionBrewingRecipes.createItemStack(brewingRecipe.outputItem(), brewingRecipe.outputPotion()))
            ));
        }
        List<EntryIngredient> potionDisplaysToHide = new ArrayList<>();
        potionDisplaysToHide.addAll(getVanillaPotionStack(ModRegistries.FELINE_AURA_POTION_SHORT));
        potionDisplaysToHide.addAll(getVanillaPotionStack(ModRegistries.FELINE_AURA_POTION_LONG));
        potionDisplaysToHide.addAll(getVanillaPotionStack(ModRegistries.SCUTE_POTION_SHORT));
        potionDisplaysToHide.addAll(getVanillaPotionStack(ModRegistries.SCUTE_POTION_LONG));
        potionDisplaysToHide.addAll(getPotionStack(Potions.MUNDANE));
        List<EntryIngredient> craftingDisplaysToHide = new ArrayList<>();
        craftingDisplaysToHide.add(getVanillaTippedArrowStack(ModRegistries.FELINE_AURA_POTION_SHORT));
        craftingDisplaysToHide.add(getVanillaTippedArrowStack(ModRegistries.FELINE_AURA_POTION_LONG));
        craftingDisplaysToHide.add(getVanillaTippedArrowStack(ModRegistries.SCUTE_POTION_SHORT));
        craftingDisplaysToHide.add(getVanillaTippedArrowStack(ModRegistries.SCUTE_POTION_LONG));
        registry.registerVisibilityPredicate((category, display) -> {
            if (display instanceof DefaultBrewingDisplay brewingDisplay) {
                EntryIngredient inputEntry = brewingDisplay.getInputEntries().getFirst();
                if (getPotionStack(Potions.AWKWARD).contains(inputEntry)) {
                    return EventResult.interruptFalse();
                }
                EntryIngredient outputEntry = brewingDisplay.getOutputEntries().getFirst();
                if (potionDisplaysToHide.contains(outputEntry)) {
                    return EventResult.interruptFalse();
                }
            }
            if (display instanceof DefaultCraftingDisplay craftingDisplay) {
                EntryIngredient outputEntry = craftingDisplay.getOutputEntries().getFirst();
                if (craftingDisplaysToHide.contains(outputEntry)) {
                    return EventResult.interruptFalse();
                }
            }
            return EventResult.pass();
        });
    }

    private List<EntryIngredient> getPotionStack(RegistrySupplier<Potion> potion) {
        return getPotionStack(ModRegistries.getPotionReference(potion));
    }

    private List<EntryIngredient> getPotionStack(Holder<Potion> potion) {
        return List.of(
                EntryIngredients.of(PotionBrewingRecipes.createItemStack(ModRegistries.OWN_POTION_ITEM.get(), potion)),
                EntryIngredients.of(PotionBrewingRecipes.createItemStack(ModRegistries.OWN_SPLASH_POTION_ITEM.get(), potion)),
                EntryIngredients.of(PotionBrewingRecipes.createItemStack(ModRegistries.OWN_LINGERING_POTION_ITEM.get(), potion))
        );
    }

    private List<EntryIngredient> getVanillaPotionStack(RegistrySupplier<Potion> potion) {
        return getVanillaPotionStack(ModRegistries.getPotionReference(potion));
    }

    private List<EntryIngredient> getVanillaPotionStack(Holder<Potion> potion) {
        return List.of(
                EntryIngredients.of(PotionBrewingRecipes.createItemStack(Items.POTION, potion)),
                EntryIngredients.of(PotionBrewingRecipes.createItemStack(Items.SPLASH_POTION, potion)),
                EntryIngredients.of(PotionBrewingRecipes.createItemStack(Items.LINGERING_POTION, potion))
        );
    }

    private EntryIngredient getVanillaTippedArrowStack(RegistrySupplier<Potion> potion) {
        return EntryIngredients.of(PotionBrewingRecipes.createItemStack(Items.TIPPED_ARROW, ModRegistries.getPotionReference(potion)));
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        FabsBnB.log("REI is loaded! Registering plugin categories...");
        registry.add(new CauldronConversionCategory());
    }

    @Override
    public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
        FabsBnB.log("REI is loaded! Registering plugin entry filtering...");
        List<RegistrySupplier<Item>> items = List.of(
                ModRegistries.FULL_WATER_CAULDRON
        );
        rule.hide(() -> items.stream().map(RegistrySupplier::get).map(EntryStacks::of).collect(Collectors.toSet()));
        List<ItemStack> stacks = List.of(

        );
        rule.hide(() -> stacks.stream().map(EntryStacks::of).collect(Collectors.toSet()));
    }

    public static class CauldronConversionCategory implements DisplayCategory<CauldronConversionDisplay> {
        @Override
        public CategoryIdentifier<? extends CauldronConversionDisplay> getCategoryIdentifier() {
            return CAULDRON_CONVERSION_DISPLAY;
        }

        @Override
        public Component getTitle() {
            return LangUtils.getTextComponent("cauldron_conversion");
        }

        @Override
        public Renderer getIcon() {
            return EntryStacks.of(ModRegistries.FULL_WATER_CAULDRON.get());
        }

        @Override
        public List<Widget> setupDisplay(CauldronConversionDisplay display, Rectangle bounds) {
            Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 13);
            List<Widget> widgets = new ArrayList<>();
            widgets.add(Widgets.createRecipeBase(bounds));
            widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
            widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5))
                    .entries(display.getOutputEntries().getFirst())
                    .disableBackground()
                    .markOutput());
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y - 5))
                    .entries(display.getInputEntries().getFirst())
                    .markInput());
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 14))
                    .entries(List.of(EntryStacks.of(ModRegistries.FULL_WATER_CAULDRON.get())))
                    .markInput());

            return widgets;
        }
    }

    public static class CauldronConversionDisplay extends BasicDisplay {
        public static final MapCodec<CauldronConversionDisplay> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.list(EntryIngredient.codec()).fieldOf("inputs").forGetter(CauldronConversionDisplay::getInputEntries),
                        Codec.list(EntryIngredient.codec()).fieldOf("outputs").forGetter(CauldronConversionDisplay::getOutputEntries)
                ).apply(instance, CauldronConversionDisplay::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, CauldronConversionDisplay> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.fromCodec(Codec.list(EntryIngredient.codec())), BasicDisplay::getInputEntries,
                ByteBufCodecs.fromCodec(Codec.list(EntryIngredient.codec())), BasicDisplay::getOutputEntries,
                CauldronConversionDisplay::new
        );

        public CauldronConversionDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
            super(inputs, outputs);
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return CAULDRON_CONVERSION_DISPLAY;
        }

        @Override
        public @Nullable DisplaySerializer<? extends Display> getSerializer() {
            return DisplaySerializer.of(CODEC, STREAM_CODEC);
        }
    }
}
