package net.melvinczyk.borninspellbooks.entity.spells.phantom_copy;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class PhantomCopyRenderer extends LivingEntityRenderer<PhantomCopyHumanoid, HumanoidModel<PhantomCopyHumanoid>> {

    public PhantomCopyRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.36f);
        this.addLayer(new PhantomCopyLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(PhantomCopyHumanoid entity) {
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
}
