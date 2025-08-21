package com.fabbe50.fabsbnb.data;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.util.LangUtils;
import com.fabbe50.fabsbnb.world.item.enchantments.IEnchantment;
import com.mojang.datafixers.util.Pair;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

import java.util.*;

public class ItemInformations {
    private static final Map<String, Pair<List<Item>, List<Component>>> itemComponentsMap = new LinkedHashMap<>();
    private static final Map<IEnchantment, List<Component>> enchantmentComponents = new LinkedHashMap<>();
    private static final Map<String, Pair<List<ItemStack>, List<Component>>> itemStackComponents = new LinkedHashMap<>();
    private static final Map<String, Pair<TagKey<?>, List<Component>>> tagKeyComponents = new LinkedHashMap<>();

    static {
        addComponent("cactus", 3, Items.CACTUS);
        addComponent("glistering_melon", 2, Items.GLISTERING_MELON_SLICE);
        addComponent("chocolate_items", ModRegistries.CHOCOLATE_MILK_BOTTLE.get(), ModRegistries.CHOCOLATE_NECKLACE.get());
        addComponent("milk_bottle", ModRegistries.MILK_BOTTLE.get());
        addComponent("yoinker", ModRegistries.ITEM_BLOCK_YOINKER.get());
        addComponent("whoosh_wand", ModRegistries.WHOOSH_WAND.get());
        addComponent("wrench", ModRegistries.WRENCH.get());
        addComponent("lava_sponge", ModRegistries.ITEM_LAVA_SPONGE.get(), ModRegistries.ITEM_LAVA_SPONGE_USED.get());
        addComponent("pusher_plate", ModRegistries.ITEM_PUSHER_BLOCK.get());
        addComponent("lights", ModRegistries.ITEM_THIN_LIGHT.get(), ModRegistries.ITEM_POWERED_THIN_LIGHT.get());
        addComponent("cat_claw", ModRegistries.CAT_CLAW.get());
        addComponent("block_placer", 3, ModRegistries.ITEM_BLOCK_PLACER.get());
        addComponent("block_breaker", 5, ModRegistries.ITEM_BLOCK_BREAKER.get());
        addComponent("block_detector", 4, ModRegistries.ITEM_BLOCK_DETECTOR.get());
        addComponent("xp_holder", ModRegistries.ITEM_XP_HOLDER.get());

        addComponent("building_wand", 9, ModRegistries.BUILDING_WANDS);
        addComponent("immune_to_cactus", ModRegistries.IMMUNE_TO_CACTUS_DAMAGE);
        addComponent("spider_no_climb", ModRegistries.SPIDER_NOT_CLIMBABLE);

        for (IEnchantment enchantment : ModRegistries.ENCHANTMENT_LIST) {
            addComponent(enchantment.getResourceKey().location().getPath(), enchantment);
        }
        for (RegistrySupplier<Potion> potion : ModRegistries.POTION_LIST) {
            addComponent(
                    ModRegistries.getPotionReference(potion).getRegisteredName()
                            .replace(FabsBnB.MOD_ID + ":", "")
                            .replace("_short", "")
                            .replace("_long", "")
                            .replace("_strong", ""),
                    PotionContents.createItemStack(Items.POTION, ModRegistries.getPotionReference(potion)),
                    PotionContents.createItemStack(Items.SPLASH_POTION, ModRegistries.getPotionReference(potion)),
                    PotionContents.createItemStack(Items.LINGERING_POTION, ModRegistries.getPotionReference(potion)),
                    PotionContents.createItemStack(Items.TIPPED_ARROW, ModRegistries.getPotionReference(potion))
            );
        }
    }

    public static Map<String, Pair<List<Item>, List<Component>>> getItemComponentsMap() {
        return itemComponentsMap;
    }

    public static Map<IEnchantment, List<Component>> getEnchantmentComponents() {
        return enchantmentComponents;
    }

    public static Map<String, Pair<List<ItemStack>, List<Component>>> getItemStackComponents() {
        return itemStackComponents;
    }

    public static Map<String, Pair<TagKey<?>, List<Component>>> getTagKeyComponents() {
        return tagKeyComponents;
    }

    private static void addComponent(String name, Item... items) {
        addComponent(name, 1, items);
    }

    private static void addComponent(String name, int rows, Item... items) {
        itemComponentsMap.put(name, new Pair<>(List.of(items), createComponents(name, rows)));
    }

    private static void addComponent(String name, IEnchantment enchantment) {
        addComponent(name, 1, enchantment);
    }

    private static void addComponent(String name, int rows, IEnchantment enchantment) {
        enchantmentComponents.put(enchantment, createComponents(name, rows));
    }

    private static void addComponent(String name, ItemStack... stacks) {
        addComponent(name, 1, stacks);
    }

    private static void addComponent(String name, int rows, ItemStack... stacks) {
        if (itemStackComponents.containsKey(name)) {
            Pair<List<ItemStack>, List<Component>> pair = itemStackComponents.get(name);
            List<ItemStack> temp = new ArrayList<>(pair.getFirst());
            temp.addAll(Arrays.stream(stacks).toList());
            itemStackComponents.put(name, new Pair<>(temp, pair.getSecond()));
        } else {
            itemStackComponents.put(name, new Pair<>(List.of(stacks), createComponents(name, rows)));
        }
    }

    private static void addComponent(String name, TagKey<?> tagKey) {
        addComponent(name, 1, tagKey);
    }

    private static void addComponent(String name, int rows, TagKey<?> tagKey) {
        tagKeyComponents.put(name, new Pair<>(tagKey, createComponents(name, rows)));
    }

    public static List<Component> createComponents(String name) {
        return createComponents(name, 1);
    }

    public static List<Component> createComponents(String name, int rows) {
        List<Component> components = new ArrayList<>();
        if (rows > 1) {
            for (int i = 0; i < rows; i++) {
                components.add(LangUtils.getTextComponent(name + ".desc_screen[" + i + "]"));
            }
        } else {
            components.add(LangUtils.getTextComponent(name + ".desc_screen"));
        }
        return components;
    }
}
