package net.melvinczyk.borninspellbooks.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.entity.mobs.SummonedZombieBruiser;
import net.melvinczyk.borninspellbooks.registry.MAMobEffectRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class SummonZombieBruiserSpell extends AbstractSpell
{
    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "summon_zombie_bruiser");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster)
    {
        return List.of(
                Component.translatable("ui.irons_spellbooks.hp", Utils.stringTruncation(getBruiserHealth(spellLevel, caster), 2)),
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getBruiserDamage(spellLevel, caster), 2))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(240)
            .build();

    public SummonZombieBruiserSpell() {
        this.manaCostPerLevel = 20;
        this.baseSpellPower = 9;
        this.spellPowerPerLevel = 1;
        this.castTime = 50;
        this.baseManaCost = 175;
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
        return Optional.of(SoundRegistry.RAISE_DEAD_START.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound()
    {
        return Optional.of(SoundRegistry.RAISE_DEAD_FINISH.get());
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }
    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData)
    {
        for (int i = 0; i < 36; i++) {
            double x, z;
            double theta = Math.toRadians((double) 360 / 36) * i;
            x = Math.cos(theta) * 1.5f;
            z = Math.sin(theta) * 1.5f;
            MagicManager.spawnParticles(world, (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "ritual")), entity.position().x + x, entity.position().y, entity.position().z + z, 1, 0, 0, 0, 0.1, false);
        }
        int summonTime = 20 * 60 * 10;

        SummonedZombieBruiser thrasher = new SummonedZombieBruiser(world, entity);
        thrasher.setPos(entity.position());


        thrasher.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(getBruiserDamage(spellLevel, entity));
        thrasher.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(getBruiserHealth(spellLevel, entity));
        thrasher.setHealth(thrasher.getMaxHealth());
        world.addFreshEntity(thrasher);

        thrasher.addEffect(new MobEffectInstance(MAMobEffectRegistry.ZOMBIE_BRUISER_TIMER.get(), summonTime, 0, false, false, false));
        int effectAmplifier = 0;
        if (entity.hasEffect(MAMobEffectRegistry.ZOMBIE_BRUISER_TIMER.get()))
            effectAmplifier += entity.getEffect(MAMobEffectRegistry.ZOMBIE_BRUISER_TIMER.get()).getAmplifier() + 1;
        entity.addEffect(new MobEffectInstance(MAMobEffectRegistry.ZOMBIE_BRUISER_TIMER.get(), summonTime, effectAmplifier, false, false, true));

        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private float getBruiserHealth(int spellLevel, LivingEntity caster)
    {
        return 40 + spellLevel * 2;
    }

    private float getBruiserDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster) * 0.35F;
    }
}
