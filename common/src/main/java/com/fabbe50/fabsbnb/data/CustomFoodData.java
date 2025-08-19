package com.fabbe50.fabsbnb.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record CustomFoodData(ResourceLocation location, int nutrition, float saturation, boolean alwaysEdible, MobEffectData[] mobEffectInstances) {
    public static CustomFoodData fromJson(JsonObject json) {
        ResourceLocation location = ResourceLocation.parse(json.get("location").getAsString());
        int nutrition = json.get("nutrition").getAsInt();
        float saturation = json.get("saturation").getAsFloat();
        boolean alwaysEdible = json.get("alwaysEdible").getAsBoolean();
        JsonArray mobEffectArray = json.get("mobEffects").getAsJsonArray();
        List<MobEffectData> mobEffectList = new ArrayList<>();
        for (JsonElement mobEffectLocation : mobEffectArray.asList()) {
            mobEffectList.add(MobEffectData.fromJson(mobEffectLocation.getAsJsonObject()));
        }
        MobEffectData[] mobEffectInstances = mobEffectList.toArray(new MobEffectData[]{});
        return new CustomFoodData(location, nutrition, saturation, alwaysEdible, mobEffectInstances);
    }

    public record MobEffectData(ResourceLocation location, int duration, int power) {
        public static MobEffectData fromJson(JsonObject json) {
            ResourceLocation location = ResourceLocation.parse(json.get("location").getAsString());
            int duration = json.get("duration").getAsInt();
            int power = json.get("power").getAsInt();
            return new MobEffectData(location, duration, power);
        }
    }
}
