package net.melvinczyk.borninspellbooks.entity.spells.spirit_copy;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class SpiritCopyRenderer extends LivingEntityRenderer<SpiritCopyHumanoid, HumanoidModel<SpiritCopyHumanoid>> {
    private static final float ALPHA_VALUE = 0.5f;

    public SpiritCopyRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(SpiritCopyHumanoid entity) {
        Player player = entity.getPlayer();

        if (player != null) {
            GameProfile profile = player.getGameProfile();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures = Minecraft.getInstance().getSkinManager().getInsecureSkinInformation(profile);

            if (textures.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                MinecraftProfileTexture skinTexture = textures.get(MinecraftProfileTexture.Type.SKIN);
                return Minecraft.getInstance().getSkinManager().registerTexture(skinTexture, MinecraftProfileTexture.Type.SKIN);
            }
        }
        return DefaultPlayerSkin.getDefaultSkin(entity.getUUID());
    }

    @Override
    public void render(SpiritCopyHumanoid iceMan, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre<>(iceMan, this, pPartialTicks, pMatrixStack, pBuffer, pPackedLight)))
            return;

        pMatrixStack.pushPose();
        this.model.attackTime = this.getAttackAnim(iceMan, pPartialTicks);

        boolean shouldSit = iceMan.isSitting();
        this.model.riding = shouldSit;
        this.model.young = iceMan.isBaby();
        float bodyYRot = iceMan.yBodyRot;
        float yHeadRot = iceMan.yHeadRot;
        float f2 = yHeadRot - bodyYRot;

        if (shouldSit) {
            f2 = yHeadRot - bodyYRot;
            float f3 = Mth.wrapDegrees(f2);
            if (f3 < -85.0F) f3 = -85.0F;
            if (f3 >= 85.0F) f3 = 85.0F;

            bodyYRot = yHeadRot - f3;
            if (f3 * f3 > 2500.0F) bodyYRot += f3 * 0.2F;
            f2 = yHeadRot - bodyYRot;
        }

        float f6 = Mth.lerp(pPartialTicks, iceMan.xRotO, iceMan.getXRot());
        if (isEntityUpsideDown(iceMan)) {
            f6 *= -1.0F;
            f2 *= -1.0F;
        }

        if (iceMan.hasPose(Pose.SLEEPING)) {
            Direction direction = iceMan.getBedOrientation();
            if (direction != null) {
                float f4 = iceMan.getEyeHeight(Pose.STANDING) - 0.1F;
                pMatrixStack.translate((double) ((float) (-direction.getStepX()) * f4), 0.0D, (double) ((float) (-direction.getStepZ()) * f4));
            }
        }

        this.setupRotations(iceMan, pMatrixStack, 0, bodyYRot, pPartialTicks);
        pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(iceMan, pMatrixStack, pPartialTicks);
        pMatrixStack.translate(0.0D, -1.501F, 0.0D);

        float limbSwingAmount = iceMan.getLimbSwingAmount();
        float limbSwing = iceMan.getLimbSwing();

        this.model.prepareMobModel(iceMan, limbSwing, limbSwingAmount, pPartialTicks);
        this.model.setupAnim(iceMan, limbSwing, limbSwingAmount, 0, f2, f6);

        RenderType renderType = RenderType.entityTranslucent(getTextureLocation(iceMan));
        VertexConsumer vertexConsumer = pBuffer.getBuffer(renderType);

        this.model.renderToBuffer(pMatrixStack, vertexConsumer, pPackedLight, LivingEntityRenderer.getOverlayCoords(iceMan, 0), 1.0F, 1.0F, 1.0F, ALPHA_VALUE);

        pMatrixStack.popPose();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post<>(iceMan, this, pPartialTicks, pMatrixStack, pBuffer, pPackedLight));
    }
}
