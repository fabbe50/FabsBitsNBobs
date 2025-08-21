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
        addText("building_wand.desc_screen[0]", "Building Wands provide a more convenient way to build large structures by allowing the placement of multiple blocks at the same time.");
        addText("building_wand.desc_screen[1]", " ");
        addText("building_wand.desc_screen[2]", "There are two modes for placement. Normal and Fuzzy.");
        addText("building_wand.desc_screen[3]", " ");
        addText("building_wand.desc_screen[4]", "Normal Mode: ");
        addText("building_wand.desc_screen[5]", "Places blocks on top of other blocks of the same type and only if the blocks are all adjacent (diagonals count) to each other.");
        addText("building_wand.desc_screen[6]", " ");
        addText("building_wand.desc_screen[7]", "Fuzzy Mode: ");
        addText("building_wand.desc_screen[8]", "Places blocks on top of other blocks where type doesn't matter. Blocks are also placed on all blocks within reach, even if they are not connected.");
        addText("chocolate_items.desc_screen", "Removes negative potion effects.");
        addText("milk_bottle.desc_screen", "Removes potion effects.");
        addText("yoinker.desc_screen", "Used to yoink blocks out of the world and place them somewhere else. Can for example be used to move chests without having to empty them first.");
        addText("whoosh_wand.desc_screen", "Alternative to rockets. Gives a boost in the direction you're facing. Gives complete immunity to fall & kinetic damage.");
        addText("wrench.desc_screen", "Used to rotate directional blocks. For example pistons, chests and and logs. Rotates the block clockwise (counter clockwise if sneaking) around the axis of the clicked face.");
        addText("lava_sponge.desc_screen", "Like a normal sponge, but for lava. Simply place in lava to remove a large area of lava. Then clear the sponge by placing it in water, a cold biome or dropping it in a cauldron with water.");
        addText("pusher_plate.desc_screen", "Pushes entities that land on the block. The direction is shown by the arrow on top of it. Must be supported underneath. It gets placed facing away from the player in the direction the player is looking.");
        addText("lights.desc_screen", "A small simple light. Can be waterlogged. Controlled by redstone, turns on when it receives a signal. Powered Light is always on.");
        addText("cat_claw.desc_screen", "Obtained by using shears on a cat. Ingredient for the Potion of Feline Aura.");
        addText("block_placer.desc_screen[0]", "Places blocks in the world.");
        addText("block_placer.desc_screen[1]", " ");
        addText("block_placer.desc_screen[2]", "Similar to a dispenser, it has 9 inventory slots. It picks a block out of a random non-empty slot when it receives a redstone signal.");
        addText("block_breaker.desc_screen[0]", "Breaks blocks in the world.");
        addText("block_breaker.desc_screen[1]", " ");
        addText("block_breaker.desc_screen[2]", "Has a tool slot in it's inventory. The tool will be used for breaking the blocks. Enchantments on the tool will be applied correctly.");
        addText("block_breaker.desc_screen[3]", " ");
        addText("block_breaker.desc_screen[4]", "The tool will take 1 durability damage for every block it breaks (2 damage if it's the incorrect tool for the block).");
        addText("block_detector.desc_screen[0]", "Detects blocks placed in front of it.");
        addText("block_detector.desc_screen[1]", " ");
        addText("block_detector.desc_screen[2]", "Outputs a redstone signal if the block matches the set block.");
        addText("block_detector.desc_screen[3]", "Set the block by placing it in front of the detector and right clicking the detector with a redstone torch.");
        addText("xp_holder.desc_screen", "Holds XP for use later. Right Click to insert 1 level. Shift + Right Click to insert all levels. Left Click to take 1 level. Shift + Left Click to take 10 levels. Retains levels when broken with a pickaxe.");
        addText("immune_to_cactus.desc_screen", "Items that don't break when they land on a cactus block.");
        addText("spider_no_climb.desc_screen", "Blocks that spiders can't climb.");
        addText("ore_miner.desc_screen", "Mines the entire ore vein at once. Only works on blocks with the #fabsbnb:ore_miner_whitelist tag.");
        addText("tree_chopper.desc_screen", "Chops down the entire tree at once. Only works on blocks with the #fabsbnb:tree_chopper_whitelist tag.");
        addText("leaf_breaker.desc_screen", "Breaks all leaves within range. Only works on blocks with the #fabsbnb:leaf_breaker_whitelist tag.");
        addText("capturing.desc_screen", "When a mob is killed with capturing, there's a chance that a spawn egg will drop.");
        addText("feline_aura.desc_screen", "Scares away creepers and phantoms.");
        addText("cactus.desc_screen[0]", "Adjusted behaviour:");
        addText("cactus.desc_screen[1]", "Doesn't break when it lands on a cactus block.");
        addText("cactus.desc_screen[2]", "Edible. Giving 2 chunks of food and high saturation. Inflicts weak nausea for 7.5 seconds.");
        addText("glistering_melon.desc_screen[0]", "Adjusted behaviour:");
        addText("glistering_melon.desc_screen[1]", "Edible. Giving 1.5 chunks of food and high saturation. Applies regeneration for 5 seconds.");
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
        addEnchantment("ore_miner", "Ore Miner", "Mines the entire ore vein and drops the items at the original block.");
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
