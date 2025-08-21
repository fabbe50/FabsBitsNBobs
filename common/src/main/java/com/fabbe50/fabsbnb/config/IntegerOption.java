package com.fabbe50.fabsbnb.config;

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import me.shedaniel.clothconfig2.impl.builders.IntFieldBuilder;
import net.minecraft.network.chat.Component;

import java.util.Properties;

public class IntegerOption extends AbstractRangedConfigOption<Integer, IntegerListEntry> {
    private boolean requiresRestart = false;

    public IntegerOption(String name, Integer defaultValue) {
        this(name, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerOption(String name, Integer defaultValue, Integer min, Integer max) {
        this(name, defaultValue, defaultValue, min, max);
    }

    public IntegerOption(String name, Integer defaultValue, Integer value, Integer min, Integer max) {
        super(name, defaultValue, value, min, max);
    }

    @Override
    public IntegerListEntry buildClothEntry(ConfigEntryBuilder builder) {
        IntFieldBuilder field = builder.startIntField(getTranslation(), getValue())
                .setDefaultValue(getDefaultValue())
                .setMin(min())
                .setMax(max())
                .setTooltip(getTooltipTranslation())
                .setSaveConsumer(this::setValue);
        field.requireRestart(requiresRestart);
        return field.build();
    }

    @Override
    public void readData(Properties properties) {
        setValue(Integer.parseInt((String) properties.computeIfAbsent(getKey(), o -> String.valueOf(getDefaultValue()))));
    }

    public static class Builder {
        String name;
        int defaultValue;
        int minValue;
        int maxValue;
        boolean requiresRestart;

        public Builder(String name, int defaultValue) {
            this(name, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        public Builder(String name, int defaultValue, int min, int max) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.minValue = min;
            this.maxValue = max;
            this.requiresRestart = true;
        }

        public IntegerOption.Builder requiresRestart() {
            this.requiresRestart = true;
            return this;
        }

        public IntegerOption.Builder min(int min) {
            this.minValue = min;
            return this;
        }

        public IntegerOption.Builder max(int max) {
            this.maxValue = max;
            return this;
        }

        public IntegerOption build() {
            IntegerOption option = new IntegerOption(name, defaultValue, minValue, maxValue);
            option.requiresRestart = requiresRestart;
            return option;
        }
    }
}
