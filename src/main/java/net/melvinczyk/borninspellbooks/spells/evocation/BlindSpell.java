package net.melvinczyk.borninspellbooks.spells.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.mcreator.borninchaosv.entity.StaffofBlindnessProjectileEntity;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.entity.spells.blind.BlindnessProjectile;
import net.melvinczyk.borninspellbooks.registry.MASchoolRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class BlindSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "blind");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster)
    {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(Math.floor((getDamage(spellLevel, caster) * 1.5F) + (getDamage(spellLevel, caster) / 6)), 2)),
                Component.translatable("ui.born_in_spellbooks.knockback", Utils.stringTruncation(getKnockback(spellLevel, caster), 2))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(MASchoolRegistry.EVOCATION_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(1)
            .build();

    public BlindSpell()
    {
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 12;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 10;
    }

    @Override
    public CastType getCastType()
    {
        return  CastType.INSTANT;
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
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        StaffofBlindnessProjectileEntity blindnessProjectile = StaffofBlindnessProjectileEntity.shoot(world, entity, world.getRandom(), 0.75F, getDamage(spellLevel, entity) , getKnockback(spellLevel, entity));
        blindnessProjectile.setPos(entity.position().add(0, entity.getEyeHeight() , 0));
        blindnessProjectile.setNoGravity(false);
        world.addFreshEntity(blindnessProjectile);
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster) * .3F;
    }

    private int getKnockback(int spellLevel, LivingEntity caster)
    {
        return (int)(getSpellPower(spellLevel, caster) * .2F);
    }
}
