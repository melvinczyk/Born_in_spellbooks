package net.melvinczyk.borninspellbooks.entity.spells.infernal_bomb;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireBomb;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class InfernalBombEntity extends FireBomb {
    public InfernalBombEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public InfernalBombEntity(Level level, LivingEntity shooter) {
        this(MAEntityRegistry.INFERNAL_BOMB.get(), level);
        setOwner(shooter);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level(),(ParticleOptions)ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("minecraft", "explosion")), x, y, z, 10, 1.5, .1, 1.5, 1, false);
    }

    @Override
    protected void onHit(HitResult hitresult) {
        super.onHit(hitresult);
        createFireField(hitresult.getLocation());
        float explosionRadius = getExplosionRadius();
        var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));
        for (Entity entity : entities) {
            double distance = entity.distanceToSqr(hitresult.getLocation());
            if (distance < explosionRadius * explosionRadius && canHitEntity(entity)) {
                if (Utils.hasLineOfSight(level(), hitresult.getLocation(), entity.position().add(0, entity.getEyeHeight() * .5f, 0), true)) {
                    double p = (1 - Math.pow(Math.sqrt(distance) / (explosionRadius), 3));
                    float damage = (float) (this.damage * p);
                    DamageSources.applyDamage(entity, damage, MASpellRegistry.INFERNAL_BOMB.get().getDamageSource(this, getOwner()));
                    if (entity instanceof LivingEntity livingEntity)
                    {
                        MobEffectInstance infernalFlame = new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_flame")), 120, 1);
                        if (infernalFlame != null)
                        {
                            livingEntity.addEffect(infernalFlame);
                        }
                    }
                }
            }
        }
        discard();
    }

    @Override
    public void createFireField(Vec3 location) {
        if (!level().isClientSide) {
            InfernalFireField fire = new InfernalFireField(level());
            fire.setOwner(getOwner());
            fire.setDuration(200);
            fire.setDamage(aoeDamage);
            fire.setRadius(getExplosionRadius());
            fire.setCircular();
            fire.moveTo(location);
            level().addFreshEntity(fire);
        }
    }

    float aoeDamage;

    @Override
    public float getSpeed() {
        return .5f;
    }

    @Override
    protected void doImpactSound(SoundEvent sound) {
        level().playSound(null, getX(), getY(), getZ(), sound, SoundSource.NEUTRAL, 2, 0.5f + Utils.random.nextFloat() * .2f);
    }

    public void setAoeDamage(float damage) {
        this.aoeDamage = damage;
    }

    public float getAoeDamage() {
        return aoeDamage;
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }
}
