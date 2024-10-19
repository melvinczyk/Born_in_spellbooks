package net.melvinczyk.borninspellbooks.entity.spells.phantom_copy;

import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class PhantomCopyHumanoid extends FrozenHumanoid {

    protected static final EntityDataAccessor<Boolean> DATA_IS_BABY = SynchedEntityData.defineId(PhantomCopyHumanoid.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> DATA_IS_SITTING = SynchedEntityData.defineId(PhantomCopyHumanoid.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> DATA_FROZEN_SPEED = SynchedEntityData.defineId(PhantomCopyHumanoid.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> DATA_LIMB_SWING = SynchedEntityData.defineId(PhantomCopyHumanoid.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> DATA_LIMB_SWING_AMOUNT = SynchedEntityData.defineId(PhantomCopyHumanoid.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> DATA_ATTACK_TIME = SynchedEntityData.defineId(PhantomCopyHumanoid.class, EntityDataSerializers.FLOAT);

    protected static final EntityDataAccessor<Optional<UUID>> DATA_PLAYER_UUID = SynchedEntityData.defineId(PhantomCopyHumanoid.class, EntityDataSerializers.OPTIONAL_UUID);

    private int deathTimer = -1;
    private UUID summonerUUID;

    public PhantomCopyHumanoid(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected Player player;

    private void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        if (this.level().isClientSide) {
            Optional<UUID> playerUUID = this.entityData.get(DATA_PLAYER_UUID);
            if (playerUUID.isPresent()) {
                return Minecraft.getInstance().level.getPlayerByUUID(playerUUID.get());
            }
        } else if (this.level() instanceof ServerLevel) {
            Optional<UUID> playerUUID = this.entityData.get(DATA_PLAYER_UUID);
            if (playerUUID.isPresent()) {
                return (Player) ((ServerLevel) this.level()).getEntity(playerUUID.get());
            }
        }
        return null;
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_BABY, false);
        this.entityData.define(DATA_IS_SITTING, false);
        this.entityData.define(DATA_FROZEN_SPEED, 0f);
        this.entityData.define(DATA_LIMB_SWING, 0f);
        this.entityData.define(DATA_LIMB_SWING_AMOUNT, 0f);
        this.entityData.define(DATA_ATTACK_TIME, 0f);
        this.entityData.define(DATA_PLAYER_UUID, Optional.empty());
    }

    private boolean isAutoSpinAttack;
    private HumanoidArm mainArm = HumanoidArm.RIGHT;

    public PhantomCopyHumanoid(Level level, LivingEntity entityToCopy, Player caster) {
        this(MAEntityRegistry.PHANTOM_COPY.get(), level);
        setPlayer(caster);
        this.entityData.set(DATA_PLAYER_UUID, Optional.of(caster.getUUID()));
        this.moveTo(entityToCopy.getX(), entityToCopy.getY(), entityToCopy.getZ(), entityToCopy.getYRot(), entityToCopy.getXRot());
        if (entityToCopy.isBaby())
            this.entityData.set(DATA_IS_BABY, true);
        if (entityToCopy.isPassenger() && (entityToCopy.getVehicle() != null && entityToCopy.getVehicle().shouldRiderSit()))
            this.entityData.set(DATA_IS_SITTING, true);

        this.setYBodyRot(entityToCopy.yBodyRot);
        this.yBodyRotO = this.yBodyRot;
        this.setYHeadRot(entityToCopy.getYHeadRot());
        this.yHeadRotO = this.yHeadRot;

        float limbSwing = entityToCopy.walkAnimation.speed();
        float limbSwingAmount = entityToCopy.walkAnimation.position();

        this.entityData.set(DATA_LIMB_SWING, limbSwing);
        this.entityData.set(DATA_LIMB_SWING_AMOUNT, limbSwingAmount);

        this.entityData.set(DATA_ATTACK_TIME, entityToCopy.attackAnim);
        this.setPose(entityToCopy.getPose());
        this.isAutoSpinAttack = entityToCopy.isAutoSpinAttack();
        this.mainArm = entityToCopy.getMainArm();

        if (entityToCopy instanceof Player player) {
            this.setCustomName(player.getDisplayName());
            this.setCustomNameVisible(true);
        } else {
            this.setCustomNameVisible(false);
        }

        setSummoner(entityToCopy);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (level().isClientSide || this.isInvulnerableTo(pSource))
            return false;

        explodeOnDeath();
        this.discard();
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (deathTimer > 0) {
            deathTimer--;

            if (deathTimer == 0) {
                explodeOnDeath();
                this.hurt(level().damageSources().generic(), 100);
            }
        }
    }


    private void explodeOnDeath() {
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3.0F, Level.ExplosionInteraction.MOB);
    }


    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.hasUUID("Summoner")) {
            this.summonerUUID = compoundTag.getUUID("Summoner");
        }
        if (compoundTag.hasUUID("PlayerUUID")) {
            UUID playerUUID = compoundTag.getUUID("PlayerUUID");
            if (this.level() instanceof ServerLevel serverLevel) {
                Entity entity = serverLevel.getEntity(playerUUID);
                if (entity instanceof Player playerEntity) {
                    this.player = playerEntity;
                }
            }
        }
    }

    @Override
    public boolean isAutoSpinAttack() {
        return this.isAutoSpinAttack;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.summonerUUID != null) {
            compoundTag.putUUID("Summoner", this.summonerUUID);
        }
        if (this.player != null) {
            compoundTag.putUUID("PlayerUUID", this.player.getUUID());
        }
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.FOLLOW_RANGE, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0);
    }
}
