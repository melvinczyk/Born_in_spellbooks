package net.melvinczyk.borninspellbooks.entity.spells.blind;

import io.redspace.ironsspellbooks.damage.DamageSources;
import net.mcreator.borninchaosv.entity.StaffofBlindnessProjectileEntity;
import net.mcreator.borninchaosv.init.BornInChaosV1ModEntities;
import net.melvinczyk.borninspellbooks.registry.SpellRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

public class BlindnessProjectile extends StaffofBlindnessProjectileEntity {
    public BlindnessProjectile(PlayMessages.SpawnEntity packet, Level world) {
        super(packet, world);
    }

    protected float baseDamage;

    @Override
    public void onHitEntity(EntityHitResult entityHitResult)
    {
        super.onHitEntity(entityHitResult);
        var target = entityHitResult.getEntity();
        DamageSources.applyDamage(target, getDamage(), SpellRegistries.BLIND.get().getDamageSource(this, getOwner()));
        discard();
    }

    public float getDamage()
    {
        return this.baseDamage;
    }


}
