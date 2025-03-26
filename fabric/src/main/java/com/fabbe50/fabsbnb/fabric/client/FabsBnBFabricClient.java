package com.fabbe50.fabsbnb.fabric.client;

import com.fabbe50.fabsbnb.FabsBnBClient;
import net.fabricmc.api.ClientModInitializer;

public final class FabsBnBFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        FabsBnBClient.initClient();
    }
}
