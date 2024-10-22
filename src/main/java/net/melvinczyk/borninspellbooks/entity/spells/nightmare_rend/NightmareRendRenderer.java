package net.melvinczyk.borninspellbooks.entity.spells.nightmare_rend;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import java.util.Random;

public class NightmareRendRenderer extends EntityRenderer<NightmareRend> {
    private static final ResourceLocation[] TEXTURES = {
            // Placeholder
            BornInSpellbooks.id("textures/entity/spell/nightmare_rend_1.png"),
            BornInSpellbooks.id("textures/entity/spell/nightmare_rend_2.png"),
            BornInSpellbooks.id("textures/entity/spell/nightmare_rend_3.png"),
            BornInSpellbooks.id("textures/entity/spell/nightmare_rend_4.png")
    };

    public NightmareRendRenderer(Context context) {
        super(context);
    }

    @Override
    public void render(NightmareRend entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();

        Pose pose = poseStack.last();
        poseStack.mulPose(Axis.YP.rotationDegrees(180- entity.getYRot()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(270 + entity.getXRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        drawSlash(pose, entity, bufferSource, entity.getBbWidth() * 1.5f, entity.isMirrored());

        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }


    private void drawSlash(Pose pose, NightmareRend entity, MultiBufferSource bufferSource, float width, boolean mirrored) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        float halfWidth = width * 1.5f;
        float height = entity.getBbHeight() * .5f;
        consumer.vertex(poseMatrix, -halfWidth, height, -halfWidth).color(255, 255, 255, 255).uv(0f, mirrored ? 0f : 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, halfWidth, height, -halfWidth).color(255, 255, 255, 255).uv(1f, mirrored ? 0f : 1f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, halfWidth, height, halfWidth).color(255, 255, 255, 255).uv(1f, mirrored ? 1f : 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0f, 1f, 0f).endVertex();
        consumer.vertex(poseMatrix, -halfWidth, height, halfWidth).color(255, 255, 255, 255).uv(0f, mirrored ? 1f : 0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0f, 1f, 0f).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(NightmareRend entity) {
        int frame = (entity.tickCount / entity.ticksPerFrame) % TEXTURES.length;
        return TEXTURES[frame];
    }
}
