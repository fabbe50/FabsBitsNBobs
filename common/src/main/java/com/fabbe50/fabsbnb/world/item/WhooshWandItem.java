package com.fabbe50.fabsbnb.world.item;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.ModConfig;
import com.fabbe50.fabsbnb.util.LangUtils;
import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.world.item.base.ModItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class WhooshWandItem extends ModItem {
    public WhooshWandItem(Properties properties) {
        super(properties.stacksTo(1).durability(ModConfig.whooshWandDurability.getValue()).enchantable(15));
    }

    @Override
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        FabsBnB.log(String.valueOf(ModConfig.debugMode.getValue()));
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ItemStack stack = serverPlayer.getItemInHand(interactionHand);
            if (!serverPlayer.getAbilities().instabuild) {
                stack.hurtAndBreak(1, serverPlayer, Utilities.convertInteractionHandToEquipmentSlot(interactionHand));
            }
            Vec3 direction = player.getViewVector(1);
            double deltaMultiplier = 1.5 + ModConfig.whooshWandMultiplier.getValue();
            player.setDeltaMovement(direction.x() * deltaMultiplier, direction.y() * deltaMultiplier, direction.z() * deltaMultiplier);
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(player));
            player.resetFallDistance();
            level.playSound(null, player.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.5f, 0.125f);
            int cooldown = ModConfig.whooshWandCooldown.getValue();
            if (cooldown > 0) {
                player.getCooldowns().addCooldown(stack, cooldown);
            }
            return InteractionResult.SUCCESS.heldItemTransformedTo(stack);
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
        consumer.accept(LangUtils.getDescription(this));
    }
}
