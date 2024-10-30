package net.melvinczyk.borninspellbooks.entity.spells.spirit_copy;

import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import net.melvinczyk.borninspellbooks.entity.spells.phantom_copy.PhantomCopyHumanoid;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class SpiritCopyHumanoid extends FrozenHumanoid {
    protected static final EntityDataAccessor<Optional<UUID>> DATA_PLAYER_UUID = SynchedEntityData.defineId(PhantomCopyHumanoid.class, EntityDataSerializers.OPTIONAL_UUID);


    private static final int LIFETIME_TICKS = 3 * 20;
    private int deathTimer = LIFETIME_TICKS;
    private UUID summonerUUID;
    protected int spellPower = 0;
    protected int radius = 0;

    public SpiritCopyHumanoid(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected Player player;
    private void setPlayer(Player player) {
        this.player = player;
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
}
