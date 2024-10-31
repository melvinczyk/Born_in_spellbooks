package net.melvinczyk.borninspellbooks.events;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.compat.tetra.TetraProxy;
import net.mcreator.borninchaosv.entity.MissionerEntity;
import net.melvinczyk.borninspellbooks.effect.PhantomSplitEffect;
import net.melvinczyk.borninspellbooks.effect.SpiritEffect;
import net.melvinczyk.borninspellbooks.misc.MASynchedSpellData;
import net.melvinczyk.borninspellbooks.registry.MAMobEffectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ServerEvents {

    @SubscribeEvent
    public static void onLivingAttacks(LivingAttackEvent event) {
        var livingEntity = event.getEntity();
        if ((livingEntity instanceof ServerPlayer) || (livingEntity instanceof IMagicEntity)) {
            var playerMagicData = MagicData.getPlayerMagicData(livingEntity);
            if (playerMagicData.getSyncedData().hasEffect(MASynchedSpellData.PHANTOM_SPLIT)) {
                if (PhantomSplitEffect.doEffect(livingEntity, event.getSource())) {
                    event.setCanceled(true);
                }
            }
            //TODO: tetra update
            TetraProxy.PROXY.handleLivingAttackEvent(event);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();

        if (SpiritEffect.isImmuneToDamage(entity)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerAttack(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (isInSpiritForm(player)) {
                event.setCanceled(true);
                player.displayClientMessage(Component.literal("You cannot attack while in Spirit form!").withStyle(ChatFormatting.RED), true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (isInSpiritForm(event.getEntity())) {
            event.setCancellationResult(InteractionResult.FAIL);
            event.setCanceled(true);
            event.getEntity().displayClientMessage(Component.literal("You cannot break blocks while in Spirit form!").withStyle(ChatFormatting.RED), true);
        }
    }

    private static boolean isInSpiritForm(Player player) {
        return player.hasEffect(MAMobEffectRegistry.SPIRIT_EFFECT.get());
    }
}
