package net.melvinczyk.borninspellbooks.entity.spells.blind;

import io.redspace.ironsspellbooks.damage.DamageSources;
import net.mcreator.borninchaosv.entity.StaffofBlindnessProjectileEntity;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;

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
        DamageSources.applyDamage(target, getDamage(), MASpellRegistry.BLIND.get().getDamageSource(this, getOwner()));
        discard();
    }

    public float getDamage()
    {
        return this.baseDamage;
    }


}
