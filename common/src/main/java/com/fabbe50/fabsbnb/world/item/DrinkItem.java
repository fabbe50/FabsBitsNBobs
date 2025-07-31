package com.fabbe50.fabsbnb.world.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class DrinkItem extends Item {
    private static final int DEFAULT_DRINK_DURATION = 40;
    private final int DRINK_DURATION;

    public DrinkItem(Properties properties) {
        this(properties.stacksTo(16), DEFAULT_DRINK_DURATION);
    }

    public DrinkItem(Properties properties, int drinkDuration) {
        super(properties);
        this.DRINK_DURATION = drinkDuration;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        super.finishUsingItem(itemStack, level, livingEntity);
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }
        if (!level.isClientSide) {
            applyEffects(livingEntity);
        }
        if (itemStack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (livingEntity instanceof Player player && !player.getAbilities().instabuild) {
                ItemStack stack = new ItemStack(Items.GLASS_BOTTLE);
                if (!player.getInventory().add(stack)) {
                    player.drop(stack, false);
                }
            }
            return itemStack;
        }
    }

    abstract void applyEffects(LivingEntity livingEntity);

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return DRINK_DURATION;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        return ItemUtils.startUsingInstantly(level, player, interactionHand);
    }
}
