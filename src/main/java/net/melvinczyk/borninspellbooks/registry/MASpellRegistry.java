package net.melvinczyk.borninspellbooks.registry;

import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.melvinczyk.borninspellbooks.spells.blood.CurseSpell;
import net.melvinczyk.borninspellbooks.spells.blood.SummonBoneSerpentSpell;
import net.melvinczyk.borninspellbooks.spells.blood.SummonSkeletonThrasherSpell;
import net.melvinczyk.borninspellbooks.spells.evocation.BlindSpell;
import net.melvinczyk.borninspellbooks.spells.evocation.DeathWishSpell;
import net.melvinczyk.borninspellbooks.spells.evocation.NightmareRendSpell;
import net.melvinczyk.borninspellbooks.spells.fire.InfernalArrowSpell;
import net.melvinczyk.borninspellbooks.spells.fire.InfernalBoltSpell;
import net.melvinczyk.borninspellbooks.spells.fire.InfernalBombSpell;
import net.melvinczyk.borninspellbooks.spells.nature.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MASpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, BornInSpellbooks.MODID);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell)
    {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    // Blood
    public static final RegistryObject<AbstractSpell> SUMMON_BONE_SERPENT = registerSpell(new SummonBoneSerpentSpell());
    public static final RegistryObject<AbstractSpell> SUMMON_SKELETON_THRASHER = registerSpell(new SummonSkeletonThrasherSpell());
    public static final RegistryObject<AbstractSpell> CURSE = registerSpell(new CurseSpell());
    // TODO: Summon barrel zombie - Summon a barrel zombie with a built in inventory
    // TODO: Imbue Chaos - Spirit of Chaos goes into self and get th obsession effect
    // TODO: Baby skeleton Summon - Summons two baby skeletons but changes depending on the biomes casted in: baby skeleton/imp/mini spirit guide
    // TODO: Vampiric Sacrifice - Sacrifice all summons and become a vampire like lifestealer. Plan to change eyes and skin layer to be lifestealer


    // Ender
    // TODO: Ender Creeper Split - Split yourself and summons a copy of you that explodes


    // Evocation
    //public static final RegistryObject<AbstractSpell> SUMMON_RAVAGER = registerSpell(new SummonRavagerSpell());
    public static final RegistryObject<AbstractSpell> BLIND = registerSpell(new BlindSpell());
    public static final RegistryObject<AbstractSpell> DEATH_WISH = registerSpell(new DeathWishSpell());
    public static final RegistryObject<AbstractSpell> NIGHTMARE_REND = registerSpell(new NightmareRendSpell());


    // Nature
    public static final RegistryObject<AbstractSpell> SUMMON_DREAD_HOUND = registerSpell(new SummonDreadHoundSpell());
    public static final RegistryObject<AbstractSpell> INFECT = registerSpell(new ShootMaggotSpell());
    public static final RegistryObject<AbstractSpell> INFECT_HOST = registerSpell(new InfectHostSpell());
    public static final RegistryObject<AbstractSpell> FLY_BARRIER = registerSpell(new FlyBarrierSpell());
    public static final RegistryObject<AbstractSpell> SWARM = registerSpell(new SwarmSpell());
    public static final RegistryObject<AbstractSpell> GNAW = registerSpell(new GnawSpell());
    // TODO: Gnaw - Summons a glutton fish in front of you that takes a big bite and disappears
    // TODO: Fly Shield - Adds a circle of flies that work like a barrier


    // Fire
    public static final RegistryObject<AbstractSpell> INFERNAL_BOMB = registerSpell(new InfernalBombSpell());
    public static final RegistryObject<AbstractSpell> INFERNAL_BOLT = registerSpell(new InfernalBoltSpell());
    public static final RegistryObject<AbstractSpell> INFERNAL_ARROW = registerSpell(new InfernalArrowSpell());


    // Electricity
    // TODO: Chaos Bolt - A chaos infused lightning bolt that strikes ground and leaves wave of electricity


    // Water
    //public static final RegistryObject<AbstractSpell> LAUNCH_TRIDENT = registerSpell(new LaunchTridentSpell());
    // TODO: Water Spout spell - Targets an entity and launches them in the air with a waver spout
    // TODO: Wave Push Back spell - Summon a wave in front of you that pushes entities away
    // TODO: Erode spell - Water beam that hits target and destroys stone
    // TODO: Summon Coralsus - Summons a Cataclysm coralsus
}
