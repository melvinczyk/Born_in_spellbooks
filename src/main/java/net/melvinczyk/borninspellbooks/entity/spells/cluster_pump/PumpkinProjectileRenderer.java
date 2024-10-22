package net.melvinczyk.borninspellbooks.entity.spells.cluster_pump;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;


public class PumpkinProjectileRenderer extends EntityRenderer<PumpkinProjectile> {
    public PumpkinProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(PumpkinProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        poseStack.pushPose();
        poseStack.translate(-0.5, 0.0, -0.5);
        blockRenderer.renderSingleBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_evil_pumpkin")).defaultBlockState(), poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }


    @Override
    public ResourceLocation getTextureLocation(PumpkinProjectile pEntity) {
        return null;
    }
}
