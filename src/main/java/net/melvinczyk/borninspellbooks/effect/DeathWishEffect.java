package net.melvinczyk.borninspellbooks.effect;

import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Level;

public class DeathWishEffect extends MagicMobEffect {
    public DeathWishEffect(MobEffectCategory pCategory, int pColor)
    {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
        explode(entity, amplifier);
    }

    private void explode(LivingEntity entity, int amplifier) {
        float baseRadius = 2.0F;
        float radiusMultiplier = amplifier * 0.5F;
        float explosionRadius = baseRadius + radiusMultiplier;
        entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), explosionRadius, Level.ExplosionInteraction.NONE);

        float maxHealth = entity.getMaxHealth();
        float explosionDamage = (maxHealth * 0.2F) + (amplifier * 2.0F);
        entity.hurt(MASpellRegistry.DEATH_WISH.get().getDamageSource(entity), explosionDamage);
    }
}
