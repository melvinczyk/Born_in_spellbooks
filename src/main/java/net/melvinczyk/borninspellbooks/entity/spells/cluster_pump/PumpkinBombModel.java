package net.melvinczyk.borninspellbooks.entity.spells.cluster_pump;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PumpkinBombModel extends GeoModel<PumpkinBombProjectile> {
    public static final ResourceLocation modelResource = new ResourceLocation("born_in_chaos_v1", "geo/pumpkinbomb.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation("born_in_chaos_v1", "textures/entities/pumpkinbomb.png");
    public static final ResourceLocation animationResource = new ResourceLocation("born_in_chaos_v1", "animations/pumpkinbomb.animation.json");

    @Override
    public ResourceLocation getModelResource(PumpkinBombProjectile pumpkinBombProjectile) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(PumpkinBombProjectile pumpkinBombProjectile) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(PumpkinBombProjectile pumpkinBombProjectile) {
        return animationResource;
    }
}
