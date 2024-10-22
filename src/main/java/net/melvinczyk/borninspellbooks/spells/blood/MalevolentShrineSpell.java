package net.melvinczyk.borninspellbooks.spells.blood;


import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.entity.spells.malevolent_shrine.Domain;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class MalevolentShrineSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "malevolent_shrine");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster)
    {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getSpellPower(spellLevel, caster) * 0.1F + 2, 1)),
                Component.translatable("ui.born_in_spellbooks.radius", Utils.stringTruncation(spellLevel + 4, 1)),
                Component.translatable("ui.born_in_spellbooks.domain_duration", Utils.stringTruncation(30, 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(360)
            .build();

    public MalevolentShrineSpell()
    {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 4;
        this.castTime = 100;
        this.baseManaCost = 700;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.RAISE_DEAD_START.get());
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound()
    {
        return Optional.of(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1:persecutor_scream")));
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        {
            Domain domain = new Domain(world, entity, getDamage(spellLevel, entity));
            domain.setPos(entity.position().subtract(0, spellLevel + 3, 0));
            domain.setRadius(spellLevel + 4);
            world.addFreshEntity(domain);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public float getDamage(int spellLevel, LivingEntity caster)
    {
        return getSpellPower(spellLevel, caster) * 0.1F + 2;
    }

    public int getDuration(int spellLevel, LivingEntity caster)
    {
        return (int)getSpellPower(spellLevel, caster);
    }
}
