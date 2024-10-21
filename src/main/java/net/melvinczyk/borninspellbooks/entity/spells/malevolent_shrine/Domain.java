package net.melvinczyk.borninspellbooks.entity.spells.malevolent_shrine;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;

import java.util.Optional;
import java.util.UUID;

public class Domain extends AoeEntity {
    private final Random random = new Random();
    private int tickCounter = 0;
    protected LivingEntity caster;
    private UUID summonerUUID;
    private LivingEntity cachedSummoner;


    public Domain(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        setRadius(10);
    }

    public Domain(Level level, LivingEntity entity) {
        this(MAEntityRegistry.DOMAIN.get(), level);
        setDomainUser(entity);
    }

    public void setDomainUser(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    public LivingEntity getDomainUser() {
        return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
    }

    @Override
    public float getParticleCount() {
        return 0;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.hasUUID("SummonerUUID")) {
            this.summonerUUID = compoundTag.getUUID("SummonerUUID");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (summonerUUID != null) {
            compoundTag.putUUID("SummonerUUID", summonerUUID);
        }
    }

    @Override
    public void tick() {
        super.tick();
        tickCounter++;
        if (tickCounter >= 10) {
            tickCounter = 0;
            damageEntities();
        }
    }

    @Override
    public void applyEffect(LivingEntity target) {
        MobEffectInstance slowness = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 2);
        target.addEffect(slowness);
    }

    private void damageEntities() {
        LivingEntity owner = this.getDomainUser();

        var entities = this.level().getEntities(this, this.getBoundingBox());

        if (owner != null) {
            for (var entity : entities) {
                if (entity instanceof LivingEntity target) {
                    if (target.equals(owner)) {
                        continue;
                    }
                    MagicManager.spawnParticles(level(), (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "fleshsplash")), target.getX(), target.getY() + target.getBbHeight() * .5f, target.getZ(), 50, target.getBbWidth() * .25f, target.getBbHeight() * .25f, target.getBbWidth() * .25f, .03, false);

                    double offsetX = random.nextDouble() * 6 - 3;
                    double offsetY = Math.max(random.nextDouble() * 6 - 3, 0.5);
                    double offsetZ = random.nextDouble() * 6 - 3;

                    Vec3 spawnPosition = target.position().add(offsetX, offsetY, offsetZ);

                    Vec3 direction = target.position().subtract(spawnPosition).normalize();

                    BloodSlashProjectile bloodSlash = new BloodSlashProjectile(this.level(), owner);
                    bloodSlash.setPos(spawnPosition);
                    bloodSlash.shoot(direction);
                    bloodSlash.setDamage(7);
                    playSound(target);

                    this.level().addFreshEntity(bloodSlash);
                }
            }
        }
    }


    private void playSound(LivingEntity targetEntity) {
        targetEntity.level().playSound(null, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), SoundRegistry.BLOOD_CAST.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
    }
}
