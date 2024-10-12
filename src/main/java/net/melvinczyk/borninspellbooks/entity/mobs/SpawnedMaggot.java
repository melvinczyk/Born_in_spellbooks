package net.melvinczyk.borninspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.mcreator.borninchaosv.entity.MaggotEntity;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
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

public class SpawnedMaggot extends MaggotEntity implements MagicSummon {
    public SpawnedMaggot(EntityType<? extends MaggotEntity> pEntityType, Level pLevel) {
        super((EntityType<MaggotEntity>) pEntityType, pLevel);
        xpReward = 0;
    }

    public void assignTarget(LivingEntity target) {
        this.setTarget(target);
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .build();
    }


    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;

    public void setSummoner(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
    }

    @Override
    public void onUnSummon() {

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
    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if (pEntity instanceof LivingEntity && pEntity.getUUID().equals(this.getSummoner().getUUID())) {
            return false;
        }
        return Utils.doMeleeAttack(this, pEntity, MASpellRegistry.INFECT_HOST.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    private int lifetime = 200;
    @Override
    public void tick() {
        super.tick();
        this.lifetime--;
        if (this.lifetime <= 0)
        {
            this.kill();
        }
        if (this.getTarget() != null) {
            LivingEntity target = this.getTarget();

            if (!target.isAlive()) {
                this.kill();
            }
        }
    }
}
