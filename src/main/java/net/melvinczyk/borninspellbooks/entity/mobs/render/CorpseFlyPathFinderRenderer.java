package net.melvinczyk.borninspellbooks.entity.mobs.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mcreator.borninchaosv.client.renderer.CorpseFlyRenderer;
import net.mcreator.borninchaosv.entity.CorpseFlyEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class CorpseFlyPathFinderRenderer extends CorpseFlyRenderer {
    public CorpseFlyPathFinderRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(CorpseFlyEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        float yOffset = entity.getBbHeight();
        poseStack.translate(0, -yOffset, 0);

        ResourceLocation texture = getTextureLocation(entity);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(texture));
        ResourceLocation modelResource = new ResourceLocation("born_in_chaos_v1", "geo/corpsefly.geo.json");
        BakedGeoModel model = this.getGeoModel().getBakedModel(modelResource);

        super.preRender(poseStack, entity, model, bufferSource, vertexConsumer, false, partialTicks, packedLight, 0, 1.0F, 1.0F, 1.0F, 0);

        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

}
