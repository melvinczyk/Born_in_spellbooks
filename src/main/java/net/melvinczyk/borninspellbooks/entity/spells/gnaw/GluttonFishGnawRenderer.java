package net.melvinczyk.borninspellbooks.entity.spells.gnaw;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.render.GeoLivingEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import javax.annotation.Nullable;

public class GluttonFishGnawRenderer extends GeoLivingEntityRenderer<GluttonFishGnawEntity> {

    public GluttonFishGnawRenderer(EntityRendererProvider.Context context) {
        super(context, new GluttonFishGnawModel());
        this.shadowRadius = 0.0f;
    }

    @Override
    public ResourceLocation getTextureLocation(GluttonFishGnawEntity animatable) {
        return GluttonFishGnawModel.textureResource;
    }

    @Override
    public void render(GluttonFishGnawEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();

        // Get the owner's (player's) yaw and pitch to align the projectile with the player's look direction
        if (entity.getOwner() != null) {
            float playerYaw = entity.getOwner().getYRot();  // Player's yaw
            float playerPitch = entity.getOwner().getXRot();  // Player's pitch

            // Rotate the entity to face the same direction as the player (yaw for horizontal, pitch for vertical)
            poseStack.mulPose(Axis.YP.rotationDegrees(-playerYaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(-playerPitch));

            // Optionally rotate the projectile to be on its side (e.g., 90 degrees on X axis)
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
        poseStack.popPose();
    }


    @Override
    public void preRender(PoseStack poseStack, GluttonFishGnawEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}