package net.melvinczyk.borninspellbooks.effect;

import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DeathWishEffect extends MagicMobEffect {
    private float spellPower = -1;
    public DeathWishEffect(MobEffectCategory pCategory, int pColor)
    {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        MobEffectInstance effectInstance = pLivingEntity.getEffect(this);
        if (effectInstance != null)
        {
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, effectInstance.getDuration(), 1, false, false, false));
        }
        //pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, (int)(spellPower * 10), 1, false, false));
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
        explode(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        if (this.spellPower == -1)
            this.spellPower = (float)pDuration / 10;
        return false;
    }

    private void explode(LivingEntity entity, int amplifier) {
        float baseRadius = 3.0F;
        float radiusMultiplier = spellPower * (0.02F * amplifier);
        float explosionRadius = baseRadius + radiusMultiplier;
        if (ServerConfigs.SPELL_GREIFING.get())
        {
            entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), explosionRadius, Level.ExplosionInteraction.MOB);
        }
        else
        {
            entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), explosionRadius, Level.ExplosionInteraction.NONE);
        }
        DamageSource explosionSource = MASpellRegistry.DEATH_WISH.get().getDamageSource(entity, entity);
        var entities = entity.level().getEntities(entity, entity.getBoundingBox().inflate(explosionRadius));
        for (Entity target : entities) {
            if (target != null && target.equals(entity)) {
                continue;
            }

            double distance = entity.position().distanceTo(entity.position());
            if (distance < explosionRadius) {
                float damage = 5;
                DamageSources.applyDamage(entity, damage, explosionSource);
                entity.invulnerableTime = 0;
            }
        }

        float maxHealth = entity.getMaxHealth();
        float explosionDamage = (maxHealth * 0.2F) + (amplifier * 2.0F);
        entity.hurt(MASpellRegistry.DEATH_WISH.get().getDamageSource(entity), explosionDamage);
    }
}
