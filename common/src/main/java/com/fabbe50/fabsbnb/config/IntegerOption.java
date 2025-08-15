package com.fabbe50.fabsbnb.config;

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import net.minecraft.network.chat.Component;

import java.util.Properties;

public class IntegerOption extends AbstractRangedConfigOption<Integer, IntegerListEntry> {
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
        return builder.startIntField(getTranslation(), getValue())
                .setDefaultValue(getDefaultValue())
                .setMin(min())
                .setMax(max())
                .setTooltip(getTooltipTranslation())
                .setSaveConsumer(this::setValue)
                .build();
    }

    @Override
    public void readData(Properties properties) {
        setValue(Integer.parseInt((String) properties.computeIfAbsent(getKey(), o -> String.valueOf(getDefaultValue()))));
    }
}
