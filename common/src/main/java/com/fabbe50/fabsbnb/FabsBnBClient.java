package com.fabbe50.fabsbnb;

import com.fabbe50.fabsbnb.registries.client.ClientRegistries;

public class FabsBnBClient {
    public static void initClient() {
        FabsBnB.log("Initializing Fab's Bits & Bobs Client...");
        ClientRegistries.init();
    }
}
