package net.melvinczyk.melsadditions.events;

import net.melvinczyk.melsadditions.MelsAdditions;
import net.melvinczyk.melsadditions.entity.mobs.SummonedBoneSerpent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MelsAdditions.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEventHandler {

    private static boolean jumpKeyPressed = false;
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player != null) {
            Entity ridingEntity = player.getVehicle();
            if (ridingEntity instanceof SummonedBoneSerpent summonedBoneSerpent) {
                if (minecraft.options.keyJump.isDown()) {
                    if (!jumpKeyPressed && ridingEntity.onGround()) {
                        summonedBoneSerpent.setDeltaMovement(summonedBoneSerpent.getDeltaMovement().add(0, 1.5, 0));
                        summonedBoneSerpent.hasImpulse = true;
                        player.setDeltaMovement(player.getDeltaMovement().add(0, 1.5, 0));
                        player.hasImpulse = true;

                        jumpKeyPressed = true;
                    }
                } else {
                    jumpKeyPressed = false;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            Entity ridingEntity = player.getVehicle();
            if (ridingEntity instanceof SummonedBoneSerpent) {
                event.setCanceled(true);
            }
        }
    }
}
