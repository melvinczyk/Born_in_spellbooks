package net.melvinczyk.borninspellbooks.entity.spells.lingering_stain;

import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
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

public class LingeringStainField extends AoeEntity {
    public LingeringStainField(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private DamageSource damageSource;


    public LingeringStainField(Level level) {
        this(MAEntityRegistry.LINGERING_STAIN.get(), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (damageSource == null) {
            damageSource = new DamageSource(DamageSources.getHolderFromResource(target, ISSDamageTypes.FIRE_FIELD), this, getOwner());
        }
        DamageSources.ignoreNextKnockback(target);
        target.hurt(damageSource, getDamage());
    }

    @Override
    public float getRadius()
    {
        return 3;
    }

    @Override
    public float getParticleCount() {
        return 2.0f * getRadius();
    }

    @Override
    protected float particleYOffset() {
        return .15f;
    }

    @Override
    protected float getParticleSpeedModifier() {
        return 1.3f;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        var infernalSurge = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "fleshsplash"));
        if (infernalSurge != null)
            return Optional.of((ParticleOptions) infernalSurge);
        return Optional.empty();
    }
}
