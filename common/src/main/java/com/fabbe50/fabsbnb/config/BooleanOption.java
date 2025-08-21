package com.fabbe50.fabsbnb.config;

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;

import java.util.Properties;

public class BooleanOption extends AbstractConfigOption<Boolean, BooleanListEntry> {
    private boolean requiresRestart = false;

    public BooleanOption(String name, Boolean defaultValue) {
        super(name, defaultValue);
    }

    public BooleanOption(String name, Boolean defaultValue, Boolean value) {
        super(name, defaultValue, value);
    }

    @Override
    public BooleanListEntry buildClothEntry(ConfigEntryBuilder builder) {
        BooleanToggleBuilder booleanBuilder = builder.startBooleanToggle(getTranslation(), getValue())
                .setDefaultValue(getDefaultValue())
                .setTooltip(getTooltipTranslation())
                .setSaveConsumer(this::setValue);
        booleanBuilder.requireRestart(requiresRestart);
        return booleanBuilder.build();
    }

    @Override
    public void readData(Properties properties) {
        setValue(Boolean.parseBoolean((String) properties.computeIfAbsent(getKey(), o -> String.valueOf(getDefaultValue()))));
    }

    public static class Builder {
        String name;
        boolean defaultValue;
        boolean requiresRestart;

        public Builder(String name, boolean defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.requiresRestart = true;
        }

        public Builder requiresRestart() {
            this.requiresRestart = true;
            return this;
        }

        public BooleanOption build() {
            BooleanOption option = new BooleanOption(name, defaultValue, defaultValue);
            option.requiresRestart = requiresRestart;
            return option;
        }
    }
}
