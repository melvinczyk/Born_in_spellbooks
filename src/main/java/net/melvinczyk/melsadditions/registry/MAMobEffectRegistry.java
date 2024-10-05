package net.melvinczyk.melsadditions.registry;


import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.effect.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.mcreator.borninchaosv.potion.CursedMarkMobEffect;


public class MAMobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, IronsSpellbooks.MODID);

    public static void register(IEventBus eventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }

    public static final RegistryObject<SummonTimer> BONE_SERPENT_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("bone_serpent_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final RegistryObject<SummonTimer> RAVAGER_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("ravager_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final RegistryObject<SummonTimer> DREAD_HOUND_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("dread_hound_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final RegistryObject<SummonTimer> SKELETON_THRASHER_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("skeleton_thrasher_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final RegistryObject<MobEffect> CURSED_MARK = MOB_EFFECT_DEFERRED_REGISTER.register("cursed_mark", () -> new MagicMobEffect(MobEffectCategory.HARMFUL, 0xFFFFFF));
}
