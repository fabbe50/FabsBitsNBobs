package com.fabbe50.fabsbnb.neoforge;

import com.fabbe50.fabsbnb.ClothScreen;
import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.FabsBnBClient;
import com.fabbe50.fabsbnb.Platform;
import com.fabbe50.fabsbnb.client.gui.screen.container.BlockBreakerScreen;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.registries.PotionBrewingRecipes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@Mod(value = FabsBnB.MOD_ID, dist = Dist.CLIENT)
public class FabsBnBNeoForgeClient {
    public FabsBnBNeoForgeClient(ModContainer container) {
        FabsBnBClient.initClient();

        if (Platform.isModLoaded("cloth_config")) {
            FabsBnB.log("Registering extension point for cloth config screen...");
            container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, screen) -> ClothScreen.getConfigScreen(screen));
        }
    }

    @EventBusSubscriber(modid = FabsBnB.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class Events {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModRegistries.BLOCK_BREAKER_MENU.get(), BlockBreakerScreen::new);
        }
    }
}
