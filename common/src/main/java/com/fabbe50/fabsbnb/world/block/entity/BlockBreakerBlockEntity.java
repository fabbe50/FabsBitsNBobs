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
        return Component.translatable(FabsBnB.translation("container.block_breaker"));
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compoundTag)) {
            ContainerHelper.loadAllItems(compoundTag, this.items, provider);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        if (!this.trySaveLootTable(compoundTag)) {
            ContainerHelper.saveAllItems(compoundTag, this.items, provider);
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
