package net.melvinczyk.melsadditions.entity.mobs;

import com.github.alexthe666.alexsmobs.entity.EntityBoneSerpent;
import com.github.alexthe666.alexsmobs.entity.ai.BoneSerpentAIJump;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import net.melvinczyk.melsadditions.registry.MAEntityRegistry;
import net.melvinczyk.melsadditions.registry.MAMobEffectRegistry;
import net.melvinczyk.melsadditions.registry.SpellRegistries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleTypes;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

import javax.annotation.Nullable;
import java.util.UUID;

public class SummonedBoneSerpent extends EntityBoneSerpent implements MagicSummon {

    public SummonedBoneSerpent(EntityType<? extends EntityBoneSerpent> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        xpReward = 0;
    }

    public SummonedBoneSerpent(Level pLevel, LivingEntity owner) {
        this(MAEntityRegistry.SUMMONED_BONE_SERPENT.get(), pLevel);
        setSummoner(owner);
    }

    public void registerGoals() {

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BoneSerpentAttackGoal());
        this.goalSelector.addGoal(0, new BoneSerpentAIJump(this, 10));
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

    @Override
    public float getStepHeight() {
        return 1f;
    }

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        this.onDeathHelper();
        super.die(pDamageSource);
    }

    @Override
    public void onRemovedFromWorld() {
        this.onRemovedHelper(this, MAMobEffectRegistry.BONE_SERPENT_TIMER.get());
        super.onRemovedFromWorld();
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
        return Utils.doMeleeAttack(this, pEntity, SpellRegistries.SUMMON_BONE_SERPENT.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (shouldIgnoreDamage(pSource))
            return false;
        return super.hurt(pSource, pAmount);
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.JUMP_STRENGTH, 2.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .build();
    }

    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
    }

    public void onUnSummon() {
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.ASH, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
            discard();
        }
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Mob) {
            return (Mob) entity;
        } else {
            entity = this.getFirstPassenger();
            if (entity instanceof Player) {
                return (Player) entity;
            }

            return null;
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (this.isVehicle()) {
            return super.mobInteract(pPlayer, pHand);
        }
        if (pPlayer == getSummoner()) {
            this.doPlayerRide(pPlayer);
        }
        return InteractionResult.sidedSuccess(level().isClientSide);
    }


    protected void doPlayerRide(Player pPlayer) {
        if (!level().isClientSide) {
            pPlayer.setYRot(this.getYRot());
            pPlayer.setXRot(this.getXRot());
            pPlayer.startRiding(this);
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isVehicle()) {
            Player player = (Player) this.getControllingPassenger();
            this.setYRot(player.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(player.getXRot() * 0.3F);

            if (this.isInLava()) {
                this.setSpeed((float) (this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * 15.0));
                super.travel(new Vec3(player.xxa * 0.6F, travelVector.y, player.zza * 0.6F));
            } else {
                this.setSpeed((float) (this.getAttribute(Attributes.MOVEMENT_SPEED).getValue()));
                super.travel(new Vec3(player.xxa * 0.3F, travelVector.y, player.zza * 0.3F));
            }
        } else {
            super.travel(travelVector);
        }
    }



    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.FALL) || source.is(DamageTypes.DROWN) || source.is(DamageTypes.IN_WALL)  || source.is(DamageTypes.LAVA) || source.is(DamageTypeTags.IS_FIRE) || super.isInvulnerableTo(source);
    }


    class BoneSerpentAttackGoal extends MeleeAttackGoal {
        public BoneSerpentAttackGoal() {
            super(SummonedBoneSerpent.this, 1.25D, true);
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            double d0 = this.getAttackReachSqr(pEnemy);
            if (pDistToEnemySqr <= d0 && this.isTimeToAttack()) {
                this.resetAttackCooldown();
                this.mob.doHurtTarget(pEnemy);
            } else if (pDistToEnemySqr <= d0 * 2.0D) {
                if (this.isTimeToAttack()) {
                    this.resetAttackCooldown();
                }

            } else {
                this.resetAttackCooldown();
            }
        }

        @Override
        public void stop() {
            super.stop();
        }
    }
}