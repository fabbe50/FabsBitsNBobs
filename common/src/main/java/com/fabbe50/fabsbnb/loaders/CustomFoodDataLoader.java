package com.fabbe50.fabsbnb.loaders;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.data.CustomFoodData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

public class CustomFoodDataLoader extends SimpleJsonResourceReloadListener {
    public static final CustomFoodDataLoader INSTANCE = new CustomFoodDataLoader();

    private final Map<ResourceLocation, CustomFoodData> dataMap = new HashMap<>();

    public CustomFoodDataLoader() {
        super(new Gson(), "food_data");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        dataMap.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : elements.entrySet()) {
            try {
                JsonObject object = entry.getValue().getAsJsonObject();
                CustomFoodData data = CustomFoodData.fromJson(object);
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
