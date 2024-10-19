package net.melvinczyk.borninspellbooks.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoid;
import io.redspace.ironsspellbooks.network.spell.ClientboundFrostStepParticles;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.entity.spells.phantom_copy.PhantomCopyHumanoid;
import net.melvinczyk.borninspellbooks.entity.spells.phantom_copy.PhantomCopyRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;


@AutoSpellConfig
public class PhantomSplitSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "phantom_split");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation(getDistance(spellLevel, caster), 1)),
                Component.translatable("ui.irons_spellbooks.shatter_damage", Utils.stringTruncation(getDamage(spellLevel, caster), 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.ENDER_RESOURCE)
            .setMaxLevel(8)
            .setCooldownSeconds(10)
            .build();

    public PhantomSplitSpell() {
        this.baseSpellPower = 14;
        this.spellPowerPerLevel = 3;
        this.baseManaCost = 15;
        this.manaCostPerLevel = 3;
        this.castTime = 0;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.FROST_STEP.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        var teleportData = (TeleportSpell.TeleportData) playerMagicData.getAdditionalCastData();

        PhantomCopyHumanoid shadow = new PhantomCopyHumanoid(level, entity, (Player) entity);
        shadow.setShatterDamage(getDamage(spellLevel, entity));
        shadow.setDeathTimer(60);
        level.addFreshEntity(shadow);

        Vec3 dest = getRandomTeleportLocation(entity.position(), level);

        if (teleportData != null) {
            var potentialTarget = teleportData.getTeleportTargetPosition();
            dest = potentialTarget;
        }

        if (dest == null) {
            dest = findTeleportLocation(spellLevel, level, entity);
        }

        Messages.sendToPlayersTrackingEntity(new ClientboundFrostStepParticles(entity.position(), dest), entity, true);

        if (entity.isPassenger()) {
            entity.stopRiding();
        }

        entity.teleportTo(dest.x, dest.y, dest.z);
        entity.resetFallDistance();
        level.playSound(null, dest.x, dest.y, dest.z, getCastFinishSound().get(), SoundSource.NEUTRAL, 1f, 1f);

        playerMagicData.resetAdditionalCastData();

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private Vec3 getRandomTeleportLocation(Vec3 currentPosition, Level level) {
        double randomX = currentPosition.x + (level.random.nextDouble() * 6 - 3);
        double randomY = currentPosition.y;
        double randomZ = currentPosition.z + (level.random.nextDouble() * 6 - 3);
        randomY = Math.max(level.getMinBuildHeight(), randomY);

        return new Vec3(randomX, randomY, randomZ);
    }


    private Vec3 findTeleportLocation(int spellLevel, Level level, LivingEntity entity) {
        return TeleportSpell.findTeleportLocation(level, entity, getDistance(spellLevel, entity));
    }

    private float getDistance(int spellLevel, LivingEntity sourceEntity) {
        return (float) (Utils.softCapFormula(getEntityPowerMultiplier(sourceEntity)) * getSpellPower(spellLevel, null)) * .65f;
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, caster) / 3;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return AnimationHolder.none();
    }
}
