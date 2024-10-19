package net.melvinczyk.borninspellbooks.entity.spells.pumpkin_smash;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PumpkinProjectileRenderer extends EntityRenderer<PumpkinProjectile> {
    protected PumpkinProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(PumpkinProjectile pEntity) {
        return null;
    }
}
