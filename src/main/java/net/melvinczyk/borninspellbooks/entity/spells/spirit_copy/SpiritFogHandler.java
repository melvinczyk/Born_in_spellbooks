package net.melvinczyk.borninspellbooks.entity.spells.spirit_copy;

import net.melvinczyk.borninspellbooks.effect.SpiritEffect;
import net.melvinczyk.borninspellbooks.registry.MAMobEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class SpiritFogHandler {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.hasEffect(MAMobEffectRegistry.SPIRIT_EFFECT.get())) {
            float[] fogColor = SpiritEffect.getFogColor();
            event.setRed(fogColor[0]);
            event.setGreen(fogColor[1]);
            event.setBlue(fogColor[2]);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onFogDensity(ViewportEvent.RenderFog event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.hasEffect(MAMobEffectRegistry.SPIRIT_EFFECT.get())) {
            event.setCanceled(true);
            event.setNearPlaneDistance(SpiritEffect.getFogStart());
            event.setFarPlaneDistance(SpiritEffect.getFogEnd());
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onSkyColor(ViewportEvent.ComputeFogColor event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.hasEffect(MAMobEffectRegistry.SPIRIT_EFFECT.get())) {
            float[] skyColor = SpiritEffect.getSkyColor();
            event.setRed(skyColor[0]);
            event.setGreen(skyColor[1]);
            event.setBlue(skyColor[2]);
        }
    }
}
