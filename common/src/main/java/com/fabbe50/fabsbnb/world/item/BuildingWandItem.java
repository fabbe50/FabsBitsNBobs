package com.fabbe50.fabsbnb.world.item;

import com.fabbe50.fabsbnb.util.LangUtils;
import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.world.item.base.ModTieredItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class BuildingWandItem extends ModTieredItem {
    public BuildingWandItem(Tier tier, Properties properties) {
        super(tier, properties.stacksTo(1).durability(tier.getUses() * Utilities.square(Utilities.getRadiusFromTier(tier))));
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

            CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();

            if (player.isShiftKeyDown()) {
                if (state.getMenuProvider(level, pos) == null) {
                    ResourceKey<Block> blockKey = state.getBlockHolder().unwrapKey().orElse(null);
                    if (blockKey != null) {
                        tag.putString("setBlock", blockKey.location().toString());
                        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                        player.setItemInHand(useOnContext.getHand(), stack);
                        ((ServerPlayer)player).sendSystemMessage(LangUtils.withValue(SET_BLOCK_KEY, LangUtils.getComponent(state)), true);
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
                                    ((ServerPlayer) player).sendSystemMessage(LangUtils.error(NOT_ENOUGH_BLOCKS_ABORT), true);
                                    return;
                                }
                            }
                            BlockState blockStateToPlace = blockToPlace.defaultBlockState();
                            if (level.setBlock(mutableBlockPos, blockStateToPlace, 3)) {
                                placedAt.add(mutableBlockPos.asLong());
                                placedAtAtomic.set(placedAt);
                                level.playSound(null, mutableBlockPos, blockStateToPlace.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 0.5f, 0.5f + level.random.nextFloat());
                                if (!player.getAbilities().instabuild) {
                                    stack.hurtAndBreak(1, player, Utilities.convertInteractionHandToEquipmentSlot(InteractionHand.MAIN_HAND));
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
                CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                boolean fuzzy = false;
                if (tag.contains("fuzzy")) {
                    fuzzy = !tag.getBoolean("fuzzy");
                } else {
                    fuzzy = true;
                }
                tag.putBoolean("fuzzy", fuzzy);
                ((ServerPlayer) player).sendSystemMessage(LangUtils.conditionWithStyle(FUZZY_TOGGLE, fuzzy), true);

                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                player.setItemInHand(interactionHand, stack);
                return InteractionResultHolder.success(stack);
            }
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);

        CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        String blockSelected = LangUtils.EMPTY;
        if (tag.contains("setBlock")) {
            blockSelected = tag.getString("setBlock");
        }
        boolean fuzzy = false;
        if (tag.contains("fuzzy")) {
            fuzzy = tag.getBoolean("fuzzy");
        }

        list.add(LangUtils.withValue(SELECTED_BLOCK_KEY, blockSelected));
        list.add(LangUtils.conditionWithStyle(FUZZY_TOGGLE, fuzzy));
    }

    public static final String SELECTED_BLOCK_KEY = LangUtils.getTextKey("building_wand.selected_block");
    public static final String SET_BLOCK_KEY = LangUtils.getTextKey("building_wand.set_block");
    public static final String NOT_ENOUGH_BLOCKS_ABORT = LangUtils.getTextKey("building_wand.not_enough_blocks.abort");
    public static final String NOT_ENOUGH_BLOCKS_INVENTORY = LangUtils.getTextKey("building_wand.not_enough_blocks.inventory");
    public static final String FUZZY_TOGGLE = LangUtils.getTextKey("building_wand.fuzzy_toggle");
}
