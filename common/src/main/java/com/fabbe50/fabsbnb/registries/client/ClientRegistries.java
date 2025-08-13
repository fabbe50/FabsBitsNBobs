package com.fabbe50.fabsbnb.registries.client;

import com.fabbe50.fabsbnb.client.gui.screen.container.BlockBreakerScreen;
import com.fabbe50.fabsbnb.client.renderer.blockentity.XPHolderRenderer;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;

public class ClientRegistries {
    public static void init() {
        ClientLifecycleEvent.CLIENT_SETUP.register(minecraft -> {
            MenuRegistry.registerScreenFactory(ModRegistries.BLOCK_BREAKER_MENU.get(), BlockBreakerScreen::new);
            BlockEntityRendererRegistry.register(ModRegistries.XP_HOLDER_BLOCK_ENTITY.get(), XPHolderRenderer::new);
        });
    }
}
