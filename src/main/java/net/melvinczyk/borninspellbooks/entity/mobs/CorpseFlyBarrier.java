package net.melvinczyk.borninspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.mcreator.borninchaosv.entity.CorpseFlyEntity;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.minecraft.util.Mth;
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
    @Nullable
    private UUID ownerUUID;
    @Nullable
    private LivingEntity owner;

    protected LivingEntity cachedSummoner;
    protected UUID summonerUUID;
    private double orbitRadius = 2.0;
    private double orbitSpeed = 0.05;
    private float angle;

    public CorpseFlyBarrier(EntityType<? extends CorpseFlyEntity> entityType, Level level) {
        super((EntityType<CorpseFlyEntity>) entityType, level);
        this.setNoGravity(true);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.refreshDimensions();
    }

    public CorpseFlyBarrier(Level level, LivingEntity owner) {
        this(MAEntityRegistry.CORPSEFLY_PATHFINDER.get(), level);
        this.owner = owner;
        this.ownerUUID = owner.getUUID();
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

    @Override
    public void tick() {
        super.tick();

        if (this.owner == null || !this.owner.isAlive()) {
            this.discard();
            return;
        }

        angle += orbitSpeed;
        if (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }

        Vec3 ownerPos = owner.position();

        double offsetX = orbitRadius * Mth.cos((float) angle);
        double offsetZ = orbitRadius * Mth.sin((float) angle);

        this.setPos(ownerPos.x + offsetX, ownerPos.y, ownerPos.z + offsetZ);

        double movementYaw = Mth.atan2(offsetZ, offsetX) * (180F / (float) Math.PI) - 90F;
        this.setYRot((float) movementYaw);
        this.setYHeadRot((float) movementYaw);
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
