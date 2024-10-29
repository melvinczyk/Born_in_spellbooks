package net.melvinczyk.borninspellbooks;

import com.mojang.logging.LogUtils;
import net.melvinczyk.borninspellbooks.entity.mobs.render.*;
import net.melvinczyk.borninspellbooks.entity.spells.great_glutton.WaterEffectRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.pumpkins.PumpkinBombRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.pumpkins.PumpkinFriendRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.great_glutton.GreatGluttonRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.infernal_arrow.InfernalArrowRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.infernal_bolt.InfernalBoltRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.infernal_bomb.InfernalBombRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.maggot.MaggotProjectileRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.malevolent_shrine.DomainRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.nightmare_rend.NightmareRendRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.phantom_copy.PhantomCopyRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.pumpkins.PumpkinProjectileRenderer;
import net.melvinczyk.borninspellbooks.entity.spells.trident.TridentRenderer;
import net.melvinczyk.borninspellbooks.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(BornInSpellbooks.MODID)
public class BornInSpellbooks
{
    public static final String MODID = "born_in_spellbooks";
    private static final Logger LOGGER = LogUtils.getLogger();

    public BornInSpellbooks()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MASpellRegistry.register(modEventBus);
        MAEntityRegistry.register(modEventBus);
        MAMobEffectRegistry.register(modEventBus);
        MAAttributeRegistry.register(modEventBus);
        MASchoolRegistry.register(modEventBus);
        MAItemRegistry.register(modEventBus);
        MACreativeTabRegistry.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    public static ResourceLocation id(@NotNull String path)
    {
        return new ResourceLocation(BornInSpellbooks.MODID, path);
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
        LOGGER.info("This took me too long to make...");
    }
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

            //EntityRenderers.register(MAEntityRegistry.SUMMONED_BONE_SERPENT.get(), SummonedBoneSerpentRenderer::new);
            EntityRenderers.register(MAEntityRegistry.TRIDENT_PROJECTILE.get(), TridentRenderer::new);
            EntityRenderers.register(MAEntityRegistry.SUMMONED_DREAD_HOUND.get(), SummonedDreadHoundRenderer::new);
            EntityRenderers.register(MAEntityRegistry.SUMMONED_SKELETON_THRASHER.get(), SummonedSkeletonThrasherRenderer::new);
            EntityRenderers.register(MAEntityRegistry.SCARLET_PERSECUTOR.get(), MAScarletPersecutorRenderer::new);
            EntityRenderers.register(MAEntityRegistry.CORPSEFLY_PATHFINDER.get(), CorpseFlyPathFinderRenderer::new);
            EntityRenderers.register(MAEntityRegistry.SPAWNED_MAGGOT.get(), SpawnedMaggotRenderer::new);
            EntityRenderers.register(MAEntityRegistry.INFERNAL_BOMB.get(), InfernalBombRenderer::new);
            EntityRenderers.register(MAEntityRegistry.INFERNAL_FIRE.get(), NoopRenderer::new);
            EntityRenderers.register(MAEntityRegistry.INFERNAL_BOLT_PROJECTILE.get(), InfernalBoltRenderer::new);
            EntityRenderers.register(MAEntityRegistry.GREAT_GLUTTON.get(), GreatGluttonRenderer::new);
            EntityRenderers.register(MAEntityRegistry.INFERNAL_ARROW_PROJECTILE.get(), InfernalArrowRenderer::new);
            EntityRenderers.register(MAEntityRegistry.SUMMONED_BARREL_ZOMBIE.get(), SummonedBarrelZombieRenderer::new);
            EntityRenderers.register(MAEntityRegistry.SWARM_PROJECTILE.get(), NoopRenderer::new);
            EntityRenderers.register(MAEntityRegistry.NIGHTMARE_REND.get(), NightmareRendRenderer::new);
            EntityRenderers.register(MAEntityRegistry.ZOMBIE_BRUISER.get(), SummonedZombieBruiserRenderer::new);
            EntityRenderers.register(MAEntityRegistry.LINGERING_STAIN.get(), NoopRenderer::new);
            EntityRenderers.register(MAEntityRegistry.PHANTOM_COPY.get(), PhantomCopyRenderer::new);
            EntityRenderers.register(MAEntityRegistry.DOMAIN.get(), DomainRenderer::new);
            EntityRenderers.register(MAEntityRegistry.WATER_FIELD.get(), WaterEffectRenderer::new);
            EntityRenderers.register(MAEntityRegistry.STUN_FIELD.get(), NoopRenderer::new);


            EntityRenderers.register(MAEntityRegistry.PUMPKIN_PROJECTILE.get(), PumpkinProjectileRenderer::new);
            EntityRenderers.register(MAEntityRegistry.PUMPKIN_BOMB.get(), PumpkinBombRenderer::new);
            EntityRenderers.register(MAEntityRegistry.PUMPKIN_FRIEND.get(), PumpkinFriendRenderer::new);


            EntityRenderers.register(MAEntityRegistry.MAGGOT_PROJECTILE.get(), MaggotProjectileRenderer::new);
        }
    }
}
