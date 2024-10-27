package net.melvinczyk.borninspellbooks.entity.spells.phantom_copy;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

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

    private static final int LIFETIME_TICKS = 3 * 20;
    private int deathTimer = LIFETIME_TICKS;
    private UUID summonerUUID;
    protected int spellPower = 0;
    protected int radius = 0;

    public PhantomCopyHumanoid(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected Player player;
    protected Entity target;

    private void setPlayer(Player player) {
        this.player = player;
    }

    private void setTarget(Entity entity)
    {
        this.target = entity;
    }

    public Player getPlayer() {
        if (!this.entityData.hasItem(DATA_PLAYER_UUID)) {
            return null;
        }
        Optional<UUID> playerUUID = this.entityData.get(DATA_PLAYER_UUID);

        if (playerUUID.isPresent()) {
            if (this.level().isClientSide) {
                return Minecraft.getInstance().level.getPlayerByUUID(playerUUID.get());
            } else if (this.level() instanceof ServerLevel) {
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

    public PhantomCopyHumanoid(Level level, LivingEntity entityToCopy, Player caster, Entity target, int duration) {
        this(MAEntityRegistry.PHANTOM_COPY.get(), level);
        setPlayer(caster);
        setTarget(target);
        this.entityData.set(DATA_PLAYER_UUID, Optional.of(caster.getUUID()));
        this.spellPower = (int)(duration * 0.015f);
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
        spawnMovementParticles();

        if (this.player != null && this.player.isAlive() && this.target != null && this.target.isAlive()) {
            followTarget(this.target);
        }

        if (deathTimer > 0) {
            deathTimer--;

            if (deathTimer <= 0) {
                explodeOnDeath();
                this.hurt(level().damageSources().generic(), 100);
                this.discard();
            }
        }
    }


    private void followTarget(Entity target) {
        double speed = 0.2;
        double dx = target.getX() - this.getX();
        double dy = target.getY() - this.getY();
        double dz = target.getZ() - this.getZ();

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance > 0.1) {
            this.setDeltaMovement(dx / distance * speed, dy / distance * speed, dz / distance * speed);
            this.lookAt(EntityAnchorArgument.Anchor.FEET, target.position());

        } else {
            this.setDeltaMovement(0, 0, 0);
        }
    }

    private void spawnMovementParticles() {
        int particleCount = 5;

        ParticleOptions particleType = ParticleTypes.PORTAL;

        double posX = this.getX();
        double posY = this.getY() + 0.5;
        double posZ = this.getZ();

        for (int i = 0; i < particleCount; i++) {
            double offsetX = (this.level().random.nextDouble() - 0.5) * 0.2;
            double offsetY = (this.level().random.nextDouble() - 0.5) * 0.2;
            double offsetZ = (this.level().random.nextDouble() - 0.5) * 0.2;

            this.level().addParticle(particleType,
                    posX + offsetX,
                    posY + offsetY,
                    posZ + offsetZ,
                    0, 0, 0);
        }
    }

    private boolean hasExploded = false;
    private void explodeOnDeath() {
        if (!hasExploded) {
            hasExploded = true;
            float explosionRadius = 5;
            DamageSource explosionSource = MASpellRegistry.PHANTOM_SPLIT.get().getDamageSource(this, getSummoner());
            var entities = level().getEntities(this, this.getBoundingBox().inflate(explosionRadius));
            Player player = getPlayer();
            for (Entity entity : entities) {
                if (player != null && entity.getUUID().equals(player.getUUID())) {
                    continue;
                }

                double distance = entity.position().distanceTo(this.position());
                if (distance < explosionRadius) {
                    float damage = (float) this.spellPower;
                    DamageSources.applyDamage(entity, damage, explosionSource);
                    entity.invulnerableTime = 0;
                }
            }
            if (!this.level().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) this.level();
                for (int i = 0; i < 200; i++) {
                    double xOffset = (random.nextDouble() - 0.5) * 5;
                    double yOffset = (random.nextDouble() - 0.5) * 5;
                    double zOffset = (random.nextDouble() - 0.5) * 5;

                    MagicManager.spawnParticles(
                            serverLevel,
                             ParticleTypes.PORTAL,
                            getX() + xOffset,
                            getY() + yOffset,
                            getZ() + zOffset,
                            3,
                            0.15, 0.15, 0.15,
                            0.03,
                            false
                    );
                }
            }
            playSound(this);
        }
    }

    private void playSound(LivingEntity targetEntity)
    {
        targetEntity.level().playSound(null, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1.0F, 1.0F);
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
