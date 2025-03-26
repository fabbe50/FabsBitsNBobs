package com.fabbe50.fabsbnb.registries.client;

import dev.architectury.event.events.client.ClientTooltipEvent;

public class ClientEvents {
    public static void register() {
        ClientTooltipEvent.ITEM.register((itemStack, list, tooltipFlag) -> {

        });
    }
}
