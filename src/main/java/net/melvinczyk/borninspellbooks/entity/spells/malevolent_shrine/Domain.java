package net.melvinczyk.borninspellbooks.entity.spells.malevolent_shrine;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;

import java.util.Optional;
import java.util.UUID;

public class Domain extends AoeEntity {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(Domain.class, EntityDataSerializers.FLOAT);

    private final Random random = new Random();
    private int tickCounter = 0;
    protected LivingEntity caster;
    private UUID summonerUUID;
    private LivingEntity cachedSummoner;
    private float bloodDamage;

    public Domain(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public Domain(Level level, LivingEntity entity, float bloodDamage) {
        this(MAEntityRegistry.DOMAIN.get(), level);
        setDomainUser(entity);
        setBloodDamage(bloodDamage);
    }


    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        float radius = this.getRadius();
        return EntityDimensions.scalable(radius * 2, radius * 2);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
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

    private void setBloodDamage(float bloodDamage)
    {
        this.bloodDamage = bloodDamage;
    }

    public float getBloodDamage()
    {
        return this.bloodDamage;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
            if (getRadius() < .1f)
                this.discard();
        }

        super.onSyncedDataUpdated(pKey);
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
        if (tickCounter >= 20) {
            tickCounter = 0;
            damageEntities();
        }
    }

    @Override
    public void applyEffect(LivingEntity target) {
    }

    private void damageEntities() {
        LivingEntity owner = this.getDomainUser();

        var entities = this.level().getEntities(this, this.getBoundingBox());

        float bloodDamage = getBloodDamage();
        if (owner != null) {
            for (var entity : entities) {
                if (entity instanceof LivingEntity target) {
                    if (target.equals(owner)) {
                        MobEffectInstance resist = new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1, false, false, true);
                        MobEffectInstance strength = new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1, false, false, true);
                        target.addEffect(resist);
                        target.addEffect(strength);
                        continue;
                    }
                    MagicManager.spawnParticles(level(), (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "fleshsplash")), target.getX(), target.getY() + target.getBbHeight() * .5f, target.getZ(), 50, target.getBbWidth() * .25f, target.getBbHeight() * .25f, target.getBbWidth() * .25f, .03, false);

                    MobEffectInstance slowness = new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 160, 2, false, false, false);
                    MobEffectInstance weakness = new MobEffectInstance(MobEffects.WEAKNESS, 160, 1, false, false, false);
                    target.addEffect(slowness);
                    target.addEffect(weakness);
                    double offsetX = random.nextDouble() * 6 - 3;
                    double offsetY = Math.max(random.nextDouble() * 6 - 3, 0.5);
                    double offsetZ = random.nextDouble() * 6 - 3;

                    Vec3 spawnPosition = target.position().add(offsetX, offsetY, offsetZ);

                    Vec3 direction = target.position().subtract(spawnPosition).normalize();

                    BloodSlashProjectile bloodSlash = new BloodSlashProjectile(this.level(), owner);
                    bloodSlash.setPos(spawnPosition);
                    bloodSlash.shoot(direction);
                    bloodSlash.setDamage(bloodDamage);
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
