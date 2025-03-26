package com.fabbe50.fabsbnb;

import dev.architectury.platform.Platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ModConfig {
    public static ModConfig INSTANCE = new ModConfig();
    private static File configFile;

    public boolean lavaSpongeEnabled = true;
    public boolean lightEnabled = true;
    public boolean pusherBlockEnabled = true;
    public boolean felineAuraPotionEnabled = true;
    public boolean felineAuraAffectsCreeper = true;
    public boolean felineAuraAffectsPhantom = true;
    public boolean veinMinerEnchantEnabled = true;
    public boolean blockYoinkerEnabled = true;
    public boolean buildingWandsEnabled = true;
    public boolean whooshWandEnabled = true;
    public boolean spiderClimbTagsEnabled = true;

    public int woodenBuildingWandRadius = 1;
    public int stoneBuildingWandRadius = 2;
    public int ironBuildingWandRadius = 7;
    public int goldBuildingWandRadius = 5;
    public int diamondBuildingWandRadius = 9;
    public int netheriteBuildingWandRadius = 15;

    public double whooshWandMultiplier = 3;

    public double entityMoverBlockSpeed = 0.3;


    public static void register() {
        configFile = new File(Platform.getConfigFolder().toFile(), FabsBnB.MOD_ID + ".properties");
        load(configFile);
    }

    public static File getConfigFile() {
        return configFile;
    }

    public static void load(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();

            INSTANCE.woodenBuildingWandRadius = readInt(properties, "woodenBuildingWandRadius", 1);
            INSTANCE.stoneBuildingWandRadius = readInt(properties, "stoneBuildingWandRadius", 2);
            INSTANCE.ironBuildingWandRadius = readInt(properties, "ironBuildingWandRadius", 7);
            INSTANCE.goldBuildingWandRadius = readInt(properties, "goldBuildingWandRadius", 5);
            INSTANCE.diamondBuildingWandRadius = readInt(properties, "diamondBuildingWandRadius", 9);
            INSTANCE.netheriteBuildingWandRadius = readInt(properties, "netheriteBuildingWandRadius", 15);

            INSTANCE.whooshWandMultiplier = readDouble(properties, "whooshWandMultiplier", 3);

            INSTANCE.entityMoverBlockSpeed = readDouble(properties, "entityMoverBlockSpeed", 0.3);
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
        FileOutputStream fos = new FileOutputStream(file, false);

        writeData(fos, "woodenBuildingWandRadius", String.valueOf(INSTANCE.woodenBuildingWandRadius));
        writeData(fos, "stoneBuildingWandRadius", String.valueOf(INSTANCE.stoneBuildingWandRadius));
        writeData(fos, "ironBuildingWandRadius", String.valueOf(INSTANCE.ironBuildingWandRadius));
        writeData(fos, "goldBuildingWandRadius", String.valueOf(INSTANCE.goldBuildingWandRadius));
        writeData(fos, "diamondBuildingWandRadius", String.valueOf(INSTANCE.diamondBuildingWandRadius));
        writeData(fos, "netheriteBuildingWandRadius", String.valueOf(INSTANCE.netheriteBuildingWandRadius));

        writeData(fos, "whooshWandMultiplier", String.valueOf(INSTANCE.whooshWandMultiplier));

        writeData(fos, "entityMoverBlockSpeed", String.valueOf(INSTANCE.entityMoverBlockSpeed));

        fos.close();
    }

    public static void writeData(FileOutputStream fos, String key, String value) throws IOException {
        fos.write((key + "=" + value).getBytes());
        fos.write("\n".getBytes());
    }

    public static boolean readBoolean(Properties properties, String key, boolean defaultValue) {
        return ((String)properties.computeIfAbsent(key, object -> String.valueOf(defaultValue))).equalsIgnoreCase("true");
    }

    public static float readFloat(Properties properties, String key, float defaultValue) {
        return Float.parseFloat((String) properties.computeIfAbsent(key, object -> String.valueOf(defaultValue)));
    }

    public static double readDouble(Properties properties, String key, double defaultValue) {
        return Double.parseDouble((String) properties.computeIfAbsent(key, object -> String.valueOf(defaultValue)));
    }

    public static int readInt(Properties properties, String key, int defaultValue) {
        return Integer.parseInt((String) properties.computeIfAbsent(key, object -> String.valueOf(defaultValue)));
    }

    public static String readString(Properties properties, String key, String defaultValue) {
        return (String) properties.computeIfAbsent(key, object -> defaultValue);
    }
}
