package com.fabbe50.fabsbnb.world.block.entity;

import com.fabbe50.fabsbnb.Utilities;
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
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class XPHolderBlockEntity extends BlockEntity {
    public int time;
    public float rot;
    public float oRot;
    public float tRot;

    private int xp;
    private boolean collectXP;
    private int range;

    public XPHolderBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public XPHolderBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ModRegistries.XP_HOLDER_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putInt("xp", this.xp);
        compoundTag.putInt("range", this.range);
        compoundTag.putBoolean("collect", this.collectXP);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.xp = compoundTag.getInt("xp");
        this.range = compoundTag.getInt("range");
        this.collectXP = compoundTag.getBoolean("collect");
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
        if (this.getLevel() != null) {
            this.oRot = this.rot;
            Player player = this.getLevel().getNearestPlayer((double) getBlockPos().getX() + (double) 0.5F, (double) getBlockPos().getY() + (double) 0.5F, (double) getBlockPos().getZ() + (double) 0.5F, 3.0F, false);
            if (player != null) {
                double d = player.getX() - ((double)getBlockPos().getX() + (double)0.5F);
                double e = player.getZ() - ((double)getBlockPos().getZ() + (double)0.5F);
                this.tRot = (float) Mth.atan2(e, d);
            } else {
                this.tRot += 0.02F;
            }
            while(this.rot >= (float)Math.PI) {
                this.rot -= ((float)Math.PI * 2F);
            }
            while(this.rot < -(float)Math.PI) {
                this.rot += ((float)Math.PI * 2F);
            }
            while(this.tRot >= (float)Math.PI) {
                this.tRot -= ((float)Math.PI * 2F);
            }
            while(this.tRot < -(float)Math.PI) {
                this.tRot += ((float)Math.PI * 2F);
            }

            float g;
            for(g = this.tRot - this.rot; g >= (float)Math.PI; g -= ((float)Math.PI * 2F)) {
            }
            while(g < -(float)Math.PI) {
                g += ((float)Math.PI * 2F);
            }

            this.rot += g * 0.4F;
            this.time++;
        }
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

        range = 5;

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

    private void markDirty() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(getBlockPos());
            level.sendBlockUpdated(getBlockPos(), state, state, 3);
        }
    }
}
