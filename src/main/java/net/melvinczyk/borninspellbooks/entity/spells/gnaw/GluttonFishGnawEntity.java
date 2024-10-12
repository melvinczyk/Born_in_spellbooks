package net.melvinczyk.borninspellbooks.entity.spells.gnaw;

import net.mcreator.borninchaosv.entity.GluttonFishEntity;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class GluttonFishGnawEntity extends GluttonFishEntity implements GeoEntity {
    private int biteTimer = 0;
    private final int biteDuration = 20;
    private final RawAnimation attackAnimation = RawAnimation.begin().thenPlay("attack");

    public GluttonFishGnawEntity(EntityType<? extends GluttonFishEntity> pEntityType, Level pLevel) {
        super((EntityType<GluttonFishEntity>) pEntityType, pLevel);
        xpReward = 0;
        this.noPhysics = true;  // Makes the entity stay in place
    }

    public GluttonFishGnawEntity(Level pLevel, LivingEntity owner) {
        this(MAEntityRegistry.GLUTTON_FISH_GNAW.get(), pLevel);
    }

    @Override
    public void tick() {
        super.tick();

        // Keep the entity in place by setting its motion to zero
        this.setDeltaMovement(0, 0, 0);
        this.setNoGravity(true);  // Disable gravity so it stays floating in the air

        if (biteTimer == 0) {
            // Trigger animation (adjust depending on your animation library)
            startBiteAnimation();
        }

        // Damage logic at mid-animation
        if (biteTimer == biteDuration / 2) {
            dealAoEDamage();
        }

        // Despawn after the bite
        if (biteTimer >= biteDuration) {
            this.remove(RemovalReason.DISCARDED);
        }

        biteTimer++;
    }
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private void startBiteAnimation() {
        // Start the attack animation (using GeckoLib for this example)

    }

    private void dealAoEDamage() {
        // Define AoE size and damage
        int damageRadius = 3; // Example: 3 block radius
        int damageAmount = 6; // Example: 6 damage points

        AABB aoeBox = new AABB(this.blockPosition().offset(-damageRadius, -damageRadius, -damageRadius),
                this.blockPosition().offset(damageRadius, damageRadius, damageRadius));

        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, aoeBox);

        for (LivingEntity target : targets) {
            if (target != this) { // Exclude self from damage
                target.kill();
            }
        }
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.JUMP_STRENGTH, 2.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)  // No movement speed, since it's stationary
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .build();
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER; // Fits with the Glutton Fish theme
    }

    @Override
    protected void registerGoals() {
        // No goals, since this is a simple, stationary entity
    }
}
