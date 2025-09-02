package com.fabbe50.fabsbnb.world.item;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class DrinkItem extends Item {
    private static final int DEFAULT_DRINK_DURATION = 40;

    public DrinkItem(Properties properties) {
        this(properties, DEFAULT_DRINK_DURATION);
    }

    public DrinkItem(Properties properties, int drinkDuration) {
        super(properties
                .stacksTo(16)
                .component(
                        DataComponents.CONSUMABLE,
                        Consumable.builder()
                                .consumeSeconds(drinkDuration / 20f)
                                .sound(SoundEvents.GENERIC_DRINK)
                                .animation(ItemUseAnimation.DRINK)
                                .hasConsumeParticles(false)
                                .onConsume(new HandleDrink(1))
                                .build()
                )
                .usingConvertsTo(Items.GLASS_BOTTLE));
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
    public @NotNull InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        return ItemUtils.startUsingInstantly(level, player, interactionHand);
    }

    public record HandleDrink(int i) implements ConsumeEffect {
        public static final MapCodec<HandleDrink> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.intRange(0, 1).optionalFieldOf("zeroOrOne", 1).forGetter(HandleDrink::i)).apply(instance, HandleDrink::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, HandleDrink> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, HandleDrink::i, HandleDrink::new);

        @Override
        public @NotNull Type<? extends ConsumeEffect> getType() {
            return ModRegistries.HANDLE_DRINK.get();
        }

        @Override
        public boolean apply(Level level, ItemStack itemStack, LivingEntity livingEntity) {
            Item item = itemStack.getItem();
            if (item instanceof DrinkItem drinkItem) {
                drinkItem.applyEffects(livingEntity);
            }
            return false;
        }
    }
}
