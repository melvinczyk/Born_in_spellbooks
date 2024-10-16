//package net.melvinczyk.borninspellbooks.spells.blood;
//
//import io.redspace.ironsspellbooks.api.config.DefaultConfig;
//import io.redspace.ironsspellbooks.api.magic.MagicData;
//import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
//import io.redspace.ironsspellbooks.api.spells.*;
//import net.melvinczyk.borninspellbooks.BornInSpellbooks;
//import net.melvinczyk.borninspellbooks.entity.mobs.SummonedBoneSerpent;
//import net.melvinczyk.borninspellbooks.registry.MAMobEffectRegistry;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.level.Level;
//
//import java.util.List;
//import java.util.Optional;
//
//@AutoSpellConfig
//public class SummonBoneSerpentSpell extends AbstractSpell
//{
//    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "summon_bone_serpent");
//
//    @Override
//    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster)
//    {
//        return List.of(
//                Component.translatable("ui.irons_spellbooks.hp", getBoneSerpentHealth(spellLevel, caster)),
//                Component.translatable("ui.irons_spellbooks.damage", getBoneSerpentDamage(spellLevel, caster))
//        );
//    }
//
//    private final DefaultConfig defaultConfig = new DefaultConfig()
//            .setMinRarity(SpellRarity.EPIC)
//            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
//            .setMaxLevel(10)
//            .setCooldownSeconds(180)
//            .build();
//
//    public SummonBoneSerpentSpell() {
//        this.manaCostPerLevel = 20;
//        this.baseSpellPower = 5;
//        this.spellPowerPerLevel = 1;
//        this.castTime = 50;
//        this.baseManaCost = 200;
//    }
//
//    @Override
//    public CastType getCastType()
//    {
//        return CastType.LONG;
//    }
//
//
//    @Override
//    public DefaultConfig getDefaultConfig() {
//        return defaultConfig;
//    }
//
//    @Override
//    public ResourceLocation getSpellResource() {
//        return spellId;
//    }
//
//    @Override
//    public Optional<SoundEvent> getCastStartSound() {
//        return Optional.of(SoundEvents.EVOKER_PREPARE_SUMMON);
//    }
//
//    @Override
//    public Optional<SoundEvent> getCastFinishSound()
//    {
//        return Optional.of(SoundEvents.EVOKER_CAST_SPELL);
//    }
//
//    @Override
//    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData)
//    {
//         int summonTime = 20 * 60 * 10;
//
//         SummonedBoneSerpent boneSerpent = new SummonedBoneSerpent(world, entity);
//         boneSerpent.setPos(entity.position());
//
//         world.addFreshEntity(boneSerpent);
//
//         boneSerpent.addEffect(new MobEffectInstance(MAMobEffectRegistry.BONE_SERPENT_TIMER.get(), summonTime, 0, false, false, false));
//         int effectAmplifier = 0;
//         if (entity.hasEffect(MAMobEffectRegistry.BONE_SERPENT_TIMER.get()))
//             effectAmplifier += entity.getEffect(MAMobEffectRegistry.BONE_SERPENT_TIMER.get()).getAmplifier() + 1;
//         entity.addEffect(new MobEffectInstance(MAMobEffectRegistry.BONE_SERPENT_TIMER.get(), summonTime, effectAmplifier, false, false, true));
//
//         super.onCast(world, spellLevel, entity, castSource, playerMagicData);
//    }
//
//    private float getBoneSerpentHealth(int spellLevel, LivingEntity caster)
//    {
//        return 20 + spellLevel * 4;
//    }
//
//    private float getBoneSerpentDamage(int spellLevel, LivingEntity caster) {
//        return getSpellPower(spellLevel, caster);
//    }
//}
