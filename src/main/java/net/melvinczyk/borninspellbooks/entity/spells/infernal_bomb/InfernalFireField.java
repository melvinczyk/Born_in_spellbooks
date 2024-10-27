package net.melvinczyk.borninspellbooks.entity.spells.infernal_bomb;

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

public class InfernalFireField extends AoeEntity {

    public InfernalFireField(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private DamageSource damageSource;
    private int effectDuration = 0;


    public InfernalFireField(Level level) {
        this(MAEntityRegistry.INFERNAL_FIRE.get(), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (damageSource == null) {
            damageSource = new DamageSource(DamageSources.getHolderFromResource(target, ISSDamageTypes.FIRE_FIELD), this, getOwner());
        }
        DamageSources.ignoreNextKnockback(target);
        target.hurt(damageSource, getDamage());

        MobEffectInstance infernalFlame = new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_flame")), effectDuration, 1);
        if (infernalFlame != null)
        {
            target.addEffect(infernalFlame);
        }
    }

    public void setEffectDuration(int duration)
    {
        this.effectDuration = duration;
    }

    @Override
    public float getRadius()
    {
        return 3;
    }

    @Override
    public float getParticleCount() {
        return 1.2f * getRadius();
    }

    @Override
    protected float particleYOffset() {
        return .2f;
    }

    @Override
    protected float getParticleSpeedModifier() {
        return 1.3f;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        var infernalSurge = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_surge"));
        if (infernalSurge != null)
            return Optional.of((ParticleOptions) infernalSurge);
        return Optional.empty();
    }
}
