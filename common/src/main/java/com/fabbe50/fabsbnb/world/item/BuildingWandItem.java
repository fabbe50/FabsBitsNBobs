package com.fabbe50.fabsbnb.world.item;

import com.fabbe50.fabsbnb.Utilities;
import com.fabbe50.fabsbnb.world.item.base.ModTieredItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class BuildingWandItem extends ModTieredItem {
    public BuildingWandItem(Tier tier, Properties properties) {
        super(tier, properties.stacksTo(1).defaultDurability(tier.getUses() * Utilities.square(Utilities.getRadiusFromTier(tier))));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack stack = useOnContext.getItemInHand();
            BlockPos pos = useOnContext.getClickedPos();
            Direction face = useOnContext.getClickedFace();
            BlockState state = level.getBlockState(pos);
            Player player = useOnContext.getPlayer();
            if (player == null) {
                return InteractionResult.FAIL;
            }
            Tier tier = this.getTier();

            CompoundTag tag = stack.getOrCreateTag();

            if (player.isShiftKeyDown()) {
                if (state.getMenuProvider(level, pos) == null) {
                    ResourceKey<Block> blockKey = state.getBlockHolder().unwrapKey().orElse(null);
                    if (blockKey != null) {
                        tag.putString("setBlock", blockKey.location().toString());
                        stack.setTag(tag);
                        player.setItemInHand(useOnContext.getHand(), stack);
                        ((ServerPlayer)player).sendSystemMessage(Component.translatable("item.fabsbnb.building-wand.set-block", Component.literal(blockKey.location().toString()).withStyle(ChatFormatting.GOLD)), true);
                        return InteractionResult.SUCCESS;
                    }
                }
                return InteractionResult.FAIL;
            } else if (tag.contains("setBlock")) {
                String stringLocation = tag.getString("setBlock");
                boolean fuzzy;
                if (tag.contains("fuzzy")) {
                    fuzzy = tag.getBoolean("fuzzy");
                } else {
                    fuzzy = false;
                }
                ResourceLocation location = ResourceLocation.tryParse(stringLocation);
                if (location == null) {
                    return InteractionResult.FAIL;
                }
                Holder.Reference<Block> blockReference = Optional.ofNullable(ResourceLocation.tryParse(tag.getString("setBlock")))
                        .map(location1 -> ResourceKey.create(Registries.BLOCK, location1))
                        .flatMap(resourceKey -> level.registryAccess().registryOrThrow(Registries.BLOCK).getHolder(resourceKey))
                        .orElse(null);
                if (blockReference == null) {
                    return InteractionResult.FAIL;
                }
                Block blockToPlace = blockReference.value();

                int maxRadius = Utilities.getRadiusFromTier(tier);

                Direction direction1 = face.getAxis().equals(Direction.Axis.X) || face.getAxis().equals(Direction.Axis.Z) ? Direction.UP : Direction.NORTH;
                Direction direction2 = face.getAxis().equals(Direction.Axis.Y) ? Direction.EAST : (face.getAxis().equals(Direction.Axis.X) ? Direction.NORTH : Direction.EAST);

                Iterable<BlockPos.MutableBlockPos> positions = BlockPos.spiralAround(pos.relative(face), maxRadius, direction1, direction2);
                AtomicReference<Set<Long>> placedAtAtomic = new AtomicReference<>(new HashSet<>());
                positions.forEach(mutableBlockPos -> {
                    BlockState blockToPlaceOn = level.getBlockState(mutableBlockPos.relative(face.getOpposite()));
                    Set<Long> placedAt = placedAtAtomic.get();
                    if (placedAt.isEmpty() || (checkSurroundingBlocksIsVisited(placedAt, mutableBlockPos) || fuzzy)) {
                        if ((state.is(blockToPlaceOn.getBlock()) || (fuzzy && !(blockToPlaceOn.isAir() || blockToPlaceOn.canBeReplaced()))) && blockToPlaceOn.isFaceSturdy(level, mutableBlockPos.relative(face.getOpposite()), face) && (level.getBlockState(mutableBlockPos).canBeReplaced() || level.getBlockState(mutableBlockPos).isAir())) {
                            if (!player.getAbilities().instabuild) {
                                if (player.getInventory().hasAnyOf(Set.of(blockToPlace.asItem()))) {
                                    int slot = player.getInventory().findSlotMatchingItem(new ItemStack(blockToPlace.asItem()));
                                    player.getInventory().removeItem(slot, 1);
                                } else {
                                    ((ServerPlayer) player).sendSystemMessage(Component.translatable("item.fabsbnb.building-wand.not-enough-blocks").withStyle(ChatFormatting.RED), true);
                                    return;
                                }
                            }
                            if (level.setBlock(mutableBlockPos, blockToPlace.defaultBlockState(), 3)) {
                                placedAt.add(mutableBlockPos.asLong());
                                placedAtAtomic.set(placedAt);
                                level.playSound(null, mutableBlockPos, blockToPlace.getSoundType(blockToPlace.defaultBlockState()).getPlaceSound(), SoundSource.BLOCKS, 0.5f, 0.5f + level.random.nextFloat());
                                if (!player.getAbilities().instabuild) {
                                    stack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                                }
                            }
                        }
                    }
                });
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private boolean checkSurroundingBlocksIsVisited(Set<Long> visited, BlockPos posToTestAround) {
        AABB testArea = new AABB(posToTestAround).inflate(1);
        return visited.stream().anyMatch(pos -> testArea.contains(BlockPos.of(pos).getCenter()));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level instanceof ServerLevel) {
            if (player.isShiftKeyDown() && !player.isUsingItem()) {
                ItemStack stack = player.getItemInHand(interactionHand);
                CompoundTag tag = stack.getOrCreateTag();
                boolean fuzzy = false;
                if (tag.contains("fuzzy")) {
                    fuzzy = !tag.getBoolean("fuzzy");
                } else {
                    fuzzy = true;
                }
                tag.putBoolean("fuzzy", fuzzy);
                ((ServerPlayer) player).sendSystemMessage(Component.translatable("text.fabsbnb.building_wand.fuzzy-toggle", fuzzy ? Component.translatable("text.true").withStyle(ChatFormatting.GREEN) : Component.translatable("text.false").withStyle(ChatFormatting.RED)), true);

                stack.setTag(tag);
                player.setItemInHand(interactionHand, stack);
                return InteractionResultHolder.success(stack);
            }
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);

        CompoundTag tag = itemStack.getOrCreateTag();
        String blockSelected = "EMPTY";
        if (tag.contains("setBlock")) {
            blockSelected = tag.getString("setBlock");
        }
        boolean fuzzy = false;
        if (tag.contains("fuzzy")) {
            fuzzy = tag.getBoolean("fuzzy");
        }

        list.add(Component.translatable("item.fabsbnb.building_wand.tooltip.block", Component.literal(blockSelected).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("text.fabsbnb.building_wand.fuzzy-toggle", fuzzy ? Component.translatable("text.true").withStyle(ChatFormatting.GREEN) : Component.translatable("text.false").withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.GRAY));
    }
}
