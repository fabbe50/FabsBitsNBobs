package com.fabbe50.fabsbnb.data;

import net.minecraft.world.item.Tiers;

public enum ToolTierScanRange {
    WOOD(Tiers.WOOD, 1),
    STONE(Tiers.STONE, 2),
    IRON(Tiers.IRON, 5),
    GOLD(Tiers.GOLD, 7),
    DIAMOND(Tiers.DIAMOND, 9),
    NETHERITE(Tiers.NETHERITE, 11);

    private final Tiers tier;
    private final int scanRange;
    ToolTierScanRange(Tiers tier, int scanRange) {
        this.tier = tier;
        this.scanRange = scanRange;
    }

    public Tiers getTier() {
        return tier;
    }

    public int getScanRange() {
        return scanRange;
    }

    public static ToolTierScanRange getScanRangeFromToolTier(Tiers tier) {
        for (ToolTierScanRange tierScanRange : values()) {
            if (tierScanRange.tier == tier) {
                return tierScanRange;
            }
        }
        return WOOD;
    }
}
