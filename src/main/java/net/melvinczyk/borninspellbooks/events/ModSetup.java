package net.melvinczyk.borninspellbooks.events;


import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.entity.mobs.*;
import net.melvinczyk.borninspellbooks.entity.spells.pumpkins.PumpkinFriend;
import net.melvinczyk.borninspellbooks.entity.spells.phantom_copy.PhantomCopyHumanoid;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BornInSpellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event)
    {
        //event.put(MAEntityRegistry.SUMMONED_BONE_SERPENT.get(), SummonedBoneSerpent.setAttributes());
        event.put(MAEntityRegistry.SUMMONED_RAVAGER.get(), SummonedRavager.setAttributes());
        event.put(MAEntityRegistry.SUMMONED_DREAD_HOUND.get(), SummonedDreadHound.setAttributes());
        event.put(MAEntityRegistry.SUMMONED_SKELETON_THRASHER.get(), SummonedSkeletonThrasher.setAttributes());
        event.put(MAEntityRegistry.SCARLET_PERSECUTOR.get(), ScarletPersecutor.setAttributes());
        event.put(MAEntityRegistry.CORPSEFLY_PATHFINDER.get(), CorpseFlyPathFinder.prepareAttributes().build());
        event.put(MAEntityRegistry.SPAWNED_MAGGOT.get(), SpawnedMaggot.setAttributes());
        event.put(MAEntityRegistry.SUMMONED_BARREL_ZOMBIE.get(), SummonedBarrelZombie.setAttributes());
        event.put(MAEntityRegistry.ZOMBIE_BRUISER.get(), SummonedZombieBruiser.setAttributes());
        event.put(MAEntityRegistry.CORPSE_FLY_BARRIER.get(), CorpseFlyBarrier.setAttributes());
        event.put(MAEntityRegistry.PHANTOM_COPY.get(), PhantomCopyHumanoid.prepareAttributes().build());
        event.put(MAEntityRegistry.PUMPKIN_FRIEND.get(), PumpkinFriend.setAttributes());

    }
}
