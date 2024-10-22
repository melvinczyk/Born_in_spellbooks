package net.melvinczyk.borninspellbooks.entity.spells.pumpkins;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;



import java.util.Optional;

public class PumpkinProjectile extends AbstractMagicProjectile implements GeoAnimatable {
    public PumpkinProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public PumpkinProjectile(Level pLevel, LivingEntity shooter) {
        this(MAEntityRegistry.PUMPKIN_PROJECTILE.get(), pLevel);
        setOwner(shooter);
    }

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

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);


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
}
