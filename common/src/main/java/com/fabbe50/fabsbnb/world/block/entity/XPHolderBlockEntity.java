package com.fabbe50.fabsbnb.world.block.entity;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.ModConfig;
import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class XPHolderBlockEntity extends BlockEntity {
    private static final float PI_F = (float)Math.PI;
    private static final float TWO_PI_F = PI_F * 2F;

    public int time;
    public float rot;
    public float oRot;
    public float tRot;

    private int xp;
    private boolean collectXP;

    public XPHolderBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public XPHolderBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ModRegistries.XP_HOLDER_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        valueOutput.putInt("xp", this.xp);
        valueOutput.putBoolean("collect", this.collectXP);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.xp = valueInput.getInt("xp").orElse(0);
        this.collectXP = valueInput.getBooleanOr("collect", false);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        tag.putInt("xp", this.xp);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void tickClient() {
        if (level == null) return;

        this.oRot = this.rot;

        BlockPos pos = this.getBlockPos();
        Player nearest = level.getNearestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 12.0, false);

        if (nearest != null) {
            double dx = nearest.getX() - (pos.getX() + 0.5);
            double dz = nearest.getZ() - (pos.getZ() + 0.5);
            double distSq = dx * dx + dz * dz;

            if (distSq < 9.0) { // player is close
                this.tRot = (float) Math.atan2(dz, dx);
            } else {
                this.tRot += 0.02F; // lazy spin
            }
        } else {
            this.tRot += 0.02F; // lazy spin
        }

        // Smoothly rotate using shortest path
        this.rot = Mth.rotLerp(0.4F, this.rot, this.tRot);
    }

    public void tickServer() {
        if (this.collectXP) {
            this.collectXP();
        }
    }

    public boolean toggleCollectXP() {
        this.collectXP = !this.collectXP;
        return this.collectXP;
    }

    public int addXP(int xp) {
        if (this.xp > Integer.MAX_VALUE - xp) {
            int rest = xp - (Integer.MAX_VALUE - this.xp);
            this.xp = Integer.MAX_VALUE;
            return rest;
        } else {
            this.xp += xp;
            return 0;
        }
    }

    public int removeXP(int xp) {
        int qty = Math.min(this.xp, xp);
        this.xp = this.xp - qty;
        return xp - qty;
    }

    public void storeXP(Player player, int level) {
        if (level == -1) {
            int total = Utilities.getPlayerTotalExperience(player);
            int rest = addXP(total);
            player.giveExperiencePoints(-total);
            player.giveExperienceLevels(-1);
            if (rest > 0) {
                player.giveExperiencePoints(rest);
            }
        } else if (level > 0) {
            int progress = (int) (player.experienceProgress * player.getXpNeededForNextLevel());
            if (player.experienceProgress > 0) {
                int taken = Utilities.removePoints(player, progress);
                int rest = addXP(taken);
                level--;
                player.experienceProgress = 0;
                if (rest > 0) {
                    player.giveExperiencePoints(rest);
                }
            }
            if (level > 0) {
                int taken = Utilities.removeLevels(player, level);
                int rest = addXP(taken);
                if (rest > 0) {
                    player.giveExperiencePoints(rest);
                }
            }
        }
        markDirty();
    }

    public void takeXP(Player player, int level) {
        if (this.xp == 0) {
            return;
        }
        if (level == -1) {
            int toTake = this.xp;
            player.giveExperiencePoints(toTake);
            this.xp = 0;
        } else if (level > 0) {
            if (roundUpToNextLevel(player)) {
                level--;
            }
            if (level > 0 && this.xp > 0) {
                int restProgress = Utilities.getTotalExperienceForLevel(player.experienceLevel + level) - Utilities.getPlayerTotalExperience(player);
                int toTake = Math.min(this.xp, restProgress);
                player.giveExperiencePoints(toTake);
                this.xp -= toTake;
                roundUpToNextLevel(player);
            }
        }
        markDirty();
    }

    public boolean roundUpToNextLevel(Player player) {
        if (this.xp <= 0) {
            return false;
        }
        int progress = (int) (player.experienceProgress * player.getXpNeededForNextLevel());
        if (progress > 0) {
            int toTake = Math.min(this.xp, Utilities.getExpNeededForNextLevel(player));
            player.giveExperiencePoints(toTake);
            this.xp -= toTake;
            return true;
        }
        return false;
    }

    private void collectXP() {
        assert level != null;

        int range = ModConfig.xpHolderCollectionRange.getValue();

        BlockPos pos = this.getBlockPos();
        AABB area = new AABB(pos.getCenter().add(-range, -range, -range), pos.getCenter().add(range, range, range));

        List<ExperienceOrb> orbs = this.level.getEntitiesOfClass(ExperienceOrb.class, area, experienceOrb -> true);
        if (orbs.isEmpty()) {
            return;
        }
        for (ExperienceOrb orb : orbs) {
            int xpAmount = orb.getValue();
            this.addXP(xpAmount);
            orb.discard();
        }
        markDirty();
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    private void markDirty() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(getBlockPos());
            level.sendBlockUpdated(getBlockPos(), state, state, 3);
        }
    }
}
