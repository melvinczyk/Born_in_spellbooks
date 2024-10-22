package net.melvinczyk.borninspellbooks.spells.nature;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.entity.spells.pumpkins.PumpkinBombProjectile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class PumpkinFriendSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "pumpkin_friend");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 2)),
                Component.translatable("ui.born_in_spellbooks.explosion_radius", Utils.stringTruncation(getExplosionRadius(spellLevel, caster), 1)),
                Component.translatable("ui.born_in_spellbooks.friend_damage", Utils.stringTruncation(getFriendDamage(spellLevel, caster), 2)),
                Component.translatable("ui.born_in_spellbooks.friend_health", Utils.stringTruncation(getFriendHealth(spellLevel, caster), 2), 1)
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
            .setMaxLevel(8)
            .setCooldownSeconds(12)
            .build();

    public PumpkinFriendSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 3;
        this.castTime = 0;
        this.baseManaCost = 30;
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        PumpkinBombProjectile maggot = new PumpkinBombProjectile(level, entity);
        maggot.setPos(entity.position().add(0, entity.getEyeHeight() , 0));
        maggot.shoot(entity.getLookAngle());
        maggot.setDamage(getDamage(spellLevel, entity));
        maggot.setNoGravity(false);
        level.addFreshEntity(maggot);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return baseSpellPower * getEntityPowerMultiplier(caster);
    }

    public float getExplosionRadius(int spellLevel, LivingEntity caster) {
        return 1 + spellLevel;
    }

    private float getFriendHealth(int spellLevel, LivingEntity caster)
    {
        return 12 + spellLevel;
    }

    private float getFriendDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster) * 0.1F;
    }
}