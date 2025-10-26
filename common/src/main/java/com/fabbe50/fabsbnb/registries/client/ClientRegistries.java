package com.fabbe50.fabsbnb.registries.client;

import com.fabbe50.fabsbnb.client.gui.screen.container.BlockBreakerScreen;
import com.fabbe50.fabsbnb.client.renderer.blockentity.XPHolderRenderer;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class ClientRegistries {
    public static void init() {
        if (Platform.isFabric()) {
            MenuRegistry.registerScreenFactory(ModRegistries.BLOCK_BREAKER_MENU.get(), BlockBreakerScreen::new);
            registerBlockRenderer();
        }
    }

    public static void registerBlockRenderer() {
        BlockEntityRendererRegistry.register(ModRegistries.XP_HOLDER_BLOCK_ENTITY.get(), XPHolderRenderer::new);
        RenderTypeRegistry.register(ChunkSectionLayer.TRANSLUCENT, ModRegistries.STRUCTURAL_GOOP.get());
        RenderTypeRegistry.register(ChunkSectionLayer.TRANSLUCENT, ModRegistries.STRUCTURAL_GLASS.get());
    }
}
