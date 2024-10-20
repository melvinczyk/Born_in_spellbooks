package net.melvinczyk.borninspellbooks.entity.spells.phantom_copy;

import com.mojang.blaze3d.vertex.PoseStack;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class PhantomCopyLayer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation TRANSPARENT_LAYER = new ResourceLocation(BornInSpellbooks.MODID, "textures/entity/texture.png");

    public PhantomCopyLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType transparentLayer = RenderType.entityTranslucent(TRANSPARENT_LAYER);
        var vertexConsumer = bufferSource.getBuffer(transparentLayer);
        this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, LivingEntityRenderer.getOverlayCoords(livingEntity, 0), 0.0F, 0.0F, 0.0F, 0.5F); // RGB is 0 for black, alpha is 0.5 for transparency
    }
}
