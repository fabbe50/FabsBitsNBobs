package com.fabbe50.fabsbnb.client.renderer.blockentity;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.util.Utilities;
import com.fabbe50.fabsbnb.world.block.entity.XPHolderBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class XPHolderRenderer implements BlockEntityRenderer<XPHolderBlockEntity> {
    private float rotation;
//    private final ItemRenderer itemRenderer;
//    private Font font;

    public XPHolderRenderer(BlockEntityRendererProvider.Context context) {
//        this.itemRenderer = context.getItemRenderer();
//        this.font = context.getFont();
    }

    @Override
    public void render(XPHolderBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, Vec3 vec3) {
        poseStack.pushPose();
        Minecraft mc = Minecraft.getInstance();
        renderText(mc, blockEntity, f, poseStack, multiBufferSource, i);
        renderXPBottle(mc, blockEntity, f, poseStack, multiBufferSource, i);
        poseStack.popPose();
    }

    private void renderText(Minecraft mc, XPHolderBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        poseStack.pushPose();

        poseStack.translate(0.5, 0.76, 0.5);
        poseStack.mulPose(Axis.ZP.rotationDegrees(90));
        poseStack.mulPose(Axis.YP.rotationDegrees(-90));

        float scale = 0.03f;
        poseStack.scale(scale, scale, scale);

        float k = Mth.lerp(f, blockEntity.oRot, blockEntity.rot);

        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        poseStack.mulPose(Axis.ZP.rotation(k));

        /*poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        poseStack.scale(0.025F, -0.025F, 0.025F);*/

        Matrix4f matrix = poseStack.last().pose();

        int xp = Utilities.getLevelFromTotalExperience(blockEntity.getXp());
        String xpText = String.valueOf(xp).trim();
        Component text = Component.literal(xpText);
        float width = mc.font.width(text) - 1;
        float textX = -width / 2f;
        float textY = (mc.font.lineHeight / 2f) - 1f;

        mc.font.drawInBatch8xOutline(
                text.getVisualOrderText(),
                textX, -textY,
                0xFF80FF20, // Color of text: XP green
                0xFF000000, // Color of outline: BLACK
                matrix,
                multiBufferSource,
                0xF000F0 // packed light, same as 15728880
        );

        poseStack.popPose();
    }

    private void renderXPBottle(Minecraft mc, XPHolderBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
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
        //ItemModel xpBottleModel = itemRenderer.(stack, blockEntity.getLevel(), null, 0);
        poseStack.translate(((1f / 16f) * 0.5), 0, 0);
        mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, i, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, blockEntity.getLevel(), 0);
        //this.itemRenderer.render(stack, ItemDisplayContext.FIXED, false, poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY, xpBottleModel);

        /*font.drawInBatch(
                "This is a lot of extra text...",
                1,
                0.4f,
                0x80FF20, // example XP green color
                false,
                poseStack.last().pose(),
                multiBufferSource,
                Font.DisplayMode.NORMAL,
                0,
                0xF000F0 // packed light, same as 15728880
        );*/

        poseStack.popPose();
    }
}
