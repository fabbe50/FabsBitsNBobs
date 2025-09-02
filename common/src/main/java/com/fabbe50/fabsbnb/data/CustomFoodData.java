package com.fabbe50.fabsbnb.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record CustomFoodData(ResourceLocation location, int nutrition, float saturation, boolean alwaysEdible, List<MobEffectData> mobEffectInstances) {
    public static final MapCodec<CustomFoodData> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("location").forGetter(CustomFoodData::location),
                    Codec.INT.fieldOf("nutrition").forGetter(CustomFoodData::nutrition),
                    Codec.FLOAT.fieldOf("saturation").forGetter(CustomFoodData::saturation),
                    Codec.BOOL.fieldOf("alwaysEdible").forGetter(CustomFoodData::alwaysEdible),
                    Codec.list(MobEffectData.CODEC.codec()).fieldOf("mobEffectInstances").forGetter(CustomFoodData::mobEffectInstances)
            ).apply(instance, CustomFoodData::new));

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
        return new CustomFoodData(location, nutrition, saturation, alwaysEdible, mobEffectList);
    }

    public record MobEffectData(ResourceLocation location, int duration, int power) {
        public static final MapCodec<MobEffectData> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("location").forGetter(MobEffectData::location),
                        Codec.INT.fieldOf("duration").forGetter(MobEffectData::duration),
                        Codec.INT.fieldOf("power").forGetter(MobEffectData::power)
                ).apply(instance, MobEffectData::new));

        public static MobEffectData fromJson(JsonObject json) {
            ResourceLocation location = ResourceLocation.parse(json.get("location").getAsString());
            int duration = json.get("duration").getAsInt();
            int power = json.get("power").getAsInt();
            return new MobEffectData(location, duration, power);
        }
    }
}
