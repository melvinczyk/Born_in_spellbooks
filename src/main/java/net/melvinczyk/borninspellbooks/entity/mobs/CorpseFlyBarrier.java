package net.melvinczyk.borninspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.mcreator.borninchaosv.entity.CorpseFlyEntity;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class CorpseFlyBarrier extends CorpseFlyEntity implements MagicSummon {

    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;

    public CorpseFlyBarrier(EntityType<? extends CorpseFlyEntity> entityType, Level level) {
        super((EntityType<CorpseFlyEntity>) entityType, level);
        this.setNoGravity(true);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.refreshDimensions();
        xpReward = 0;
    }

    public CorpseFlyBarrier(Level level, LivingEntity owner) {
        this(MAEntityRegistry.CORPSEFLY_PATHFINDER.get(), level);
        setSummoner(owner);
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1)
                .add(Attributes.JUMP_STRENGTH, 2.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .build();
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

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
    }


    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    public void tick() {
        if (!level().isClientSide) {
            LivingEntity owner = getSummoner();
            if (owner == null || !owner.isAlive()) {
                this.discard();
                return;
            }

            float rotationSpeed = 1.0F;
            float orbitRadius = 2.0F;
            float tick = owner.tickCount + this.level().getGameTime();

            float angle = tick * rotationSpeed;

            double offsetX = orbitRadius * Math.cos(angle);
            double offsetZ = orbitRadius * Math.sin(angle);

            Vec3 ownerPos = owner.position();

            this.setPos(ownerPos.x + offsetX, ownerPos.y, ownerPos.z + offsetZ);

            this.setYRot((tick * rotationSpeed * 360F) % 360F);
        }

        super.tick();
    }



    @Override
    protected @NotNull PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, pLevel);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public LivingEntity getSummoner() {
        return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
    }

    @Override
    public void onUnSummon() {
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }
}
