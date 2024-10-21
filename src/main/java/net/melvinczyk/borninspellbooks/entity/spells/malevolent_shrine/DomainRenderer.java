package net.melvinczyk.borninspellbooks.entity.spells.malevolent_shrine;

import com.mojang.blaze3d.vertex.*;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class DomainRenderer extends EntityRenderer<Domain> {
    private static final ResourceLocation SPHERE_TEXTURE = BornInSpellbooks.id("textures/entity/domain.png");

    public DomainRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Domain entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, entity.getBoundingBox().getYsize() / 2, 0);

        float radius = entity.getRadius() + 1;
        int latitudeBands = 16;
        int longitudeBands = 16;

        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(SPHERE_TEXTURE));

        renderSphere(radius, latitudeBands, longitudeBands, poseMatrix, normalMatrix, vertexConsumer);
        renderCircle(poseStack, vertexConsumer, radius);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    private void renderSphere(float radius, int latitudeBands, int longitudeBands, Matrix4f poseMatrix, Matrix3f normalMatrix, VertexConsumer vertexConsumer) {
        for (int lat = 0; lat < latitudeBands; lat++) {
            float theta = (float) (lat * Math.PI / latitudeBands);
            float sinTheta = (float) Math.sin(theta);
            float cosTheta = (float) Math.cos(theta);

            float nextTheta = (float) ((lat + 1) * Math.PI / latitudeBands);
            float sinNextTheta = (float) Math.sin(nextTheta);
            float cosNextTheta = (float) Math.cos(nextTheta);

            for (int lon = 0; lon < longitudeBands; lon++) {
                float phi = (float) (lon * 2 * Math.PI / longitudeBands);
                float sinPhi = (float) Math.sin(phi);
                float cosPhi = (float) Math.cos(phi);

                float nextPhi = (float) ((lon + 1) * 2 * Math.PI / longitudeBands);
                float sinNextPhi = (float) Math.sin(nextPhi);
                float cosNextPhi = (float) Math.cos(nextPhi);

                float x1 = radius * cosPhi * sinTheta;
                float y1 = radius * cosTheta;
                float z1 = radius * sinPhi * sinTheta;

                float x2 = radius * cosNextPhi * sinTheta;
                float y2 = radius * cosTheta;
                float z2 = radius * sinNextPhi * sinTheta;

                float x3 = radius * cosPhi * sinNextTheta;
                float y3 = radius * cosNextTheta;
                float z3 = radius * sinPhi * sinNextTheta;

                float x4 = radius * cosNextPhi * sinNextTheta;
                float y4 = radius * cosNextTheta;
                float z4 = radius * sinNextPhi * sinNextTheta;

                float u = (float) lon / longitudeBands;
                float v = (float) lat / latitudeBands;
                float uNext = (float) (lon + 1) / longitudeBands;
                float vNext = (float) (lat + 1) / latitudeBands;

                if (lon == longitudeBands - 1) {
                    uNext = 1.0f;
                }

                vertexConsumer.vertex(poseMatrix, x1, y1, z1)
                        .color(255, 255, 255, 255)
                        .uv(u, v)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(LightTexture.FULL_BRIGHT)
                        .normal(normalMatrix, x1 / radius, y1 / radius, z1 / radius)
                        .endVertex();

                vertexConsumer.vertex(poseMatrix, x2, y2, z2)
                        .color(255, 255, 255, 255)
                        .uv(uNext, v)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(LightTexture.FULL_BRIGHT)
                        .normal(normalMatrix, x2 / radius, y2 / radius, z2 / radius)
                        .endVertex();

                vertexConsumer.vertex(poseMatrix, x3, y3, z3)
                        .color(255, 255, 255, 255)
                        .uv(u, vNext)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(LightTexture.FULL_BRIGHT)
                        .normal(normalMatrix, x3 / radius, y3 / radius, z3 / radius)
                        .endVertex();

                vertexConsumer.vertex(poseMatrix, x2, y2, z2)
                        .color(255, 255, 255, 255)
                        .uv(uNext, v)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(LightTexture.FULL_BRIGHT)
                        .normal(normalMatrix, x2 / radius, y2 / radius, z2 / radius)
                        .endVertex();

                vertexConsumer.vertex(poseMatrix, x4, y4, z4)
                        .color(255, 255, 255, 255)
                        .uv(uNext, vNext)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(LightTexture.FULL_BRIGHT)
                        .normal(normalMatrix, x4 / radius, y4 / radius, z4 / radius)
                        .endVertex();

                vertexConsumer.vertex(poseMatrix, x3, y3, z3)
                        .color(255, 255, 255, 255)
                        .uv(u, vNext)
                        .overlayCoords(OverlayTexture.NO_OVERLAY)
                        .uv2(LightTexture.FULL_BRIGHT)
                        .normal(normalMatrix, x3 / radius, y3 / radius, z3 / radius)
                        .endVertex();
            }
        }
    }

    private void renderCircle(PoseStack poseStack, VertexConsumer vertexConsumer, float radius) {
        float yOffset = 0.01f;
        int segments = 16;

        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        float angleIncrement = (float) (2 * Math.PI / segments);

        for (int i = 0; i < segments; i++) {
            float theta1 = i * angleIncrement;
            float theta2 = (i + 1) * angleIncrement;

            float x1 = radius * (float) Math.cos(theta1);
            float z1 = radius * (float) Math.sin(theta1);
            float x2 = radius * (float) Math.cos(theta2);
            float z2 = radius * (float) Math.sin(theta2);

            float u1 = 0.5f + 0.5f * (float) Math.cos(theta1);
            float v1 = 0.5f + 0.5f * (float) Math.sin(theta1);
            float u2 = 0.5f + 0.5f * (float) Math.cos(theta2);
            float v2 = 0.5f + 0.5f * (float) Math.sin(theta2);

            vertexConsumer.vertex(poseMatrix, 0, yOffset, 0)
                    .color(255, 255, 255, 255)
                    .uv(0.5f, 0.5f)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(LightTexture.FULL_BRIGHT)
                    .normal(normalMatrix, 0, 1, 0)
                    .endVertex();

            vertexConsumer.vertex(poseMatrix, x2, yOffset, z2)
                    .color(255, 255, 255, 255)
                    .uv(u2, v2)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(LightTexture.FULL_BRIGHT)
                    .normal(normalMatrix, 0, 1, 0)
                    .endVertex();

            vertexConsumer.vertex(poseMatrix, x1, yOffset, z1)
                    .color(255, 255, 255, 255)
                    .uv(u1, v1)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(LightTexture.FULL_BRIGHT)
                    .normal(normalMatrix, 0, 1, 0)
                    .endVertex();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Domain pEntity) {
        return SPHERE_TEXTURE;
    }
}
