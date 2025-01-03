package net.melvinczyk.borninspellbooks.entity.spells.pumpkins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PumpkinBombRenderer extends GeoEntityRenderer<PumpkinBombProjectile> {
    public PumpkinBombRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PumpkinBombModel());
    }

    @Override
    public void render(PumpkinBombProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light)
    {
        poseStack.pushPose();
        super.render(entity, yaw ,partialTicks, poseStack, bufferSource, light);

        poseStack.popPose();
    }
}
