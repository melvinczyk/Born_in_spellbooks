package net.melvinczyk.melsadditions.events;


import net.melvinczyk.melsadditions.MelsAdditions;
import net.melvinczyk.melsadditions.entity.mobs.SummonedBoneSerpent;
import net.melvinczyk.melsadditions.entity.mobs.SummonedDreadHound;
import net.melvinczyk.melsadditions.entity.mobs.SummonedRavager;
import net.melvinczyk.melsadditions.registry.MAEntityRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MelsAdditions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event)
    {
        event.put(MAEntityRegistry.SUMMONED_BONE_SERPENT.get(), SummonedBoneSerpent.setAttributes());
        event.put(MAEntityRegistry.SUMMONED_RAVAGER.get(), SummonedRavager.setAttributes());
        event.put(MAEntityRegistry.SUMMONED_DREAD_HOUND.get(), SummonedDreadHound.setAttributes());
    }
}
