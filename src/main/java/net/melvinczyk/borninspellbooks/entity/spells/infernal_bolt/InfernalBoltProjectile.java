package net.melvinczyk.borninspellbooks.entity.spells.infernal_bolt;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class InfernalBoltProjectile extends AbstractMagicProjectile {
    public InfernalBoltProjectile(EntityType<? extends InfernalBoltProjectile> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
    }

    private int effectAmplifier;

    public InfernalBoltProjectile(Level levelIn, LivingEntity shooter, int spellLevel) {
        this(MAEntityRegistry.INFERNAL_BOLT_PROJECTILE.get(), levelIn);
        setOwner(shooter);
        effectAmplifier = (spellLevel - 1) / 4 +1;
    }

    @Override
    public float getSpeed() {
        return 1.75f;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundEvents.FIREWORK_ROCKET_BLAST);
    }

    @Override
    protected void doImpactSound(SoundEvent sound) {
        level().playSound(null, getX(), getY(), getZ(), sound, SoundSource.NEUTRAL, 2, 1.2f + Utils.random.nextFloat() * .2f);

    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        var target = entityHitResult.getEntity();
        DamageSources.applyDamage(target, getDamage(), MASpellRegistry.INFERNAL_BOLT.get().getDamageSource(this, getOwner()));
        int time = 40 + (20 * effectAmplifier);
        MobEffectInstance infernalFlameEffect = new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_flame")), time, effectAmplifier);
        ((LivingEntity) target).addEffect(infernalFlameEffect);
        discard();
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level(), (ParticleOptions)ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "anim_fire")), x, y, z, 5, .1, .1, .1, .25, true);
    }

    @Override
    public void trailParticles() {

        for (int i = 0; i < 1; i++) {
            float yHeading = -((float) (Mth.atan2(getDeltaMovement().z, getDeltaMovement().x) * (double) (180F / (float) Math.PI)) + 90.0F);
            float radius = .25f;
            int steps = 6;
            for (int j = 0; j < steps; j++) {
                float offset = (1f / steps) * i;
                double radians = ((tickCount + offset) / 7.5f) * 360 * Mth.DEG_TO_RAD;
                Vec3 swirl = new Vec3(Math.cos(radians) * radius, Math.sin(radians) * radius, 0).yRot(yHeading * Mth.DEG_TO_RAD);
                double x = getX() + swirl.x;
                double y = getY() + swirl.y + getBbHeight() / 2;
                double z = getZ() + swirl.z;
                Vec3 jitter = Utils.getRandomVec3(.05f);
                level().addParticle((ParticleOptions)ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_surge")), x, y, z, jitter.x, jitter.y, jitter.z);
            }
            //level.addParticle(ParticleTypes.SMOKE, getX(), getY(), getZ(), 0, 0, 0);

        }
    }
}
