package net.melvinczyk.borninspellbooks.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.registry.MAMobEffectRegistry;
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
public class PhantomSplitSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "phantom_split");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDuration(spellLevel, caster) * 0.05f, 2)),
                Component.translatable("ui.born_in_spellbooks.explosion_radius", Utils.stringTruncation(spellLevel + 2, 1)),
                Component.translatable("ui.irons_spellbooks.effect_length", Utils.stringTruncation((getDuration(spellLevel, caster)), 0))
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
        return Optional.of(SoundEvents.ENDERMAN_TELEPORT);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MAMobEffectRegistry.PHANTOM_SPLIT.get(), getDuration(spellLevel, entity), spellLevel, false, false, true));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getDuration(int spellLevel, LivingEntity sourceEntity) {
        return (int) (getSpellPower(spellLevel, sourceEntity) * 20);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return AnimationHolder.none();
    }
}
