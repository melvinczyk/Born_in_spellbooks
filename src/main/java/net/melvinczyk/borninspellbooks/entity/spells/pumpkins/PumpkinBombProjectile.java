package net.melvinczyk.borninspellbooks.entity.spells.pumpkins;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.melvinczyk.borninspellbooks.registry.MAMobEffectRegistry;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import net.minecraft.world.entity.ai.attributes.Attributes;


import java.util.Objects;
import java.util.Optional;

public class PumpkinBombProjectile extends AbstractMagicProjectile implements GeoAnimatable {
    public PumpkinBombProjectile(EntityType<? extends PumpkinBombProjectile> pEntityType, Level Level)
    {
        super(pEntityType, Level);
    }

    protected float friendHealth = 1;
    protected float friendDamage = 0;
    protected float explosionDamage = 1;

    public PumpkinBombProjectile(Level pLevel, LivingEntity shooter, float friendHealth, float friendDamage, float explosionDamage) {
        this(MAEntityRegistry.PUMPKIN_BOMB.get(), pLevel);
        setOwner(shooter);
        this.friendHealth = friendHealth;
        this.friendDamage = friendDamage;
        this.explosionDamage = explosionRadius;
    }

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public float getSpeed() {
        return 0.7f;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundEvents.WOOD_BREAK);
    }

    @Override
    protected void doImpactSound(SoundEvent sound) {
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.setDeltaMovement(0, 0, 0);

        int summonTime = 20 * 30;

        LivingEntity player = (LivingEntity) this.getOwner();
        Vec3 position = this.getPosition(0);
        PumpkinFriend pumpkinFriend = new PumpkinFriend(level(),(LivingEntity) player, explosionDamage);
        pumpkinFriend.setPos(position);
        Objects.requireNonNull(pumpkinFriend.getAttributes().getInstance(Attributes.MAX_HEALTH)).setBaseValue(friendHealth);
        Objects.requireNonNull(pumpkinFriend.getAttributes().getInstance(Attributes.ATTACK_DAMAGE)).setBaseValue(friendDamage);
        level().addFreshEntity(pumpkinFriend);
        playSound(pumpkinFriend);

        if (!this.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            MagicManager.spawnParticles(serverLevel, ParticleTypes.HAPPY_VILLAGER, pumpkinFriend.getX(), pumpkinFriend.getY() + pumpkinFriend.getBbHeight() * .5f, pumpkinFriend.getZ(), 25, pumpkinFriend.getBbWidth() * .5f, pumpkinFriend.getBbHeight() * .5f, pumpkinFriend.getBbWidth() * .5f, .03, false);
        }
        pumpkinFriend.addEffect(new MobEffectInstance(MAMobEffectRegistry.PUMPKIN_FRIEND_TIMER.get(), summonTime, 0, false, false, false));
        int effectAmplifier = 0;
        player.addEffect(new MobEffectInstance(MAMobEffectRegistry.PUMPKIN_FRIEND_TIMER.get(), summonTime, effectAmplifier, false, false, true));

        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        var target = entityHitResult.getEntity();
        DamageSources.applyDamage(target, getDamage(), MASpellRegistry.PUMPKIN_FRIEND.get().getDamageSource(this, getOwner()));
        this.impactParticles(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(0, 0, 0);
    }

    @Override
    public void trailParticles() {
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }

    private void playSound(LivingEntity targetEntity)
    {
        targetEntity.level().playSound(null, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), SoundEvents.BEEHIVE_EXIT, SoundSource.HOSTILE, 1.0F, 1.0F);
    }
}
