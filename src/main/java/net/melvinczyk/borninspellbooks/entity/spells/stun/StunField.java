package net.melvinczyk.borninspellbooks.entity.spells.stun;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class StunField extends AoeEntity {
    public StunField(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private int effectDuration = 0;
    private DamageSource damageSource;

    public StunField(Level level) {
        this(MAEntityRegistry.STUN_FIELD.get(), level);
    }
    @Override
    public void applyEffect(LivingEntity target) {
        if (damageSource == null) {
            damageSource = new DamageSource(DamageSources.getHolderFromResource(target, ISSDamageTypes.FIRE_FIELD), this, getOwner());
        }
        DamageSources.ignoreNextKnockback(target);
        target.hurt(damageSource, getDamage());
        MobEffectInstance stun = new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("born_in_chaos_v1", "stun")), effectDuration, 0);
        if (stun != null) {
            target.addEffect(stun);
        }
    }

    public void setEffectDuration(int duration)
    {
        this.effectDuration = duration;
    }

    @Override
    public float getParticleCount() {
        return 2;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        var infernalSurge = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "cloudsofdust"));
        if (infernalSurge != null)
            return Optional.of((ParticleOptions) infernalSurge);
        return Optional.empty();
    }
}
