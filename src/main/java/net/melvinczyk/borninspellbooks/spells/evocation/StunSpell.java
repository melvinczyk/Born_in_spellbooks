package net.melvinczyk.borninspellbooks.spells.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.entity.spells.stun.StunField;
import net.melvinczyk.borninspellbooks.misc.MATickHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class StunSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "stun");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 2)),
                Component.translatable("ui.irons_spellbooks.effect_length", Utils.stringTruncation(getDuration(spellLevel, caster) * 0.1f, 0))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(25)
            .build();

    public StunSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 4;
        this.castTime = 17;
        this.baseManaCost = 50;
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
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1", "missionary_stun")));
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!world.isClientSide) {
            Runnable addStunFieldTask = () -> {
                StunField stunField = new StunField(world);
                stunField.setOwner(entity);
                stunField.setDuration(10);
                stunField.setEffectDuration(getDuration(spellLevel, entity));
                stunField.setDamage(getDamage(spellLevel, entity));
                stunField.setCircular();
                stunField.setRadius(5);
                stunField.moveTo(new Vec3(entity.getX(), entity.getY(), entity.getZ()));
                world.addFreshEntity(stunField);
            };
            MATickHandler.delay(20, addStunFieldTask);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }


    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_INSTANT_CAST;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.STOMP;
    }

    private int getDuration(int spellLevel, LivingEntity caster)
    {
        return spellLevel * 10;
    }
    private float getDamage(int spellLevel, LivingEntity caster)
    {
        return getSpellPower(spellLevel, caster) * 0.15f;
    }
}
