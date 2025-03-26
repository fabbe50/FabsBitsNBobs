package com.fabbe50.fabsbnb.registries.client;

import com.fabbe50.fabsbnb.client.gui.screen.container.BlockBreakerScreen;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import dev.architectury.registry.menu.MenuRegistry;

public class ClientRegistries {
    public static void init() {
        MenuRegistry.registerScreenFactory(ModRegistries.BLOCK_BREAKER_MENU.get(), BlockBreakerScreen::new);
    }
}
