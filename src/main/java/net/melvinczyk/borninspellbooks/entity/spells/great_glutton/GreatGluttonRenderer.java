package net.melvinczyk.borninspellbooks.entity.spells.great_glutton;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.render.GeoLivingEntityRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.maggot.MaggotProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GreatGluttonRenderer extends GeoEntityRenderer<GreatGluttonProjectile> {

    public GreatGluttonRenderer(EntityRendererProvider.Context context) {
        super(context, new GreatGluttonModel());
    }

    @Override
    public void render(GreatGluttonProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light)
    {
        poseStack.pushPose();

        float yOffset = entity.getBbHeight();
        poseStack.translate(0, yOffset - 1, 0);
        Vec3 motion = entity.getDeltaMovement();
        float xRot = -((float)(Mth.atan2(motion.horizontalDistance(), motion.y) * (180F / (float)Math.PI)) - 90.0F);
        float yRot = -((float)(Mth.atan2(motion.z, motion.x) * (180F / (float)Math.PI)) + 90.0F);
        yRot += 180.0F;
        xRot = -xRot;
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(xRot));

        super.render(entity, yaw ,partialTicks, poseStack, bufferSource, light);

        poseStack.popPose();
    }
}