package net.melvinczyk.borninspellbooks.events;


import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.entity.mobs.*;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BornInSpellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event)
    {
        event.put(MAEntityRegistry.SUMMONED_BONE_SERPENT.get(), SummonedBoneSerpent.setAttributes());
        event.put(MAEntityRegistry.SUMMONED_RAVAGER.get(), SummonedRavager.setAttributes());
        event.put(MAEntityRegistry.SUMMONED_DREAD_HOUND.get(), SummonedDreadHound.setAttributes());
        event.put(MAEntityRegistry.SUMMONED_SKELETON_THRASHER.get(), SummonedSkeletonThrasher.setAttributes());
        event.put(MAEntityRegistry.SCARLET_PERSECUTOR.get(), ScarletPersecutor.setAttributes());
        event.put(MAEntityRegistry.CORPSEFLY_PATHFINDER.get(), ScarletPersecutor.setAttributes());
    }
}
