package com.fabbe50.fabsbnb.registries.client;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.client.gui.screen.container.BlockBreakerScreen;
import com.fabbe50.fabsbnb.client.renderer.blockentity.XPHolderRenderer;
import com.fabbe50.fabsbnb.loaders.CustomFoodDataLoader;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.server.packs.PackType;

public class ClientRegistries {
    public static void init() {
        if (Platform.isFabric()) {
            MenuRegistry.registerScreenFactory(ModRegistries.BLOCK_BREAKER_MENU.get(), BlockBreakerScreen::new);
            registerBlockRenderer();
        }
        ReloadListenerRegistry.register(PackType.SERVER_DATA, CustomFoodDataLoader.INSTANCE, FabsBnB.location("custom_food"));
    }

    public static void registerBlockRenderer() {
        BlockEntityRendererRegistry.register(ModRegistries.XP_HOLDER_BLOCK_ENTITY.get(), XPHolderRenderer::new);
        RenderTypeRegistry.register(ChunkSectionLayer.TRANSLUCENT, ModRegistries.STRUCTURAL_GOOP.get());
        RenderTypeRegistry.register(ChunkSectionLayer.TRANSLUCENT, ModRegistries.STRUCTURAL_GLASS.get());
    }
}
