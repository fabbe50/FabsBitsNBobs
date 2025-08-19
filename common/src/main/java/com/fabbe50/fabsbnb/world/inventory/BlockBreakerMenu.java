package com.fabbe50.fabsbnb.world.inventory;

import com.fabbe50.fabsbnb.registries.ModRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class BlockBreakerMenu extends AbstractContainerMenu {
    private static final int SLOT_COUNT = 1;
    private final Container container;

    public BlockBreakerMenu(int i, Inventory inventory, FriendlyByteBuf buf) {
        this(i, inventory, new SimpleContainer(SLOT_COUNT));
    }

    public BlockBreakerMenu(int i, Inventory inventory, Container container) {
        super(ModRegistries.BLOCK_BREAKER_MENU.get(), i);

        checkContainerSize(container, SLOT_COUNT);
        this.container = container;

        this.addSlot(new ToolSlot(container, 0, 62 + 18, 17 + 18));

        for(int j = 0; j < 3; j++) {
            for(int k = 0; k < 9; k++) {
                this.addSlot(new Slot(inventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for(int j = 0; j < 9; j++) {
            this.addSlot(new Slot(inventory, j, 8 + j * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (i == 0) {
                if (!this.moveItemStackTo(itemStack2, 1, 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (BlockBreakerMenu.ToolSlot.mayPlaceItem(itemStack)) {
                if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
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

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    static class ToolSlot extends Slot {
        public ToolSlot(Container container, int i, int j, int k) {
            super(container, i, j, k);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return mayPlaceItem(stack);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        public static boolean mayPlaceItem(ItemStack itemStack) {
            return itemStack.getItem() instanceof DiggerItem;
        }
    }
}
