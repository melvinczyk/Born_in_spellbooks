package net.melvinczyk.borninspellbooks.entity.spells.great_glutton;

import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GreatGluttonModel extends GeoModel<GreatGluttonProjectile> {
    public static final ResourceLocation modelResource = new ResourceLocation("born_in_chaos_v1", "geo/gluttonfish.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation("born_in_chaos_v1", "textures/entities/gluttonfish.png");
    public static final ResourceLocation animationResource = new ResourceLocation("born_in_chaos_v1", "animations/gluttonfish.animation.json");

    @Override
    public ResourceLocation getModelResource(GreatGluttonProjectile object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(GreatGluttonProjectile object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(GreatGluttonProjectile animatable) {
        return animationResource;
    }
}
