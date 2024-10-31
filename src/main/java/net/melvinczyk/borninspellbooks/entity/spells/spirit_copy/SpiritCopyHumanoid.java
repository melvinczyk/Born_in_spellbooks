package net.melvinczyk.borninspellbooks.entity.spells.spirit_copy;

import com.mojang.logging.LogUtils;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class SpiritCopyHumanoid extends FrozenHumanoid {
    protected static final EntityDataAccessor<Optional<UUID>> DATA_PLAYER_UUID = SynchedEntityData.defineId(SpiritCopyHumanoid.class, EntityDataSerializers.OPTIONAL_UUID);

    protected static final EntityDataAccessor<Boolean> DATA_IS_BABY = SynchedEntityData.defineId(SpiritCopyHumanoid.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> DATA_IS_SITTING = SynchedEntityData.defineId(SpiritCopyHumanoid.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> DATA_FROZEN_SPEED = SynchedEntityData.defineId(SpiritCopyHumanoid.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> DATA_LIMB_SWING = SynchedEntityData.defineId(SpiritCopyHumanoid.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> DATA_LIMB_SWING_AMOUNT = SynchedEntityData.defineId(SpiritCopyHumanoid.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> DATA_ATTACK_TIME = SynchedEntityData.defineId(SpiritCopyHumanoid.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> DATA_ATTACK_ANIM = SynchedEntityData.defineId(SpiritCopyHumanoid.class, EntityDataSerializers.FLOAT);

    private static final Logger LOGGER = LogUtils.getLogger();

    private int lifetime;
    private UUID summonerUUID;
    protected Player player;
    private Vec3 playerMovement;
    private float storedYBodyRot;
    private float storedYHeadRot;
    private boolean invulnerable = true;

    private boolean isAutoSpinAttack;
    private HumanoidArm mainArm = HumanoidArm.RIGHT;

    public SpiritCopyHumanoid(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
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
        this.entityData.define(DATA_ATTACK_ANIM, 0.0F);
    }

    public SpiritCopyHumanoid(Level level, LivingEntity entityToCopy, Player caster, int lifetime) {
        this(MAEntityRegistry.SPIRIT_COPY.get(), level);

        try {
            if (caster == null) {
                LOGGER.error("SpiritCopyHumanoid: Caster is null.");
                return;
            }
            if (entityToCopy == null) {
                LOGGER.error("SpiritCopyHumanoid: Entity to copy is null.");
                return;
            }

            this.entityData.set(DATA_PLAYER_UUID, Optional.of(caster.getUUID()));
            this.lifetime = lifetime;

            this.moveTo(entityToCopy.getX(), entityToCopy.getY(), entityToCopy.getZ(), entityToCopy.getYRot(), entityToCopy.getXRot());

            this.setYBodyRot(entityToCopy.yBodyRot);
            this.yBodyRotO = this.yBodyRot;
            this.setYHeadRot(entityToCopy.getYHeadRot());
            this.yHeadRotO = this.yHeadRot;

            this.entityData.set(DATA_LIMB_SWING, entityToCopy.walkAnimation.position()); // Exact current walking position
            this.entityData.set(DATA_LIMB_SWING_AMOUNT, entityToCopy.walkAnimation.speed()); // Walking speed (at that moment)

            this.entityData.set(DATA_ATTACK_TIME, entityToCopy.attackAnim);

            if (entityToCopy.isBaby())
                this.entityData.set(DATA_IS_BABY, true);
            if (entityToCopy.isPassenger() && entityToCopy.getVehicle() != null && entityToCopy.getVehicle().shouldRiderSit())
                this.entityData.set(DATA_IS_SITTING, true);

            if (entityToCopy instanceof Player player) {
                setPlayer(player);
                setMomentum(player.getDeltaMovement());
                this.storedYBodyRot = player.yBodyRot;
                this.storedYHeadRot = player.yHeadRot;
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    this.setItemSlot(slot, player.getItemBySlot(slot));
                }
            } else {
                this.setCustomNameVisible(false);
            }

            this.setPose(entityToCopy.getPose());
            this.isAutoSpinAttack = entityToCopy.isAutoSpinAttack();
            this.mainArm = entityToCopy.getMainArm();

            setSummoner(entityToCopy);
        } catch (Exception e) {
            LOGGER.error("Error when creating Spirit Copy object: ", e);
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        Player player = getPlayer();
        if (player != null) {
            return player.getArmorSlots();
        }
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        Player player = getPlayer();
        if (player != null) {
            if (pSlot.getType() == EquipmentSlot.Type.ARMOR) {
                return player.getItemBySlot(pSlot);
            } else if (pSlot == EquipmentSlot.MAINHAND) {
                return player.getMainHandItem();
            }
        }
        return ItemStack.EMPTY;
    }

    private void setPlayer(Player player) {
        this.player = player;
    }

    private void setMomentum(Vec3 momentum)
    {
        this.playerMovement = momentum;
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
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.summonerUUID != null) {
            compoundTag.putUUID("Summoner", this.summonerUUID);
        }
        if (this.player != null) {
            compoundTag.putUUID("PlayerUUID", this.player.getUUID());
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isAutoSpinAttack() {
        return this.isAutoSpinAttack;
    }

    @Override
    public HumanoidArm getMainArm() {
        return mainArm;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.FOLLOW_RANGE, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (lifetime > 0) {
            lifetime--;
        }

        if (lifetime == 0) {
            teleportPlayerToCopy();
            invulnerable = false;
            this.hurt(level().damageSources().generic(), 100);
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (invulnerable || level().isClientSide || this.isInvulnerableTo(pSource)) {
            return false;
        }

        this.playHurtSound(pSource);
        this.discard();
        return true;
    }

    private void teleportPlayerToCopy() {
        Player player = getPlayer();
        if (player != null && !level().isClientSide) {
            player.teleportTo(this.getX(), this.getY(), this.getZ());


            player.setYBodyRot(this.storedYBodyRot);
            player.setYHeadRot(this.storedYHeadRot);

            if (this.playerMovement != null) {
                player.setDeltaMovement(this.playerMovement);
            }
            player.hurtMarked = true;
        }
    }


}
