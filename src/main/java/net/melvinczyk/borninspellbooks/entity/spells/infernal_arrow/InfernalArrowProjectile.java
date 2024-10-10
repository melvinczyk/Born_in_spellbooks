package net.melvinczyk.borninspellbooks.entity.spells.infernal_arrow;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowProjectile;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

public class InfernalArrowProjectile extends MagicArrowProjectile {
    private final List<Entity> victims = new ArrayList<>();
    private int hitsPerTick;
    @Override
    public void trailParticles() {
        Vec3 vec3 = this.position().subtract(getDeltaMovement());
        level().addParticle((ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_surge")), vec3.x, vec3.y, vec3.z, 0, 0, 0);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(level(), (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_surge")), x, y, z, 15, .1, .1, .1, .5, false);
    }

    public InfernalArrowProjectile(EntityType<? extends MagicArrowProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public InfernalArrowProjectile(Level levelIn, LivingEntity shooter) {
        this(MAEntityRegistry.INFERNAL_ARROW_PROJECTILE.get(), levelIn);
        setOwner(shooter);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        if (!victims.contains(entity)) {
            DamageSources.applyDamage(entity, damage, MASpellRegistry.INFERNAL_ARROW.get().getDamageSource(this, getOwner()));
            MobEffectInstance infernalFlameEffect = new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("born_in_chaos_v1", "infernal_flame")), 80, 1);
            ((LivingEntity) entity).addEffect(infernalFlameEffect);
            victims.add(entity);
        }
        if (hitsPerTick++ < 5) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                onHit(hitresult);
            }
        }
    }
}
