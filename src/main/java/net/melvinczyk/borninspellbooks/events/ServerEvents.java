package net.melvinczyk.borninspellbooks.events;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.compat.tetra.TetraProxy;
import net.melvinczyk.borninspellbooks.effect.PhantomSplitEffect;
import net.melvinczyk.borninspellbooks.effect.SpiritEffect;
import net.melvinczyk.borninspellbooks.misc.MASynchedSpellData;
import net.melvinczyk.borninspellbooks.registry.MAMobEffectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

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
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (isInSpiritForm(event.getEntity())) {
            event.setCancellationResult(InteractionResult.FAIL);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (isInSpiritForm(player)) {
            BlockPos pos = event.getPos();
            BlockEntity blockEntity = player.level().getBlockEntity(pos);
            if (blockEntity instanceof Container || blockEntity instanceof BedBlockEntity) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerSpellCast(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        if (player.hasEffect(MAMobEffectRegistry.SPIRIT_EFFECT.get())) {
            MagicData playerMagicData = MagicData.getPlayerMagicData(player);
            if (playerMagicData.isCasting()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    Utils.serverSideCancelCast(serverPlayer);
                    MagicData.getPlayerMagicData(player).getPlayerRecasts().removeRecast(playerMagicData.getCastingSpellId());
                    serverPlayer.displayClientMessage(Component.literal("You cannot cast spells while in Spirit form!").withStyle(ChatFormatting.RED), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerPickupItem(EntityItemPickupEvent event) {
        Player player = event.getEntity();

        if (isInSpiritForm(player)) {
            event.setCanceled(true);
        }
    }

    private static boolean isInSpiritForm(Player player) {
        return player.hasEffect(MAMobEffectRegistry.SPIRIT_EFFECT.get());
    }
}
