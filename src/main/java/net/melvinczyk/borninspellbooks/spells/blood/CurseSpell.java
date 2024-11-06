package net.melvinczyk.borninspellbooks.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.spells.TargetedTargetAreaCastData;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.util.MAMobEffectInstance;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;
import net.melvinczyk.borninspellbooks.registry.MAMobEffectRegistry;


import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class CurseSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "curse");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster)
    {
        int summons = (int)Math.ceil((double)spellLevel / 2);
        return List.of(
                Component.translatable("ui.irons_spellbooks.summon_count", summons),
                Component.translatable("ui.irons_spellbooks.hp",Utils.stringTruncation(getScarletPersecutorHealth(spellLevel, caster), 2)),
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getScarletPersecutorDamage(spellLevel, caster), 2))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(60)
            .build();

    public CurseSpell()
    {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 4;
        this.castTime = 30;
        this.baseManaCost = 150;
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
        return Optional.of(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1", "charm_of_rage_use")));
    }


    @Override
    public Optional<SoundEvent> getCastFinishSound()
    {
        return Optional.of(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1", "fallen_curse_mark")));
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if (Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, .35f)) {
            float radius = 1f;
            var target = ((TargetEntityCastData) playerMagicData.getAdditionalCastData()).getTarget((ServerLevel) level);
            var area = TargetedAreaEntity.createTargetAreaEntity(level, target.position(), radius, MobEffects.BAD_OMEN.getColor(), target);
            playerMagicData.setAdditionalCastData(new TargetedTargetAreaCastData(target, area));
            return true;
        }
        return false;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof TargetedTargetAreaCastData targetData) {
            var targetEntity = targetData.getTarget((ServerLevel) world);
            MagicManager.spawnParticles(world, (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "srirst_part")), targetEntity.getX(), targetEntity.getY() + targetEntity.getBbHeight() * .5f, targetEntity.getZ(), 50, targetEntity.getBbWidth() * .5f, targetEntity.getBbHeight() * .5f, targetEntity.getBbWidth() * .5f, .03, false);
            targetEntity.addEffect(new MAMobEffectInstance(MAMobEffectRegistry.CURSED_MARK.get(), getDuration(spellLevel, entity), getAmplifier(spellLevel, entity), entity, false, false, true));
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public int getAmplifier(int spellLevel, LivingEntity caster) {
        return spellLevel - 1;
    }

    public int getDuration(int spellLevel, LivingEntity caster) {
        return (int) (getSpellPower(spellLevel, caster) * 2);
    }

    @Override
    public Vector3f getTargetingColor() {
        return Utils.deconstructRGB(MobEffects.MOVEMENT_SLOWDOWN.getColor());
    }

    private float getScarletPersecutorHealth(int spellLevel, LivingEntity caster)
    {
        return 10 + (getSpellPower(spellLevel, caster) * 0.1F);
    }

    private float getScarletPersecutorDamage(int spellLevel, LivingEntity caster)
    {
        return 4 + (getSpellPower(spellLevel, caster) * 0.1F);
    }
}
