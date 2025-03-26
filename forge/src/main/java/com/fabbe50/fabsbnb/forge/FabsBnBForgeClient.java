package com.fabbe50.fabsbnb.forge;

import com.fabbe50.fabsbnb.ClothScreen;
import com.fabbe50.fabsbnb.FabsBnBClient;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;

public class FabsBnBForgeClient {
    public static void init() {
        FabsBnBClient.initClient();
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (a, b) -> true));
        FabsBnBForgeClient.registerConfigScreen();
    }

    public static void registerConfigScreen() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> ClothScreen.getConfigScreen(screen)));
    }
}
