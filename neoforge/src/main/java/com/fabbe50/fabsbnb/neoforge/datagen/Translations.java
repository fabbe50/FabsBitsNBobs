package com.fabbe50.fabsbnb.neoforge.datagen;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.ModConfig;
import com.fabbe50.fabsbnb.config.IConfigOption;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.util.LangUtils;
import com.mojang.datafixers.util.Pair;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class Translations extends LanguageProvider {
    public Translations(PackOutput output, String locale) {
        super(output, FabsBnB.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add(LangUtils.MOD_NAME, "Fab's Bits & Bobs");

        // Blocks
        addBlock(ModRegistries.LAVA_SPONGE, "Lava Sponge");
        addBlock(ModRegistries.LAVA_SPONGE_USED, "Hot Oozing Lava Sponge", "Combine with water bucket in smithing table or place in a cold and dark place.");
        addBlock(ModRegistries.PUSHER_BLOCK, "Pusher Plate");
        addBlock(ModRegistries.THIN_LIGHT, "Light");
        addBlock(ModRegistries.POWERED_THIN_LIGHT, "Powered Light");
        addBlock(ModRegistries.BLOCK_PLACER, "Block Placer");
        addBlock(ModRegistries.BLOCK_BREAKER, "Block Breaker");
        addBlock(ModRegistries.BLOCK_DETECTOR, "Block Detector");
        addBlock(ModRegistries.XP_HOLDER, "XP Holder");

        // Containers
        add(LangUtils.getContainerKey("block_placer"), "Block Placer");
        add(LangUtils.getContainerKey("block_breaker"), "Block Breaker");

        // Items
        addItem(ModRegistries.ITEM_BLOCK_YOINKER, "Block Yoinker", "Allows you to yoink blocks out of the world and place them somewhere else.");
        addItem(ModRegistries.WOODEN_BUILDING_WAND, "Wooden Building Wand");
        addItem(ModRegistries.STONE_BUILDING_WAND, "Stone Building Wand");
        addItem(ModRegistries.IRON_BUILDING_WAND, "Iron Building Wand");
        addItem(ModRegistries.GOLD_BUILDING_WAND, "Gold Building Wand");
        addItem(ModRegistries.DIAMOND_BUILDING_WAND, "Diamond Building Wand");
        addItem(ModRegistries.NETHERITE_BUILDING_WAND, "Netherite Building Wand");
        addItem(ModRegistries.FULL_WATER_CAULDRON, "Water Cauldron");
        addItem(ModRegistries.CAT_CLAW, "Cat Claw");
        addItem(ModRegistries.WHOOSH_WAND, "Whoosh Wand", "Gives a boost on use. Completely negates fall-damage when the item is held.");
        addItem(ModRegistries.WRENCH, "Wrench", "Allows you to rotate directional blocks in the world.");
        addItem(ModRegistries.MILK_BOTTLE, "Milk Bottle");
        addItem(ModRegistries.CHOCOLATE_MILK_BOTTLE, "Chocolate Milk Bottle");
        addItem(ModRegistries.CHOCOLATE_NECKLACE, "Chocolate Empowered Necklace");

        // Text
        addText("empty", "Empty");
        addText("true", "True");
        addText("false", "False");
        addText("contains", "Contains: %s");
        addText("building_wand.selected_block", "Selected Block: %s");
        addText("building_wand.set_block", "Set block to %s");
        addText("building_wand.not_enough_blocks.abort", "Not enough blocks to complete placement.");
        addText("building_wand.not_enough_blocks.inventory", "Not enough blocks in inventory to place.");
        addText("building_wand.fuzzy_toggle", "Fuzzy Mode: %s");
        addText("detector.set", "Set target to: %s");
        addText("detector.info", "Right click with a redstone torch when the block you want to target is in front of the detector. Current Target: %s");
        addText("xp_holder.collect", "Collect XP Mode: %s");
        addText("xp_holder.stored_level", "Stored Levels: %s");
        addText("value.blocks", "%s Blocks");

        // Effects & Potions
        add(ModRegistries.FELINE_AURA.get(), "Feline Aura");
        add("effect.fabsbnb.feline_aura.description", "Scares attacking phantoms and causes creepers to run away in fear.");
        addPotion("feline_aura_short", "Feline Aura");
        addPotion("feline_aura_long", "Feline Aura");

        // Enchantments
        addEnchantment("vein_miner", "Ore Miner", "Mines the entire ore vein and drops the items at the original block.");
        addEnchantment("tree_chopper", "Tree Chopper", "Mines the entire tree stem and drops the items at the original block.");
        addEnchantment("leaf_breaker", "Leaf Breaker", "Mines the all leaves within range and drops the items at the original block.");
        addEnchantment("capturing", "Capturing", "Adds a chance for mobs to drop their spawn eggs.");

        // Config
        addConfig(ModConfig.debugMode, "Debug Mode", "Shows more information in the log. NOTE: CAN BE SPAMMY");
        addConfig(ModConfig.woodenBuildingWandRadius, "Wooden Building Wand Radius", "The radius of which the building wand will place block in.");
        addConfig(ModConfig.stoneBuildingWandRadius, "Stone Building Wand Radius", "The radius of which the building wand will place block in.");
        addConfig(ModConfig.ironBuildingWandRadius, "Iron Building Wand Radius", "The radius of which the building wand will place block in.");
        addConfig(ModConfig.goldBuildingWandRadius, "Golden Building Wand Radius", "The radius of which the building wand will place block in.");
        addConfig(ModConfig.diamondBuildingWandRadius, "Diamond Building Wand Radius", "The radius of which the building wand will place block in.");
        addConfig(ModConfig.netheriteBuildingWandRadius, "Netherite Building Wand Radius", "The radius of which the building wand will place block in.");
        addConfig(ModConfig.whooshWandMultiplier, "Whoosh Wand Movement Multiplier", "The multiplier for the boost the whoosh wand gives you.");
        addConfig(ModConfig.whooshWandCooldown, "Whoosh Wand Cooldown", "The cooldown applied to the wand after each use.");
        addConfig(ModConfig.necklaceWorksInInventory, "Necklace Works in Inventory", "If the chocolate necklace should work anywhere in the inventory. Otherwise it will only work while it's being held or in curio/trinket slot.");
        addConfig(ModConfig.entityMoverBlockSpeed, "Pusher Block Speed Multiplier", "The multiplier for the speed addon calculation. [{current speed} + ({multiplier} * 1.5)]");
        addConfig(ModConfig.oreMinerMiningLimit, "Ore Miner Limit", "The amount of blocks the ore miner enchantment is allowed to mine. NOTE: LARGE NUMBERS CAN CAUSE LAG");
        addConfig(ModConfig.oreMinerScanRange, "Ore Miner Scan Range", "How far away from each block it should scan. This is not the originally mined block, but rather next block in the scan queue.");
        addConfig(ModConfig.treeChopperMiningLimit, "Tree Chopper Limit", "The amount of blocks the tree chopper enchantment is allowed to harvest. NOTE: LARGE NUMBERS CAN CAUSE LAG");
        addConfig(ModConfig.treeChopperScanRange, "Tree Chopper Scan Range", "How far away from each block it should scan. This is not the originally mined block, but rather next block in the scan queue.");
        addConfig(ModConfig.leafBreakerMiningLimit, "Leaf Breaker Limit", "The amount of blocks the leaf breaker enchantment is allowed to harvest. NOTE: LARGE NUMBERS CAN CAUSE LAG");
        addConfig(ModConfig.leafBreakerScanRange, "Leaf Breaker Scan Range", "How far away from each block it should scan. This is not the originally mined block, but rather next block in the scan queue.");
        addConfig(ModConfig.oneInNChanceToDropSpawnEgg, "1 in n chance to drop spawn egg",
                "The 1 in n chance for a spawn egg to drop when killed with capturing enchantment.",
                "Note: n is divided by the level of the enchantment. For example 'n=100' would result in a 1/33 chance at level 3."
        );

        // Misc
        add("emi.category.fabsbnb.cauldron_conversion", "Cauldron Conversation");
        add("tag.item.fabsbnb.building_wands", "Building Wands");
        add("tag.item.c.netherite_items", "Netherite Items");
        add("tag.item.curios.necklace", "Necklace");
        add("tag.item.fabsbnb.immune_to_cactus", "Immune to Cactus");
        add("jei.category.fabsbnb.cauldron_conversion", "Cauldron Conversation");
        add("fabsbnb.cauldron_conversion", "Cauldron Conversation");
    }

    public void addText(String simpleKey, String translation) {
        add(LangUtils.getTextKey(simpleKey), translation);
    }

    public void addItem(RegistrySupplier<Item> item, String translation) {
        add(item.get(), translation);
    }

    public void addItem(RegistrySupplier<Item> item, String translation, String description) {
        addItem(item, translation, Pair.of("desc", description));
    }

    @SafeVarargs
    public final void addItem(RegistrySupplier<Item> item, String translation, Pair<String, String>... descriptionPair) {
        add(item.get(), translation);
        for (Pair<String, String> pair : descriptionPair) {
            add(item.get().getDescriptionId() + "." + pair.getFirst(), pair.getSecond());
        }
    }

    public void addBlock(RegistrySupplier<Block> block, String translation) {
        add(block.get(), translation);
    }

    public void addBlock(RegistrySupplier<Block> block, String translation, String description) {
        addBlock(block, translation, Pair.of("desc", description));
    }

    @SafeVarargs
    public final void addBlock(RegistrySupplier<Block> block, String translation, Pair<String, String>... descriptionPair) {
        add(block.get(), translation);
        for (Pair<String, String> pair : descriptionPair) {
            add(block.get().getDescriptionId() + "." + pair.getFirst(), pair.getSecond());
        }
    }

    public void addPotion(String name, String translation) {
        add("item.minecraft.potion.effect." + name, "Potion of " + translation);
        add("item.minecraft.splash_potion.effect." + name, "Splash Potion of " + translation);
        add("item.minecraft.lingering_potion.effect." + name, "Lingering Potion of " + translation);
        add("item.minecraft.tipped_arrow.effect." + name, "Arrow of " + translation);
    }

    public void addEnchantment(String name, String translation, String description) {
        add("enchantment." + FabsBnB.MOD_ID + "." + name, translation);
        add("enchantment." + FabsBnB.MOD_ID + "." + name + ".desc", description);
    }

    public <V extends IConfigOption<?, ?>> void addConfig(V config, String translation, String... description) {
        addConfig(config.getKey(), translation, description);
    }

    public void addConfig(String name, String translation, String... description) {
        add(LangUtils.getConfigKey(name), translation);
        for (int i = 0; i < description.length; i++) {
            add(LangUtils.getConfigTooltipKey(name, i), description[i]);
        }
    }
}
