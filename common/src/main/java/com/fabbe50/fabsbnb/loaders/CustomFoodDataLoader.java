package com.fabbe50.fabsbnb.loaders;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.data.CustomFoodData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class CustomFoodDataLoader extends SimpleJsonResourceReloadListener<JsonElement> {
    public static final CustomFoodDataLoader INSTANCE = new CustomFoodDataLoader();

    private final Map<ResourceLocation, CustomFoodData> dataMap = new HashMap<>();

    public CustomFoodDataLoader() {
        super(ExtraCodecs.JSON, FileToIdConverter.json("food_data"));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        dataMap.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
            try {
                CustomFoodData data = CustomFoodData.fromJson(entry.getValue().getAsJsonObject());
                dataMap.put(entry.getKey(), data);
            } catch (Exception e) {
                FabsBnB.error("Failed to load custom food data: " + entry.getKey());
            }
        }
        FabsBnB.log("Loaded " + dataMap.size() + " custom food data entries");
    }

    public Map<ResourceLocation, CustomFoodData> getDataMap() {
        return dataMap;
    }
}
