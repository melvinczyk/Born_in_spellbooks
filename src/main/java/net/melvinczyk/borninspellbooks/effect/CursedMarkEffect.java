package net.melvinczyk.borninspellbooks.effect;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.melvinczyk.borninspellbooks.entity.mobs.ScarletPersecutor;
import net.melvinczyk.borninspellbooks.util.MAMobEffectInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class CursedMarkEffect extends MagicMobEffect {
    private LivingEntity caster;
    private int spellPower;
    public CursedMarkEffect(MobEffectCategory pCategory, int pColor)
    {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        MAMobEffectInstance effectInstance = (MAMobEffectInstance) pLivingEntity.getEffect(this);
        if (effectInstance != null)
        {
            this.caster = effectInstance.getCaster();
            this.spellPower = effectInstance.getDuration() / 2;
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
        spawnScarletPersecutor(entity, amplifier);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int pAmplifier) {
        if (livingEntity.level().isClientSide) {
            MagicManager.spawnParticles(livingEntity.level(), (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "fleshsplash")), livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() * .5f, livingEntity.getZ(), 50, livingEntity.getBbWidth() * .25f, livingEntity.getBbHeight() * .25f, livingEntity.getBbWidth() * .25f, .03, false);
        }
    }

    private void playSound(LivingEntity targetEntity)
    {
        targetEntity.level().playSound(null, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), (SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1:persecutor_scream")), SoundSource.HOSTILE, 0.75F, 1.0F);
    }


    private void spawnScarletPersecutor(LivingEntity targetEntity, int amplifier) {
        if (!targetEntity.level().isClientSide) {
            var level = targetEntity.level();

            float radius = 2.0f + .185f * 4;
            playSound(targetEntity);
            int spellLevel = amplifier + 1;
            float attributeModifier = (this.spellPower) * 0.1f;
            MagicManager.spawnParticles(level, (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "srirst_part")), targetEntity.getX(), targetEntity.getY() + targetEntity.getBbHeight() * .5f, targetEntity.getZ(), 20, targetEntity.getBbWidth() * .5f, targetEntity.getBbHeight() * .5f, targetEntity.getBbWidth() * .5f, .03, false);
            for (int i = 0; i <  spellLevel; i++)
            {
                if(i % 2 == 0)
                {
                    ScarletPersecutor scarletPersecutor = new ScarletPersecutor(level, this.caster);
                    scarletPersecutor.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(scarletPersecutor.getOnPos()), MobSpawnType.MOB_SUMMONED, null, null);
                    var yrot = 6.281f / 8 * i + targetEntity.getYRot() * Mth.DEG_TO_RAD;
                    Vec3 spawn = Utils.moveToRelativeGroundLevel(level, targetEntity.getEyePosition().add(new Vec3(radius * Mth.cos(yrot), 1, radius * Mth.sin(yrot))), 10);
                    scarletPersecutor.setPos(spawn.x, spawn.y, spawn.z);
                    scarletPersecutor.setYRot(targetEntity.getYRot());
                    scarletPersecutor.setOldPosAndRot();
                    scarletPersecutor.assignTarget(targetEntity);

                    scarletPersecutor.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(4 + attributeModifier);
                    scarletPersecutor.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(10 + attributeModifier);

                    level.addFreshEntity(scarletPersecutor);
                }
            }
        }
    }
}
