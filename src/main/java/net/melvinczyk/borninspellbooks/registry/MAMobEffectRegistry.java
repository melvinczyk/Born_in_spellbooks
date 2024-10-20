package net.melvinczyk.borninspellbooks.registry;


import io.redspace.ironsspellbooks.effect.*;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.effect.CursedMarkEffect;
import net.melvinczyk.borninspellbooks.effect.DeathWishEffect;
import net.melvinczyk.borninspellbooks.effect.PhantomSplitEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class MAMobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, BornInSpellbooks.MODID);

    public static void register(IEventBus eventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }

    public static final RegistryObject<SummonTimer> BONE_SERPENT_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("bone_serpent_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final RegistryObject<SummonTimer> RAVAGER_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("ravager_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final RegistryObject<SummonTimer> DREAD_HOUND_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("dread_hound_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final RegistryObject<SummonTimer> SKELETON_THRASHER_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("skeleton_thrasher_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));
    public static final RegistryObject<SummonTimer> ZOMBIE_BRUISER_TIMER = MOB_EFFECT_DEFERRED_REGISTER.register("zombie_bruiser_timer", () -> new SummonTimer(MobEffectCategory.BENEFICIAL, 0xbea925));


    public static final RegistryObject<MobEffect> CURSED_MARK = MOB_EFFECT_DEFERRED_REGISTER.register("cursed_mark", () -> new CursedMarkEffect(MobEffectCategory.HARMFUL, 0xed0707));
    public static final RegistryObject<MobEffect> DEATH_WISH = MOB_EFFECT_DEFERRED_REGISTER.register("death_wish", () -> new DeathWishEffect(MobEffectCategory.HARMFUL, 0xed0707));
    public static final RegistryObject<MobEffect> PHANTOM_SPLIT = MOB_EFFECT_DEFERRED_REGISTER.register("phantom_split", () -> new PhantomSplitEffect(MobEffectCategory.BENEFICIAL, 0xed0707));
}
