package com.fabbe50.fabsbnb.neoforge;

import com.fabbe50.fabsbnb.ClothScreen;
import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.FabsBnBClient;
import com.fabbe50.fabsbnb.Platform;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = FabsBnB.MOD_ID, dist = Dist.CLIENT)
public class FabsBnBNeoForgeClient {
    public FabsBnBNeoForgeClient(ModContainer container) {
        FabsBnBClient.initClient();

        if (Platform.isModLoaded("cloth_config")) {
            FabsBnB.log("Registering extension point for cloth config screen...");
            container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, screen) -> ClothScreen.getConfigScreen(screen));
        }
    }
}
