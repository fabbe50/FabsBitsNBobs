package com.fabbe50.fabsbnb.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class BlockPlacerMenu extends AbstractContainerMenu {
    private static final int SLOT_COUNT = 9;
    private final Container container;

    public BlockPlacerMenu(int i, Inventory inventory, Container container) {
        super(MenuType.GENERIC_3x3, i);
        checkContainerSize(container, SLOT_COUNT);
        this.container = container;
        container.startOpen(inventory.player);

        for(int j = 0; j < 3; ++j) {
            for(int k = 0; k < 3; ++k) {
                this.addSlot(new BlockSlot(container, k + j * 3, 62 + k * 18, 17 + j * 18));
            }
        }

        for(int j = 0; j < 3; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for(int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
        }
    }

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (i < 9) {
                if (!this.moveItemStackTo(itemStack2, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (BlockPlacerMenu.BlockSlot.mayPlaceItem(itemStack)) {
                if (!this.moveItemStackTo(itemStack2, 0, 9, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack2);
        }

        return itemStack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    static class BlockSlot extends Slot {
        public BlockSlot(Container container, int i, int j, int k) {
            super(container, i, j, k);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return mayPlaceItem(stack);
        }

        public static boolean mayPlaceItem(ItemStack itemStack) {
            return itemStack.getItem() instanceof BlockItem;
        }
    }
}
