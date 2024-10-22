package net.melvinczyk.borninspellbooks.entity.spells.maggot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MaggotProjectileRenderer extends GeoEntityRenderer<MaggotProjectile> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("born_in_chaos_v1", "textures/entities/maggot.png");


    public MaggotProjectileRenderer(EntityRendererProvider.Context context)
    {
        super(context, new MaggotProjectileModel());
    }

    @Override
    public void render(MaggotProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light)
    {
        poseStack.pushPose();
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
