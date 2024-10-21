package net.melvinczyk.borninspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import io.redspace.ironsspellbooks.effect.CustomDescriptionMobEffect;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import net.melvinczyk.borninspellbooks.entity.spells.phantom_copy.PhantomCopyHumanoid;
import net.melvinczyk.borninspellbooks.misc.MASynchedSpellData;
import net.melvinczyk.borninspellbooks.registry.MAMobEffectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PhantomSplitEffect extends CustomDescriptionMobEffect {
    public PhantomSplitEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    protected static int amplifier = 0;
    protected static int duration = 0;

    @Override
    public Component getDescriptionLine(MobEffectInstance instance) {
        int amp = instance.getAmplifier() + 2;
        return Component.translatable("tooltip.born_in_spellbooks.phantom_split_description", amp).withStyle(ChatFormatting.WHITE);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().removeEffects(MASynchedSpellData.PHANTOM_SPLIT);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        MobEffectInstance effectInstance = pLivingEntity.getEffect(this);
        if (effectInstance != null)
        {
            amplifier = effectInstance.getAmplifier() + 2;
            duration = effectInstance.getDuration();
        }
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().addEffects(MASynchedSpellData.PHANTOM_SPLIT);
    }

    public static boolean doEffect(LivingEntity livingEntity, DamageSource damageSource) {
        if (livingEntity.level().isClientSide
                || damageSource.is(DamageTypeTags.IS_FALL)
                || damageSource.is(DamageTypeTags.IS_FIRE)
                || damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                || damageSource.is(DamageTypeTagGenerator.BYPASS_EVASION)) {
            return false;
        }
        else {

            var data = MagicData.getPlayerMagicData(livingEntity).getSyncedData();
            data.subtractEvasionHit();
            if (data.getEvasionHitsRemaining() < 0) {
                livingEntity.removeEffect(MAMobEffectRegistry.PHANTOM_SPLIT.get());
            }

            var level = livingEntity.level();
            Vec3 currentPosition = livingEntity.position();
            Vec3 dest = getRandomTeleportLocation(currentPosition, level);

            var teleportData = MagicData.getPlayerMagicData(livingEntity).getAdditionalCastData();
            if (teleportData instanceof TeleportSpell.TeleportData) {
                dest = ((TeleportSpell.TeleportData) teleportData).getTeleportTargetPosition();
            }

            Entity directDamager = damageSource.getDirectEntity();
            Entity damager = damageSource.getEntity();
            if (directDamager instanceof Projectile projectile)
            {
                Entity projectileOwner = projectile.getOwner();
                if (projectileOwner != null)
                {
                 damager = projectileOwner;
                }
            }

            PhantomCopyHumanoid copy = new PhantomCopyHumanoid(level, livingEntity, (Player) livingEntity, damager, amplifier, duration);
            level.addFreshEntity(copy);

            if (dest == null) {
                dest = getRandomTeleportLocation(currentPosition, level);
            }

            if (livingEntity.isPassenger()) {
                livingEntity.stopRiding();
            }

            livingEntity.teleportTo(dest.x, dest.y, dest.z);
            livingEntity.resetFallDistance();

            level.playSound(null, dest.x, dest.y, dest.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
            livingEntity.playSound(SoundEvents.ENDERMAN_TELEPORT, 2.0F, 1.0F);
            particleCloud(livingEntity);
            livingEntity.removeEffect(MAMobEffectRegistry.PHANTOM_SPLIT.get());

            return true;
        }
    }

    private static Vec3 getRandomTeleportLocation(Vec3 currentPosition, Level level) {
        double randomX = currentPosition.x + (level.random.nextDouble() * 6 - 3);
        double randomY = Math.max(level.getMinBuildHeight(), currentPosition.y);
        double randomZ = currentPosition.z + (level.random.nextDouble() * 6 - 3);

        return new Vec3(randomX, randomY, randomZ);
    }


    private static void particleCloud(LivingEntity entity) {
        Vec3 pos = entity.position().add(0, entity.getBbHeight() / 2, 0);
        MagicManager.spawnParticles(entity.level(), ParticleTypes.PORTAL, pos.x, pos.y, pos.z, 70, entity.getBbWidth() / 4, entity.getBbHeight() / 5, entity.getBbWidth() / 4, .035, false);
    }
}
