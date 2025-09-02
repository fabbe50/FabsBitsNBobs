package com.fabbe50.fabsbnb.world.item.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class ModTieredItem extends Item {
    private final ToolMaterial material;

    public ModTieredItem(ToolMaterial material, Properties properties) {
        super(properties.durability(material.durability()));
        this.material = material;
    }

    public ToolMaterial getMaterial() {
        return material;
    }
}
