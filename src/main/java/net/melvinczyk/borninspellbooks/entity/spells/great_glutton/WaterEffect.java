package net.melvinczyk.borninspellbooks.entity.spells.great_glutton;

import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class WaterEffect extends AoeEntity {

    private UUID summonerUUID;
    private LivingEntity cachedSummoner;
    private int tickCounter = 0;
    public final int ticksPerFrame = 2;


    public WaterEffect(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public WaterEffect(Level level, LivingEntity entity) {
        this(MAEntityRegistry.WATER_FIELD.get(), level);
        setDomainUser(entity);
    }

    public void setDomainUser(@Nullable LivingEntity owner) {
        if (owner != null) {
            this.summonerUUID = owner.getUUID();
            this.cachedSummoner = owner;
        }
    }

    public LivingEntity getDomainUser() {
        return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.hasUUID("SummonerUUID")) {
            this.summonerUUID = compoundTag.getUUID("SummonerUUID");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (summonerUUID != null) {
            compoundTag.putUUID("SummonerUUID", summonerUUID);
        }
    }

    private boolean shotFish = false;

    @Override
    public void tick() {
        super.tick();
        tickCounter++;
        if (tickCounter >= 60 && !shotFish) {
            shotFish = true;
            GreatGluttonProjectile fish = new GreatGluttonProjectile(level(), getDomainUser());
            fish.setPos(this.position().add(0, -3, 0));
            fish.shoot(new Vec3(0, 1.2f, 0));
            fish.setDamage(20);
            level().addFreshEntity(fish);
        }
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    protected boolean canRide(Entity entity) {
        return false;
    }

    @Override
    public void applyEffect(LivingEntity target) {

    }

    @Override
    public float getParticleCount() {
        return 10;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.of(ParticleTypes.RAIN);
    }

    private void playSound(LivingEntity targetEntity)
    {
        targetEntity.level().playSound(null, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), (SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1:glutton_fish_ambient")), SoundSource.HOSTILE, 1.0F, 1.0F);
    }
}
