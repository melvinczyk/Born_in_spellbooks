package net.melvinczyk.borninspellbooks.util;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class MAMobEffectInstance extends MobEffectInstance {

    private final LivingEntity caster;
    private final float spellPower;
    public MAMobEffectInstance(MobEffect pEffect, int pDuration, int amplifier, LivingEntity caster, float spellPower) {
        super(pEffect, pDuration, amplifier);
        this.caster = caster;
        this.spellPower = spellPower;
    }

    public MAMobEffectInstance(MobEffect pEffect, int pDuration, int amplifier, LivingEntity caster) {
        super(pEffect, pDuration, amplifier);
        this.caster = caster;
        this.spellPower = 0;
    }

    public LivingEntity getCaster()
    {
        return this.caster;
    }

    public float getSpellpower()
    {
        return this.spellPower;
    }
}
