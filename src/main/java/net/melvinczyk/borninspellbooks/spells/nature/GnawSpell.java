//package net.melvinczyk.borninspellbooks.spells.nature;
//
//
//import io.redspace.ironsspellbooks.api.config.DefaultConfig;
//import io.redspace.ironsspellbooks.api.magic.MagicData;
//import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
//import io.redspace.ironsspellbooks.api.spells.*;
//import io.redspace.ironsspellbooks.api.util.AnimationHolder;
//import io.redspace.ironsspellbooks.api.util.Utils;
//import io.redspace.ironsspellbooks.damage.SpellDamageSource;
//import io.redspace.ironsspellbooks.registries.SoundRegistry;
//import net.melvinczyk.borninspellbooks.BornInSpellbooks;
//import net.melvinczyk.borninspellbooks.entity.spells.gnaw.GluttonFishGnawEntity;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.MobType;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.enchantment.EnchantmentHelper;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.Vec3;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.List;
//import java.util.Optional;
//
//
//@AutoSpellConfig
//public class GnawSpell extends AbstractSpell {
//    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "gnaw");
//
//    @Override
//    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
//        return List.of(Component.translatable("ui.irons_spellbooks.damage", getDamageText(spellLevel, caster)));
//    }
//
//    private final DefaultConfig defaultConfig = new DefaultConfig()
//            .setMinRarity(SpellRarity.UNCOMMON)
//            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
//            .setMaxLevel(5)
//            .setCooldownSeconds(15)
//            .build();
//
//    public GnawSpell() {
//        this.manaCostPerLevel = 15;
//        this.baseSpellPower = 5;
//        this.spellPowerPerLevel = 2;
//        this.castTime = 10;
//        this.baseManaCost = 30;
//    }
//
//    @Override
//    public Optional<SoundEvent> getCastStartSound() {
//        return Optional.of(SoundRegistry.FLAMING_STRIKE_UPSWING.get());
//    }
//
//    @Override
//    public Optional<SoundEvent> getCastFinishSound() {
//        return Optional.of(SoundRegistry.FLAMING_STRIKE_SWING.get());
//    }
//
//    @Override
//    public CastType getCastType() {
//        return CastType.LONG;
//    }
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
//    public boolean canBeInterrupted(@Nullable Player player) {
//        return false;
//    }
//
//    @Override
//    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
//        return getCastTime(spellLevel);
//    }
//
//    @Override
//    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
//            GluttonFishGnawEntity gnawEntity = new GluttonFishGnawEntity(world, entity);
//
//            Vec3 lookVec = entity.getLookAngle();
//            gnawEntity.setPos(entity.getX() + lookVec.x * 2, entity.getY() + lookVec.y * 5, entity.getZ() + lookVec.z * 2);
//
//            world.addFreshEntity(gnawEntity);
//    }
//
//
//    @Override
//    public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
//        return super.getDamageSource(projectile, attacker).setFireTime(3);
//    }
//
//    private float getDamage(int spellLevel, LivingEntity entity) {
//        return getSpellPower(spellLevel, entity) + Utils.getWeaponDamage(entity, MobType.UNDEFINED) + EnchantmentHelper.getFireAspect(entity);
//    }
//
//
//    private String getDamageText(int spellLevel, LivingEntity entity) {
//        if (entity != null) {
//            float weaponDamage = Utils.getWeaponDamage(entity, MobType.UNDEFINED);
//            String plus = "";
//            if (weaponDamage > 0) {
//                plus = String.format(" (+%s)", Utils.stringTruncation(weaponDamage, 1));
//            }
//            String damage = Utils.stringTruncation(getDamage(spellLevel, entity), 1);
//            return damage + plus;
//        }
//        return "" + getSpellPower(spellLevel, entity);
//    }
//
//    @Override
//    public AnimationHolder getCastStartAnimation() {
//        return SpellAnimations.ONE_HANDED_HORIZONTAL_SWING_ANIMATION;
//    }
//
//    @Override
//    public AnimationHolder getCastFinishAnimation() {
//        return AnimationHolder.pass();
//    }
//}
