package com.fabbe50.fabsbnb.neoforge;

import com.fabbe50.fabsbnb.ClothScreen;
import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.FabsBnBClient;
import com.fabbe50.fabsbnb.Platform;
import com.fabbe50.fabsbnb.client.gui.screen.container.BlockBreakerScreen;
import com.fabbe50.fabsbnb.client.renderer.blockentity.XPHolderRenderer;
import com.fabbe50.fabsbnb.registries.ModRegistries;
import com.fabbe50.fabsbnb.registries.TabList;
import com.fabbe50.fabsbnb.registries.client.ClientRegistries;
import com.fabbe50.fabsbnb.world.block.entity.XPHolderBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(value = FabsBnB.MOD_ID, dist = Dist.CLIENT)
public class FabsBnBNeoForgeClient {
    public FabsBnBNeoForgeClient(IEventBus eventBus, ModContainer container) {
        FabsBnBClient.initClient();

        if (Platform.isModLoaded("cloth_config")) {
            FabsBnB.log("Registering extension point for cloth config screen...");
            container.registerExtensionPoint(IConfigScreenFactory.class, (modContainer, screen) -> ClothScreen.getConfigScreen(screen));
        }

        eventBus.addListener(this::onInitClientEvent);
        eventBus.addListener(this::onPopulateCreativeTab);
    }

    public void onInitClientEvent(FMLClientSetupEvent event) {
        ClientRegistries.registerBlockRenderer();
    }

    public void onPopulateCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab().equals(ModRegistries.TAB.get())) {
            TabData data = new TabData(event);
            TabList<BuildCreativeModeTabContentsEvent, TabData> tabList = new TabList<>();
            tabList.registerTab(data, event.getParameters().holders());
        }
    }

    @EventBusSubscriber(modid = FabsBnB.MOD_ID, value = Dist.CLIENT)
    public static class Events {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModRegistries.BLOCK_BREAKER_MENU.get(), BlockBreakerScreen::new);
        }
    }

    public static class TabData extends TabList.TabReg<BuildCreativeModeTabContentsEvent> {
        public TabData(BuildCreativeModeTabContentsEvent regHandler) {
            super(regHandler);
        }

        @Override
        public void accept(ItemStack stack) {
            getRegHandler().accept(stack);
        }
    }
}
