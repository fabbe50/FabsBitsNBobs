package com.fabbe50.fabsbnb.mixin;

import com.fabbe50.fabsbnb.ModConfig;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultServerData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VaultBlock.class)
public class VaultBlockMixin {
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private static void injectUseItemOn(ItemStack stack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (level instanceof ServerLevel) {
            if (ModConfig.vaultUnlocking.getValue()) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof VaultBlockEntity vaultblockentity) {
                    VaultServerData vaultServerData = vaultblockentity.getServerData();
                    if (stack.is(ModRegistries.VAULT_UNLOCKERS) && vaultServerData != null && vaultServerData.hasRewardedPlayer(player)) {
                        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                        stack.consume(vaultblockentity.getConfig().keyItem().getCount(), player);
                        vaultServerData.rewardedPlayers.remove(player.getUUID());
                        vaultServerData.markChanged();
                        cir.setReturnValue(InteractionResult.SUCCESS);
                    }
                }
            }
        }
    }
}
