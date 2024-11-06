package net.melvinczyk.borninspellbooks.effect;

import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.melvinczyk.borninspellbooks.registry.MAMobEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public class SpiritEffect extends MagicMobEffect {
    public SpiritEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
    private static final float[] SPIRIT_FOG_COLOR = new float[]{0, 0, 0};
    private static final float[] SPIRIT_SKY_COLOR = new float[]{0, 0, 0};

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

    @OnlyIn(Dist.CLIENT)
    public static float[] getFogColor() {
        return SPIRIT_FOG_COLOR;
    }

    @OnlyIn(Dist.CLIENT)
    public static float[] getSkyColor() {
        return SPIRIT_SKY_COLOR;
    }

    @OnlyIn(Dist.CLIENT)
    public static float getFogStart() {
        return 3.0F;
    }

    @OnlyIn(Dist.CLIENT)
    public static float getFogEnd() {
        return 10.0F;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int pAmplifier) {
        if (livingEntity.level().isClientSide && livingEntity == Minecraft.getInstance().player) {
            for (int i = 0; i < 50; i++) {
                Vec3 pos = new Vec3(
                        getRandomScaled(20),
                        getRandomScaled(4f) + 4,
                        getRandomScaled(20)
                ).add(livingEntity.position());

                Vec3 random = new Vec3(
                        getRandomScaled(.04f),
                        getRandomScaled(.02f),
                        getRandomScaled(.04f)
                );
                livingEntity.level().addParticle(
                        ParticleTypes.END_ROD,
                        pos.x, pos.y, pos.z,
                        random.x, random.y * 0.5, random.z
                );
            }
        }
    }

    private double getRandomScaled(float scale) {
        return (Math.random() - 0.5D) * scale;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 5 == 0;
    }
}
