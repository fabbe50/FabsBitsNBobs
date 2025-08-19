package com.fabbe50.fabsbnb.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ItemStackEvent {
    Event<Created> CREATED = EventFactory.createEventResult();
    Event<InventoryTick> INVENTORY_TICK = EventFactory.createEventResult();

    interface Created {
        void onStackCreated(ItemStack stack);
    }

    interface InventoryTick {
        void inventoryTick(ItemStack stack, Level level, Entity entity);
    }
}
