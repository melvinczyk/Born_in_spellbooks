package net.melvinczyk.melsadditions.registry;

import net.melvinczyk.melsadditions.MelsAdditions;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.melvinczyk.melsadditions.spells.blood.SummonBoneSerpentSpell;
import net.melvinczyk.melsadditions.spells.evocation.SummonRavagerSpell;
import net.melvinczyk.melsadditions.spells.nature.SummonDreadHoundSpell;
import net.melvinczyk.melsadditions.spells.water.LaunchTridentSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SpellRegistries {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, MelsAdditions.MODID);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell)
    {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    // Blood
    public static final RegistryObject<AbstractSpell> SUMMON_BONE_SERPENT = registerSpell(new SummonBoneSerpentSpell());

    // Evocation
    public static final RegistryObject<AbstractSpell> SUMMON_RAVAGER = registerSpell(new SummonRavagerSpell());

    // Nature
    public static final RegistryObject<AbstractSpell> SUMMON_DREAD_HOUND = registerSpell(new SummonDreadHoundSpell());

    // Water
    public static final RegistryObject<AbstractSpell> LAUNCH_TRIDENT = registerSpell(new LaunchTridentSpell());
    // TODO: Water Spout spell - Targets an entity and launches them in the air with a waver spout
    // TODO: Wave Push Back spell - Summon a wave in front of you that pushes entities away
    // TODO: Erode spell - Water beam that hits target and destroys stone
    // TODO: Summon Coralsus - Summons a Cataclysm coralsus
    
}
