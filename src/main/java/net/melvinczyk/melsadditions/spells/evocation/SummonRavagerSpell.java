package net.melvinczyk.melsadditions.spells.evocation;


import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import net.melvinczyk.melsadditions.MelsAdditions;
import net.melvinczyk.melsadditions.entity.mobs.SummonedBoneSerpent;
import net.melvinczyk.melsadditions.entity.mobs.SummonedRavager;
import net.melvinczyk.melsadditions.registry.MAMobEffectRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class SummonRavagerSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(MelsAdditions.MODID, "summon_ravager");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster)
    {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", getRavagerHealth(spellLevel, caster)),
                Component.translatable("ui.irons_spellbooks.damage", getRavagerDamage(spellLevel, caster))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(360)
            .build();

    public SummonRavagerSpell() {
        this.manaCostPerLevel = 25;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
        this.baseManaCost = 300;
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

        SummonedRavager ravager = new SummonedRavager(world, entity);
        ravager.setPos(entity.position());

        world.addFreshEntity(ravager);

        ravager.addEffect(new MobEffectInstance(MAMobEffectRegistry.RAVAGER_TIMER.get(), summonTime, 0, false, false, false));
        int effectAmplifier = 0;
        if (entity.hasEffect(MAMobEffectRegistry.RAVAGER_TIMER.get()))
            effectAmplifier += entity.getEffect(MAMobEffectRegistry.RAVAGER_TIMER.get()).getAmplifier() + 1;
        entity.addEffect(new MobEffectInstance(MAMobEffectRegistry.RAVAGER_TIMER.get(), summonTime, effectAmplifier, false, false, true));

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getRavagerHealth(int spellLevel, LivingEntity caster)
    {
        return 20 + spellLevel * 4;
    }

    private float getRavagerDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }
}
