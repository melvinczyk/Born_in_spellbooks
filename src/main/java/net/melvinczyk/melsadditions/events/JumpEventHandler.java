package net.melvinczyk.melsadditions.events;

import net.melvinczyk.melsadditions.MelsAdditions;
import net.melvinczyk.melsadditions.entity.mobs.SummonedBoneSerpent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MelsAdditions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class JumpEventHandler {
    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event)
    {
        if (event.getEntity() instanceof  Player player)
        {
            if (player.getVehicle() instanceof SummonedBoneSerpent summonedBoneSerpent)
            {

                summonedBoneSerpent.setDeltaMovement(summonedBoneSerpent.getDeltaMovement().add(0, 0.5, 0));
                summonedBoneSerpent.setJumping(true);
            }
        }
    }
}
