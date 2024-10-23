package net.melvinczyk.borninspellbooks.entity.spells.infernal_arrow;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.melvinczyk.borninspellbooks.entity.spells.infernal_bomb.InfernalFireField;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class InfernalArrowProjectile extends AbstractMagicProjectile {
    private BlockPos lastHitBlock = BlockPos.ZERO;

    @Override
    public float getSpeed() {
        return 3.0f;
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.position().subtract(getDeltaMovement());
        level().addParticle((ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_surge")), vec3.x, vec3.y, vec3.z, 0, 0, 0);
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected boolean shouldPierceShields() {
        return false;
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level(), (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_surge")), x, y, z, 15, .1, .1, .1, .5, false);
    }

    public InfernalArrowProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(false);
    }

    public InfernalArrowProjectile(Level levelIn, LivingEntity shooter) {
        this(MAEntityRegistry.INFERNAL_ARROW_PROJECTILE.get(), levelIn);
        this.setNoGravity(false);
        setOwner(shooter);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();

        DamageSources.applyDamage(entity, damage, MASpellRegistry.INFERNAL_ARROW.get().getDamageSource(this, getOwner()));

        MobEffectInstance infernalFlameEffect = new MobEffectInstance(
                ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_flame")),
                80,
                1
        );
        ((LivingEntity) entity).addEffect(infernalFlameEffect);

        explodeAtEntity(entity);
        discard();
    }

    @Override
    protected void onHit(HitResult result) {
        if (!level().isClientSide) {
            var blockPos = BlockPos.containing(result.getLocation());

            if (result.getType() == HitResult.Type.BLOCK) {
                if (!blockPos.equals(lastHitBlock)) {
                    lastHitBlock = blockPos;
                    createFireField(new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
                }
            } else if (result.getType() == HitResult.Type.ENTITY) {
                level().playSound(null, BlockPos.containing(position()), SoundRegistry.FORCE_IMPACT.get(), SoundSource.NEUTRAL, 2, .65f);
            }
        }
        super.onHit(result);
    }

    private void explodeAtEntity(Entity entity) {
        Vec3 position = entity.position();

        entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), 2.0F, Level.ExplosionInteraction.NONE);
        createFireField(position);
    }

    public void createFireField(Vec3 location) {
        if (!level().isClientSide) {
            InfernalFireField fire = new InfernalFireField(level());
            fire.setOwner(getOwner());
            fire.setDuration(40);
            fire.setEffectDuration(40);
            fire.setDamage(damage / 5);
            fire.setRadius(10);
            fire.setCircular();
            fire.moveTo(location);
            level().addFreshEntity(fire);
        }
    }
}
