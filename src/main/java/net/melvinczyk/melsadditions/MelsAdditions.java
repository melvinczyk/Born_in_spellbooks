package net.melvinczyk.melsadditions;

import com.mojang.logging.LogUtils;
import net.melvinczyk.melsadditions.entity.mobs.render.SummonedBoneSerpentRenderer;
import net.melvinczyk.melsadditions.events.EntityEventHandler;
import net.melvinczyk.melsadditions.registry.MAAttributeRegistry;
import net.melvinczyk.melsadditions.registry.MAEntityRegistry;
import net.melvinczyk.melsadditions.registry.MAMobEffectRegistry;
import net.melvinczyk.melsadditions.registry.SpellRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(MelsAdditions.MODID)
public class MelsAdditions
{
    public static final String MODID = "mels_additions";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MelsAdditions(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        SpellRegistries.register(modEventBus);
        MAEntityRegistry.register(modEventBus);
        MAMobEffectRegistry.register(modEventBus);
        MAAttributeRegistry.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    public static ResourceLocation id(@NotNull String path)
    {
        return new ResourceLocation(MelsAdditions.MODID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(MAEntityRegistry.SUMMONED_BONE_SERPENT.get(), SummonedBoneSerpentRenderer::new);
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
