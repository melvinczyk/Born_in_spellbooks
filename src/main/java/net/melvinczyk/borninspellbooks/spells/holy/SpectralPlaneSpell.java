package net.melvinczyk.borninspellbooks.spells.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.entity.spells.spirit_copy.SpiritCopyHumanoid;
import net.melvinczyk.borninspellbooks.registry.MAMobEffectRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class SpectralPlaneSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "spiritual_experience");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks((getSpellPower(spellLevel, caster) * 20) - 300, 1)));
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.EPIC)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(6)
            .setCooldownSeconds(60)
            .build();

    public SpectralPlaneSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 4;
        this.castTime = 0;
        this.baseManaCost = 150;
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
        return Optional.ofNullable(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1", "charm_of_protection_use")));

    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        SpiritCopyHumanoid copy = new SpiritCopyHumanoid(world, entity, (Player) entity, getDuration(spellLevel, entity));
        copy.setPos(new Vec3(entity.getX(), entity.getY(), entity.getZ()));
        world.addFreshEntity(copy);
        MagicManager.spawnParticles(world, ParticleTypes.END_ROD, entity.getX(), entity.getY() + entity.getBbHeight() * .5f, entity.getZ(), 50, entity.getBbWidth() * .5f, entity.getBbHeight() * .5f, entity.getBbWidth() * .5f, .03, false);
        entity.addEffect(new MobEffectInstance(MAMobEffectRegistry.SPIRIT_EFFECT.get(), getDuration(spellLevel, entity), 0, false, false, true));
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.TRUE_INVISIBILITY.get(), getDuration(spellLevel, entity), 0, false, false, false));
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private int getDuration(int spellLevel, LivingEntity source) {
        return (int) (getSpellPower(spellLevel, source) * 20) - 300;
    }
}
