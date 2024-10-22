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

        this.hasHitBlock = true;
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

    private int tickCounter = 0; // Counter to track ticks
    private boolean hasHitBlock = false; // Track if the projectile has hit a block
    private static final int BOMB_COUNT = 3; // Number of bombs to spawn
    private int currentAngleIndex = 0;

    @Override
    public void tick() {
        super.tick(); // Call the superclass tick method

        if (hasHitBlock) {
            tickCounter++; // Increment the tick counter

            // Check if 40 ticks have passed
            if (tickCounter % 40 == 0) {
                spawnPumpkinBombs();
                currentAngleIndex = (currentAngleIndex + 1) % BOMB_COUNT; // Move to the next angle
            }

            // Optional: Stop ticking after a certain number of bomb spawns
            if (tickCounter > 160) { // Example: Stop after 4 bomb spawns
                this.discard();
            }
        }
    }

    private void spawnPumpkinBombs() {
        int degreesPerPumpkin = 360 / BOMB_COUNT; // Degrees between each projectile
        Vec3 origin = this.position();
        for (int i = 0; i < BOMB_COUNT; i++) {

            Vec3 motion = new Vec3(0, 0, .3 + BOMB_COUNT * .01f);
            motion = motion.xRot(75 * Mth.DEG_TO_RAD);
            motion = motion.yRot(degreesPerPumpkin * i * Mth.DEG_TO_RAD);


            PumpkinBombProjectile head = new PumpkinBombProjectile(level(), (LivingEntity) this.getOwner());

            Vec3 spawn = origin.add(motion.multiply(1, 0, 1).normalize().scale(.3f));
            var angle = Utils.rotationFromDirection(motion);

            head.moveTo(spawn.x, spawn.y - head.getBoundingBox().getYsize() / 2, spawn.z, angle.y, angle.x);
            level().addFreshEntity(head);
        }
    }


}
