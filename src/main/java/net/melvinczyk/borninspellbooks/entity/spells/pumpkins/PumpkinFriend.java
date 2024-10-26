package net.melvinczyk.borninspellbooks.entity.spells.pumpkins;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.mcreator.borninchaosv.entity.PumpkinBombEntity;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class PumpkinFriend extends PumpkinBombEntity implements MagicSummon {
    public PumpkinFriend(EntityType<? extends PumpkinBombEntity> pEntityType, Level pLevel) {
        super((EntityType<PumpkinBombEntity>) pEntityType, pLevel);
        xpReward = 0;
    }

    protected float explosionDamage = 1;

    public PumpkinFriend(Level pLevel, LivingEntity owner, float newDamage) {
        this(MAEntityRegistry.PUMPKIN_FRIEND.get(), pLevel);
        setSummoner(owner);
        this.explosionDamage = newDamage;
    }

    public void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2f, false));
        this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 0.9f, 15, 5, false, 25));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 6.0F));

        this.targetSelector.addGoal(1, new GenericOwnerHurtByTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(2, new GenericOwnerHurtTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(3, new GenericCopyOwnerTargetGoal(this, this::getSummoner));
        this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());
    }


    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.summonerUUID = OwnerHelper.deserializeOwner(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        OwnerHelper.serializeOwner(compoundTag, summonerUUID);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack(this, pEntity, MASpellRegistry.PUMPKIN_FRIEND.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && shouldIgnoreDamage(pSource))
            return false;
        return super.hurt(pSource, pAmount);
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.JUMP_STRENGTH, 2.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .build();
    }

    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
    }

    public void onUnSummon() {
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.POOF, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
            discard();
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isVehicle()) {
            Player player = (Player) this.getControllingPassenger();
            this.setYRot(player.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(player.getXRot() * 0.3F);

            this.setSpeed((float) (this.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
            super.travel(new Vec3(player.xxa * 0.3F, travelVector.y, player.zza * 0.3F));

        } else {
            super.travel(travelVector);
        }
    }

    private boolean hasExploded = false;
    @Override
    public void tick()
    {
        super.tick();
        if (this.getHealth() <= 0 && !hasExploded)
        {
            hasExploded = true;
            explodeOnDeath();
        }
    }

    private void explodeOnDeath()
    {
        float explosionRadius = 5;
        DamageSource explosionSource = MASpellRegistry.PUMPKIN_FRIEND.get().getDamageSource(this, getSummoner());
        var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));
        LivingEntity player = getSummoner();
        for (Entity entity : entities) {
            if (entity.equals(player))
            {
                continue;
            }

            double distance = entity.position().distanceTo(this.position());
            if (distance < explosionRadius) {
                DamageSources.applyDamage(entity, this.explosionDamage, explosionSource);
                entity.invulnerableTime = 0;
            }
        }
        Random random = new Random();

        if (!this.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            for (int i = 0; i < 100; i++) {
                double xOffset = (random.nextDouble() - 0.5) * 5;
                double yOffset = (random.nextDouble() - 0.5) * 5;
                double zOffset = (random.nextDouble() - 0.5) * 5;

                MagicManager.spawnParticles(
                        serverLevel,
                        (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_surge")),
                        getX() + xOffset,
                        getY() + yOffset,
                        getZ() + zOffset,
                        1,
                        0.1, 0.1, 0.1,
                        0.03,
                        false
                );
            }
        }

        playSound(this);
        this.discard();
    }

    private void playSound(LivingEntity targetEntity)
    {
        targetEntity.level().playSound(null, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.0F, 1.0F);
    }
}
