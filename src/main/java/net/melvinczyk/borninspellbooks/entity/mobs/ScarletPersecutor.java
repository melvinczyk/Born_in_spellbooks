package net.melvinczyk.borninspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.mcreator.borninchaosv.entity.ScarletPersecutorEntity;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;


public class ScarletPersecutor extends ScarletPersecutorEntity implements MagicSummon {
    public ScarletPersecutor(EntityType<? extends ScarletPersecutorEntity> pEntityType, Level pLevel) {
        super((EntityType<ScarletPersecutorEntity>) pEntityType, pLevel);
        xpReward = 0;
    }

    public ScarletPersecutor(Level level, LivingEntity owner)
    {
        this(MAEntityRegistry.SCARLET_PERSECUTOR.get(), level);
        setSummoner(owner);
    }

    public void assignTarget(LivingEntity target) {
        this.setTarget(target);
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.FLYING_SPEED, 1.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .build();
    }


    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
    }

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

    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
    }

    @Override
    public void onUnSummon() {
        if (!level().isClientSide) {
            MagicManager.spawnParticles(level(), ParticleTypes.POOF, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
            discard();
        }
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if (this.getSummoner() == null)
        {
            return false;
        }
        if (pEntity instanceof LivingEntity && pEntity.getUUID().equals(this.getSummoner().getUUID())) {
            return false;
        }
        return Utils.doMeleeAttack(this, pEntity, MASpellRegistry.CURSE.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    private int lifetime = 180;
    @Override
    public void tick() {
        super.tick();
        this.lifetime--;
        if (this.lifetime <= 0)
        {
            this.onUnSummon();
        }
        if (this.getTarget() != null) {
            LivingEntity target = this.getTarget();

            if (!target.isAlive()) {
                this.onUnSummon();
            }
        }
    }
}
