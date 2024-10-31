package net.melvinczyk.borninspellbooks.events;

import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.compat.tetra.TetraProxy;
import net.melvinczyk.borninspellbooks.effect.PhantomSplitEffect;
import net.melvinczyk.borninspellbooks.effect.SpiritEffect;
import net.melvinczyk.borninspellbooks.misc.MASynchedSpellData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
}
