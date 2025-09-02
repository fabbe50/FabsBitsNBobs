package com.fabbe50.fabsbnb.world.block.entity;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.util.LangUtils;
import com.fabbe50.fabsbnb.world.inventory.BlockBreakerMenu;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

public class BlockBreakerBlockEntity extends RandomizableContainerBlockEntity {
    public static final int CONTAINER_SIZE = 1;
    private NonNullList<ItemStack> items;

    public BlockBreakerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    }

    public BlockBreakerBlockEntity(BlockPos pos, BlockState state) {
        this(ModRegistries.BLOCK_BREAKER_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return LangUtils.getContainerComponent("block_breaker");
    }

    @Override
    public void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(valueInput)) {
            ContainerHelper.loadAllItems(valueInput, this.items);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        if (!this.trySaveLootTable(valueOutput)) {
            ContainerHelper.saveAllItems(valueOutput, this.items);
        }
    }

    @Override
    public boolean canTakeItem(Container container, int i, ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem) {
            return super.canTakeItem(container, i, itemStack);
        }
        return false;
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new BlockBreakerMenu(i, inventory, this);
    }
}
