package net.melvinczyk.borninspellbooks.effect;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.melvinczyk.borninspellbooks.registry.MAMobEffectRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SpiritEffect extends MagicMobEffect {
    public SpiritEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    protected static int amplifier = 0;
    protected static int duration = 0;

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        MobEffectInstance effectInstance = pLivingEntity.getEffect(this);
        if (effectInstance != null)
        {
            amplifier = effectInstance.getAmplifier() + 2;
            duration = effectInstance.getDuration();
        }
    }

    public static boolean isImmuneToDamage(LivingEntity entity) {
        if (entity instanceof Player) {
            MobEffectInstance spiritEffect = entity.getEffect(MAMobEffectRegistry.SPIRIT_EFFECT.get());
            return spiritEffect != null;
        }
        return false;
    }
}
