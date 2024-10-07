package net.melvinczyk.borninspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.mcreator.borninchaosv.entity.ScarletPersecutorEntity;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.melvinczyk.borninspellbooks.registry.SpellRegistries;
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


public class ScarletPersecutor extends ScarletPersecutorEntity {
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
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.FLYING_SPEED, 0.4D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
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
    public boolean doHurtTarget(Entity pEntity) {
        return Utils.doMeleeAttack(this, pEntity, SpellRegistries.CURSE.get().getDamageSource(this, getSummoner()));
    }

    @Override
    public void die(DamageSource pDamageSource) {
        super.die(pDamageSource);
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    private int lifetime = 100;
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
