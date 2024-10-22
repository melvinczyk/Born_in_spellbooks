package net.melvinczyk.borninspellbooks.entity.spells.pumpkins;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import net.minecraft.world.entity.ai.attributes.Attributes;


import java.util.Objects;
import java.util.Optional;

public class PumpkinBombProjectile extends AbstractMagicProjectile implements GeoAnimatable {
    public PumpkinBombProjectile(EntityType<? extends PumpkinBombProjectile> pEntityType, Level Level)
    {
        super(pEntityType, Level);
    }

    public PumpkinBombProjectile(Level pLevel, LivingEntity shooter) {
        this(MAEntityRegistry.PUMPKIN_BOMB.get(), pLevel);
        setOwner(shooter);
    }

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public float getSpeed() {
        return 0.9f;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundEvents.SLIME_DEATH_SMALL);
    }

    @Override
    protected void doImpactSound(SoundEvent sound) {
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.setDeltaMovement(0, 0, 0);
        Vec3 position = this.getPosition(0);
        PumpkinFriend pumpkinFriend = new PumpkinFriend(level(),(LivingEntity) this.getOwner());
        pumpkinFriend.setPos(position);
        Objects.requireNonNull(pumpkinFriend.getAttributes().getInstance(Attributes.MAX_HEALTH)).setBaseValue(12);
        level().addFreshEntity(pumpkinFriend);
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
        controllerRegistrar.add(animationController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }

    private boolean playSwingAnimation = true;
    private final RawAnimation animationBuilder = RawAnimation.begin().thenPlay("idle");
    private final AnimationController animationController = new AnimationController(this, "controller", 0, this::predicate);

    private PlayState predicate(software.bernie.geckolib.core.animation.AnimationState event) {

        if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            if (playSwingAnimation) {
                event.getController().setAnimation(animationBuilder);
                event.getController().setAnimationSpeed(0.75F);
                playSwingAnimation = false;
            }
        }

        return PlayState.CONTINUE;
    }
}
