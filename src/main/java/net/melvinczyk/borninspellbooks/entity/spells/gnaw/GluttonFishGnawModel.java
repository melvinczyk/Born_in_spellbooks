package net.melvinczyk.borninspellbooks.entity.spells.gnaw;

import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GluttonFishGnawModel extends GeoModel<GluttonFishGnawEntity> {
    public static final ResourceLocation modelResource = new ResourceLocation(BornInSpellbooks.MODID, "geo/gluttonfish_head.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation("born_in_chaos_v1", "textures/entities/gluttonfish.png");
    public static final ResourceLocation animationResource = new ResourceLocation("born_in_chaos_v1", "animations/gluttonfish.animation.json");

    @Override
    public ResourceLocation getModelResource(GluttonFishGnawEntity object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(GluttonFishGnawEntity object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(GluttonFishGnawEntity animatable) {
        return animationResource;
    }
}
