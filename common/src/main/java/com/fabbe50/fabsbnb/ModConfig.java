package com.fabbe50.fabsbnb;

import com.fabbe50.fabsbnb.config.*;
import com.fabbe50.fabsbnb.util.LangUtils;
import dev.architectury.platform.Platform;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class ModConfig {
    private static File configFile;

    private static final Map<String, IConfigOption<?, ?>> configOptions = new LinkedHashMap<>();

    public static BooleanOption debugMode = addConfig(new BooleanOption("debugMode", true));

    public static IntegerSliderOption woodenBuildingWandRadius = addConfig(new IntegerSliderOption("woodenBuildingWandRadius", 1, 1, 30).makeBuilder().textGetter(LangUtils.VALUE_BLOCKS).subCategory("building_wands").build());
    public static IntegerSliderOption stoneBuildingWandRadius = addConfig(new IntegerSliderOption("stoneBuildingWandRadius", 2, 1, 30).makeBuilder().textGetter(LangUtils.VALUE_BLOCKS).subCategory("building_wands").build());
    public static IntegerSliderOption ironBuildingWandRadius = addConfig(new IntegerSliderOption("ironBuildingWandRadius", 7, 1, 30).makeBuilder().textGetter(LangUtils.VALUE_BLOCKS).subCategory("building_wands").build());
    public static IntegerSliderOption goldBuildingWandRadius = addConfig(new IntegerSliderOption("goldBuildingWandRadius", 5, 1, 30).makeBuilder().textGetter(LangUtils.VALUE_BLOCKS).subCategory("building_wands").build());
    public static IntegerSliderOption diamondBuildingWandRadius = addConfig(new IntegerSliderOption("diamondBuildingWandRadius", 9, 1, 30).makeBuilder().textGetter(LangUtils.VALUE_BLOCKS).subCategory("building_wands").build());
    public static IntegerSliderOption netheriteBuildingWandRadius = addConfig(new IntegerSliderOption("netheriteBuildingWandRadius", 15, 1, 30).makeBuilder().textGetter(LangUtils.VALUE_BLOCKS).subCategory("building_wands").build());

    public static DoubleOption whooshWandMultiplier = addConfig(new DoubleOption("whooshWandMultiplier", 3d, 1d, 50d));
    public static IntegerOption whooshWandCooldown = addConfig(new IntegerOption("whooshWandCooldown", 5, 0, Integer.MAX_VALUE));
    public static IntegerOption whooshWandDurability = addConfig(new IntegerOption("whooshWandDurability", 786));

    public static BooleanOption necklaceWorksInInventory = addConfig(new BooleanOption("necklaceWorksInInventory", true));
    public static IntegerOption necklaceDurability = addConfig(new IntegerOption("necklaceDurability", 256));

    public static DoubleOption entityMoverBlockSpeed = addConfig(new DoubleOption("entityMoverBlockSpeed", 0.3d));
    public static BooleanOption experimentalSquidPushing = addConfig(new BooleanOption("experimentalSquidPushing", false));

    public static IntegerOption xpHolderCollectionRange = addConfig(new IntegerOption("xpHolderCollectionRange", 5, 1, 20));

    public static BooleanOption oreMinerEnabled = addConfig(new BooleanOption("oreMinerEnabled", true).makeBuilder().requiresRestart().category("enchantments").subCategory("ore_miner").build());
    public static IntegerOption oreMinerMiningLimit = addConfig(new IntegerOption("oreMinerMiningLimit", 256, 1, Integer.MAX_VALUE).makeBuilder().requiresRestart().category("enchantments").subCategory("ore_miner").build());
    public static IntegerOption oreMinerScanRange = addConfig(new IntegerOption("oreMinerScanRange", 2, 1, 10).makeBuilder().requiresRestart().category("enchantments").subCategory("ore_miner").build());
    public static BooleanOption treeChopperEnabled = addConfig(new BooleanOption("treeChopperEnabled", true).makeBuilder().requiresRestart().category("enchantments").subCategory("tree_chopper").build());
    public static IntegerOption treeChopperMiningLimit = addConfig(new IntegerOption("treeChopperMiningLimit", 256, 1, Integer.MAX_VALUE).makeBuilder().requiresRestart().category("enchantments").subCategory("tree_chopper").build());
    public static IntegerOption treeChopperScanRange = addConfig(new IntegerOption("treeChopperScanRange", 2, 1, 10).makeBuilder().requiresRestart().category("enchantments").subCategory("tree_chopper").build());
    public static BooleanOption leafBreakerEnabled = addConfig(new BooleanOption("leafBreakerEnabled", true).makeBuilder().requiresRestart().category("enchantments").subCategory("leaf_breaker").build());
    public static IntegerOption leafBreakerMiningLimit = addConfig(new IntegerOption("leafBreakerMiningLimit", 64, 1, Integer.MAX_VALUE).makeBuilder().requiresRestart().category("enchantments").subCategory("leaf_breaker").build());
    public static IntegerOption leafBreakerScanRange = addConfig(new IntegerOption("leafBreakerScanRange", 1, 1, 10).makeBuilder().requiresRestart().category("enchantments").subCategory("leaf_breaker").build());
    public static BooleanOption capturingEnabled = addConfig(new BooleanOption("capturingEnabled", true).makeBuilder().requiresRestart().category("enchantments").subCategory("capturing").build());
    public static IntegerOption oneInNChanceToDropSpawnEgg = addConfig(new IntegerOption("oneInNChanceToDropSpawnEgg", 100, 1, Integer.MAX_VALUE).makeBuilder().category("enchantments").subCategory("capturing").build());
    public static BooleanOption harvestingEnabled = addConfig(new BooleanOption("harvestingEnabled", true).makeBuilder().requiresRestart().category("enchantments").subCategory("harvesting").build());
    public static BooleanOption tillingEnabled = addConfig(new BooleanOption("tillingEnabled", true).makeBuilder().requiresRestart().category("enchantments").subCategory("tilling").build());
    public static BooleanOption scytheEnabled = addConfig(new BooleanOption("scytheEnabled", true).makeBuilder().requiresRestart().category("enchantments").subCategory("scythe").build());

    public static void register() {
        FabsBnB.log("Registering config...");
        configFile = new File(Platform.getConfigFolder().toFile(), FabsBnB.MOD_ID + ".properties");
        load(configFile);
        FabsBnB.log("Config registered!");
    }

    public static File getConfigFile() {
        return configFile;
    }

    public static void load(File file) {
        try {
            FabsBnB.log("Loading config...");
            FileInputStream fis = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();

            for (String key : configOptions.keySet()) {
                IConfigOption<?, ?> config = configOptions.get(key);
                config.readData(properties);
            }

            FabsBnB.log("Config loaded!");
        } catch (IOException e) {
            try {
                save(file);
                load(file);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void save(File file) throws IOException {
        FabsBnB.log("Saving config...");
        FileOutputStream fos = new FileOutputStream(file, false);

        for (String key : configOptions.keySet()) {
            IConfigOption<?, ?> config = configOptions.get(key);
            config.writeData(fos);
        }

        fos.close();
        FabsBnB.log("Config saved!");
    }

    private static <T, R, V extends IConfigOption<T, R>> V addConfig(V configOption) {
        configOptions.put(configOption.getKey(), configOption);
        return configOption;
    }

    public static Map<String, IConfigOption<?, ?>> getConfigOptions() {
        return configOptions;
    }
}
