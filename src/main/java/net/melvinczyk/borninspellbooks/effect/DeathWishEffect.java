package net.melvinczyk.borninspellbooks.effect;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class DeathWishEffect extends MagicMobEffect {
    private int tickCount = 0;
    private LivingEntity caster;
    public DeathWishEffect(MobEffectCategory pCategory, int pColor)
    {
        super(pCategory, pColor);
    }

    public DeathWishEffect(MobEffectCategory pCategory, int pColor, LivingEntity caster)
    {
        super(pCategory, pColor);
        this.caster = caster;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
        explode(entity);
    }

    private void playSound(LivingEntity targetEntity)
    {
        targetEntity.level().playSound(null, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), (SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1:persecutor_scream")), SoundSource.HOSTILE, 1.0F, 1.0F);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);

        tickCount++;

        if (tickCount >= 40) {
            dealDamage(entity);
            tickCount = 0;
        }
    }

    private void dealDamage(LivingEntity entity) {
        //DamageSources.applyDamage(entity, 1, MASpellRegistry.DEATH_WISH.get().getDamageSource(caster));
        entity.hurt(entity.damageSources().magic(), 1);
        if (entity.getHealth() <= 1) {
            explode(entity);
        }
    }

    private void explode(LivingEntity entity) {
        entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), 3.0F, Level.ExplosionInteraction.NONE);

        float maxHealth = entity.getMaxHealth();
        entity.setHealth(maxHealth * 0.1F);

        playSound(entity);
    }

}
