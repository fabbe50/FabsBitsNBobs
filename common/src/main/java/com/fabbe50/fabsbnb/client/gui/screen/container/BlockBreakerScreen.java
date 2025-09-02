package com.fabbe50.fabsbnb.client.gui.screen.container;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.world.inventory.BlockBreakerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlockBreakerScreen extends AbstractContainerScreen<BlockBreakerMenu> {
    private static final ResourceLocation CONTAINER_LOCATION = FabsBnB.location("textures/gui/container/block_breaker.png");

    public BlockBreakerScreen(BlockBreakerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        this.renderBackground(guiGraphics, i, j, f);
        super.render(guiGraphics, i, j, f);
        this.renderTooltip(guiGraphics, i, j);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_LOCATION, k, l, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }
}
