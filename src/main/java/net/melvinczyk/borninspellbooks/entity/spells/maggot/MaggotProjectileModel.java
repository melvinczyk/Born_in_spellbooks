package net.melvinczyk.borninspellbooks.entity.spells.maggot;


import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;


public class MaggotProjectileModel extends GeoModel<MaggotProjectile>{
    public static final ResourceLocation modelResource = new ResourceLocation("born_in_chaos_v1", "geo/maggot.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation("born_in_chaos_v1", "textures/entities/maggot.png");
    public static final ResourceLocation animationResource = new ResourceLocation("born_in_chaos_v1", "animations/maggot.animation.json");


    @Override
    public ResourceLocation getModelResource(MaggotProjectile geoAnimatable) {
        return MaggotProjectileModel.modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(MaggotProjectile geoAnimatable) {
        return MaggotProjectileModel.textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(MaggotProjectile geoAnimatable) {
        return MaggotProjectileModel.animationResource;
    }
}
