package net.melvinczyk.melsadditions.spells.nature;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.melvinczyk.melsadditions.MelsAdditions;
import net.melvinczyk.melsadditions.entity.mobs.SummonedDreadHound;
import net.melvinczyk.melsadditions.registry.MAMobEffectRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;


@AutoSpellConfig
public class SummonDreadHoundSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(MelsAdditions.MODID, "summon_dread_hound");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster)
    {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", getDreadHoundHealth(spellLevel, caster)),
                Component.translatable("ui.irons_spellbooks.damage", getDreadHoundDamage(spellLevel, caster))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(180)
            .build();

    public SummonDreadHoundSpell() {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 1;
        this.castTime = 70;
        this.baseManaCost = 200;
    }

    @Override
    public CastType getCastType()
    {
        return CastType.LONG;
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
        return Optional.of(SoundEvents.EVOKER_PREPARE_SUMMON);
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound()
    {
        return Optional.of(SoundEvents.EVOKER_CAST_SPELL);
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData)
    {
        int summonTime = 20 * 60 * 10;

        float radius = 1.0f + .185f * spellLevel;
        for (int i = 0; i < spellLevel; i++)
        {
            if (i % 2 == 0)
            {
                SummonedDreadHound dreadHound = new SummonedDreadHound(world, entity);
                dreadHound.finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(dreadHound.getOnPos()), MobSpawnType.MOB_SUMMONED, null, null);
                dreadHound.addEffect(new MobEffectInstance(MAMobEffectRegistry.DREAD_HOUND_TIMER.get(), summonTime, 0, false, false, false));
                var yrot = 6.281f / spellLevel * i + entity.getYRot() * Mth.DEG_TO_RAD;
                Vec3 spawn = Utils.moveToRelativeGroundLevel(world, entity.getEyePosition().add(new Vec3(radius * Mth.cos(yrot), 0, radius * Mth.sin(yrot))), 10);
                dreadHound.setPos(spawn.x, spawn.y, spawn.z);
                dreadHound.setYRot(entity.getYRot());
                dreadHound.setOldPosAndRot();
                world.addFreshEntity(dreadHound);
            }
        }
        int effectAmplifier = spellLevel - 1;
        if (entity.hasEffect(MAMobEffectRegistry.DREAD_HOUND_TIMER.get()))
            effectAmplifier += entity.getEffect(MAMobEffectRegistry.DREAD_HOUND_TIMER.get()).getAmplifier() + 1;
        entity.addEffect(new MobEffectInstance(MAMobEffectRegistry.DREAD_HOUND_TIMER.get(), summonTime, effectAmplifier, false, false, true));

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDreadHoundHealth(int spellLevel, LivingEntity caster)
    {
        return 20 + spellLevel * 4;
    }

    private float getDreadHoundDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }
}
