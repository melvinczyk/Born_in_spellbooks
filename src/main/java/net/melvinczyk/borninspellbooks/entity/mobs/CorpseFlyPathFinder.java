package net.melvinczyk.borninspellbooks.entity.mobs;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.goals.WispAttackGoal;
import net.mcreator.borninchaosv.entity.BloodyGadflyEntity;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;


public class CorpseFlyPathFinder extends BloodyGadflyEntity implements GeoEntity {

    @Nullable
    private UUID ownerUUID;

    @Nullable
    private LivingEntity cachedOwner;

    private final RawAnimation animation = RawAnimation.begin().thenPlay("walk");

    private Vec3 lastTickPos;
    private float damageAmount;

    public CorpseFlyPathFinder(EntityType<? extends BloodyGadflyEntity> entityType, Level level) {
        super((EntityType<BloodyGadflyEntity>) entityType, level);
        this.setNoGravity(true);
        this.refreshDimensions();
        //setSize(0.1F, 2.0F);
    }

    public CorpseFlyPathFinder(Level levelIn, LivingEntity owner, float damageAmount) {
        this(MAEntityRegistry.CORPSEFLY_PATHFINDER.get(), levelIn);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.damageAmount = damageAmount;

        setOwner(owner);

        var xRot = owner.getXRot();
        var yRot = owner.getYRot();
        var yHeadRot = owner.getYHeadRot();

        this.setYRot(yRot);
        this.setXRot(xRot);
        this.setYBodyRot(yRot);
        this.setYHeadRot(yHeadRot);
        this.lastTickPos = this.position();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new WispAttackGoal(this, 1));
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public LivingEntity getTarget() {
        return super.getTarget();
    }

    @Override
    public void tick() {
        super.tick();


        var target = this.getTarget();
        if (target == null || target.isRemoved()) {
            if (tickCount > 10) {
                this.popAndDie();
            }
        } else {
            if (this.getBoundingBox().intersects(target.getBoundingBox())) {
                DamageSources.applyDamage(target, damageAmount, MASpellRegistry.INFECT_HOST.get().getDamageSource(this, cachedOwner));
                var p = target.getEyePosition();
                MagicManager.spawnParticles(level(), (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "fleshsplash")), p.x, p.y, p.z, 25, .5f, .5f, .5f, .3, true);
                if (!target.isAlive()) {
                    this.playSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1:stomach_open")), 1.0f, 1.0f);
                } else {
                    this.playSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1:swarmer_death")), 1.0f, 1.0f);

                }
                discard();
                if (target.isAlive()) {
                    var level = target.level();
                    float radius = .5f + .185f * 3;
                    for (int i = 0; i < 3; i++) {
                        SpawnedMaggot maggot = new SpawnedMaggot(level, this.cachedOwner, 140);
                        if (maggot != null) {
                            var yrot = 6.281f / 6 * i + target.getYRot() * Mth.DEG_TO_RAD;
                            maggot.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(maggot.getOnPos()), MobSpawnType.MOB_SUMMONED, null, null);
                            Vec3 spawn = Utils.moveToRelativeGroundLevel(level, target.getEyePosition().add(new Vec3(radius * Mth.cos(yrot), 1, radius * Mth.sin(yrot))), 10);

                            maggot.setPos(spawn.x, spawn.y, spawn.z);
                            level.addFreshEntity(maggot);
                            maggot.assignTarget(target);
                        }
                    }
                }
            }
        }
        lastTickPos = this.position();
    }

    public void setOwner(@Nullable LivingEntity pOwner) {
        if (pOwner != null) {
            this.ownerUUID = pOwner.getUUID();
            this.cachedOwner = pOwner;
        }
    }

    @Override
    protected @NotNull PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel) {
            public boolean isStableDestination(BlockPos blockPos) {
                return !this.level.getBlockState(blockPos.below()).isAir();
            }

            public void tick() {
                super.tick();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return pDimensions.height;
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double) 0.5F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.4D));
            } else {
                this.moveRelative(this.getSpeed(), pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double) 0.6D));
            }
        }

        this.calculateEntityAnimation(false);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void setTarget(@org.jetbrains.annotations.Nullable LivingEntity target) {
        super.setTarget(target);
    }

    @Override
    protected void customServerAiStep() {
        if (this.cachedOwner == null || !this.cachedOwner.isAlive()) {
            this.discard();
        }
    }

    private PlayState predicate(AnimationState event) {
        event.getController().setAnimation(animation);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_KNOCKBACK, 1.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FOLLOW_RANGE, 40.0)
                .add(Attributes.FLYING_SPEED, .4)
                .add(Attributes.MOVEMENT_SPEED, .4);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.level().isClientSide) {
            this.popAndDie();
        }
        return true;
    }

    private void popAndDie() {
        this.playSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1:stomach_open")), 1.0f, 1.0f);

        if (!this.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            serverLevel.sendParticles((ParticleOptions) Objects.requireNonNull(ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "fleshsplash"))), this.getX(), this.getY(), this.getZ(), 25, 0.2D, 0.2D, 0.2D, 0.0D);
            this.discard();
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }
}
