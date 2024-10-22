//package net.melvinczyk.borninspellbooks.spells.nature;
//
//import io.redspace.ironsspellbooks.api.config.DefaultConfig;
//import io.redspace.ironsspellbooks.api.magic.MagicData;
//import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
//import io.redspace.ironsspellbooks.api.spells.*;
//import io.redspace.ironsspellbooks.api.util.Utils;
//import net.melvinczyk.borninspellbooks.BornInSpellbooks;
//import net.melvinczyk.borninspellbooks.entity.mobs.CorpseFlyBarrier;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.MobSpawnType;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.Vec3;
//
//import java.util.List;
//
//@AutoSpellConfig
//public class FlyBarrierSpell extends AbstractSpell {
//    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "fly_barrier");
//
//    @Override
//    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster)
//    {
//        return List.of(
//                Component.translatable("ui.irons_spellbooks.summon_count", getFlyCount(spellLevel))
//        );
//    }
//
//    private final DefaultConfig defaultConfig = new DefaultConfig()
//            .setMinRarity(SpellRarity.EPIC)
//            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
//            .setMaxLevel(10)
//            .setCooldownSeconds(360)
//            .build();
//
//    public FlyBarrierSpell() {
//        this.manaCostPerLevel = 10;
//        this.baseSpellPower = 1;
//        this.spellPowerPerLevel = 1;
//        this.castTime = 50;
//        this.baseManaCost = 150;
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
//    public CastType getCastType() {
//        return CastType.LONG;
//    }
//
//    @Override
//    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
//                CorpseFlyBarrier thrasher = new CorpseFlyBarrier(world, entity);
//                thrasher.setPos(entity.position());
//
//                world.addFreshEntity(thrasher);
//                super.onCast(world, spellLevel, entity, castSource, playerMagicData);
//    }
//
//    private int getFlyCount(int spellLevel)
//    {
//        return (spellLevel / 3);
//    }
//}
