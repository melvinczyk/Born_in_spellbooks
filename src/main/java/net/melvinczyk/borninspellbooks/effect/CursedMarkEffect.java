package net.melvinczyk.borninspellbooks.effect;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.melvinczyk.borninspellbooks.entity.mobs.ScarletPersecutor;
import net.melvinczyk.borninspellbooks.registry.MAEntityRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class CursedMarkEffect extends MagicMobEffect {
    public CursedMarkEffect(MobEffectCategory pCategory, int pColor)
    {
        super(pCategory, pColor);
    }

    public CursedMarkEffect(MobEffectCategory pCategory, int pColor, LivingEntity caster)
    {
        super(pCategory, pColor);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
        spawnScarletPersecutor(entity, amplifier);
    }

    private void playSound(LivingEntity targetEntity)
    {
        targetEntity.level().playSound(null, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ(), (SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1:persecutor_scream")), SoundSource.HOSTILE, 1.0F, 1.0F);
    }


    private void spawnScarletPersecutor(LivingEntity targetEntity, int amplifier) {
        if (!targetEntity.level().isClientSide) {
            var level = targetEntity.level();

            float radius = 2.0f + .185f * 4;
            playSound(targetEntity);
            int spellLevel = amplifier + 1;
            for (int i = 0; i <  spellLevel; i++)
            {
                if(i % 2 == 0)
                {
                    ScarletPersecutor scarletPersecutor = MAEntityRegistry.SCARLET_PERSECUTOR.get().create(level);
                    if (scarletPersecutor != null) {
                        scarletPersecutor.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(scarletPersecutor.getOnPos()), MobSpawnType.MOB_SUMMONED, null, null);
                        var yrot = 6.281f / 8 * i + targetEntity.getYRot() * Mth.DEG_TO_RAD;
                        Vec3 spawn = Utils.moveToRelativeGroundLevel(level, targetEntity.getEyePosition().add(new Vec3(radius * Mth.cos(yrot), 1, radius * Mth.sin(yrot))), 10);
                        scarletPersecutor.setPos(spawn.x, spawn.y, spawn.z);
                        scarletPersecutor.setYRot(targetEntity.getYRot());
                        scarletPersecutor.setOldPosAndRot();

                        scarletPersecutor.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(5 + amplifier);
                        scarletPersecutor.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(15 + amplifier);

                        level.addFreshEntity(scarletPersecutor);
                        scarletPersecutor.assignTarget(targetEntity);
                    }

                }
            }
        }
    }
}
