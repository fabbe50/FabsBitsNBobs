package com.fabbe50.fabsbnb.client.renderer.blockentity;

import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.world.block.entity.XPHolderBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class XPHolderRenderer implements BlockEntityRenderer<XPHolderBlockEntity> {
    private float rotation;
    private final ItemRenderer itemRenderer;
    private final Font font;

    public XPHolderRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
        this.font = context.getFont();
    }

    @Override
    public void render(XPHolderBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.751, 0.5);
        poseStack.mulPose(Axis.ZP.rotationDegrees(90));
        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        poseStack.scale(0.03f, 0.03f, 0.03f);
        float h;
        for(h = blockEntity.rot - blockEntity.oRot; h >= (float)Math.PI; h -= ((float)Math.PI * 2F)) {
        }
        while(h < -(float)Math.PI) {
            h += ((float)Math.PI * 2F);
        }
        float k = blockEntity.oRot + h * f;
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.mulPose(Axis.ZP.rotation(k));
        Component text = Component.literal(String.valueOf(Utilities.getLevelFromTotalExperience(blockEntity.getXp())).trim());
        float width = this.font.width(text) - 1f;
        float textPos = -width / 2;
        float yPos = (this.font.lineHeight / 2f) - 1f;
        FormattedCharSequence formattedText = text.getVisualOrderText();
        this.font.drawInBatch8xOutline(formattedText, textPos, -yPos, 8453920, 0, poseStack.last().pose(), multiBufferSource, 15728880);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.5F, 1.2F, 0.5F);
        float g = (float)blockEntity.time + f;
        poseStack.translate(0.0F, 0.1F + Mth.sin(g * 0.1F) * 0.05F, 0.0F);

        if (this.rotation >= Math.PI * 2) {
            this.rotation = 0;
        }
        this.rotation = this.rotation + ((1 * 0.02f) * f);
        poseStack.mulPose(Axis.YP.rotation(-this.rotation));
        poseStack.scale(0.7f, 0.7f, 0.7f);
        ItemStack stack = new ItemStack(Items.EXPERIENCE_BOTTLE);
        BakedModel xpBottleModel = itemRenderer.getModel(stack, blockEntity.getLevel(), null, 0);
        poseStack.translate(((1f / 16f) * 0.5), 0, 0);
        this.itemRenderer.render(stack, ItemDisplayContext.FIXED, false, poseStack, multiBufferSource, 0xFFFFFF, OverlayTexture.NO_OVERLAY, xpBottleModel);
        poseStack.popPose();
    }
}
