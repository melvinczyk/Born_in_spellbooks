package net.melvinczyk.borninspellbooks.effect;

import io.redspace.ironsspellbooks.effect.TrueInvisibilityEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SneakyTrueInvisibilityEffect extends TrueInvisibilityEffect {
    public SneakyTrueInvisibilityEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

    }
}
