package com.fabbe50.fabsbnb.data;

import net.minecraft.world.item.ToolMaterial;

public enum ToolMaterialScanRange {
    WOOD(ToolMaterial.WOOD, 1),
    STONE(ToolMaterial.STONE, 2),
    IRON(ToolMaterial.IRON, 5),
    GOLD(ToolMaterial.GOLD, 7),
    DIAMOND(ToolMaterial.DIAMOND, 9),
    NETHERITE(ToolMaterial.NETHERITE, 11);

    private final ToolMaterial material;
    private final int scanRange;
    ToolMaterialScanRange(ToolMaterial tier, int scanRange) {
        this.material = tier;
        this.scanRange = scanRange;
    }

    public ToolMaterial getMaterial() {
        return material;
    }

    public int getScanRange() {
        return scanRange;
    }

    public static ToolMaterialScanRange getScanRangeFromToolTier(ToolMaterial material) {
        for (ToolMaterialScanRange tierScanRange : values()) {
            if (tierScanRange.material == material) {
                return tierScanRange;
            }
        }
        return WOOD;
    }
}
