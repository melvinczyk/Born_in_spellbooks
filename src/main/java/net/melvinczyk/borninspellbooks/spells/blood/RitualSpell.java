package net.melvinczyk.borninspellbooks.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.network.spell.ClientboundSyncTargetingData;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.entity.spells.infernal_bomb.InfernalFireField;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class RitualSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "ritual");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster)
    {
        return List.of(
                Component.translatable("ui.irons_spellbooks.healing", Utils.stringTruncation(1+ getSpellPower(spellLevel, caster) * 0.1F, 1)),
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getSpellPower(spellLevel, caster) * 0.25F, 1)),
                Component.translatable("ui.irons_spellbooks.effect_length", Utils.stringTruncation(getSpellPower(spellLevel, caster) * 0.6f, 2))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setMaxLevel(8)
            .setCooldownSeconds(120)
            .build();

    public RitualSpell()
    {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 4;
        this.castTime = 30;
        this.baseManaCost = 150;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.RAISE_DEAD_START.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound()
    {
        return Optional.of(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1:stomach_open")));
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        float aimAssist = .25f;
        float range = 25f;
        Vec3 start = entity.getEyePosition();
        Vec3 end = entity.getLookAngle().normalize().scale(range).add(start);
        var target = Utils.raycastForEntity(entity.level(), entity, start, end, true, aimAssist, (e) -> e instanceof MagicSummon summon && summon.getSummoner() == entity);
        if (target instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity livingTarget) {
            playerMagicData.setAdditionalCastData(new TargetEntityCastData(livingTarget));
            if (entity instanceof ServerPlayer serverPlayer) {
                Messages.sendToPlayer(new ClientboundSyncTargetingData(livingTarget, this), serverPlayer);
                serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.spell_target_success", livingTarget.getDisplayName().getString(), this.getDisplayName(serverPlayer)).withStyle(ChatFormatting.GREEN)));
                for (int i = 0; i < 36; i++) {
                    double x, z;
                    double theta = Math.toRadians((double) 360 / 36) * i;
                    x = Math.cos(theta) * 1.5f;
                    z = Math.sin(theta) * 1.5f;
                    MagicManager.spawnParticles(level, (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "ritual")), entity.position().x + x, entity.position().y, entity.position().z + z, 1, 0, 0, 0, 0.1, false);
                    MagicManager.spawnParticles(level, (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "ritual")), entityHit.getEntity().position().x + x, entityHit.getEntity().position().y, entityHit.getEntity().position().z + z, 1, 0, 0, 0, 0.1, false);
                }
            }
            return true;
        } else if (entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetActionBarTextPacket(Component.translatable("ui.irons_spellbooks.sacrifice_target_failure").withStyle(ChatFormatting.RED)));
        }
        return false;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData) {
            var targetEntity = targetData.getTarget((ServerLevel) world);
            if (targetEntity instanceof MagicSummon summon && summon.getSummoner().getUUID().equals(entity.getUUID())) {
                MagicManager.spawnParticles(world, (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "fleshsplash")), targetEntity.getX(), targetEntity.getY() + targetEntity.getBbHeight() * .5f, targetEntity.getZ(), 50, targetEntity.getBbWidth() * .5f, targetEntity.getBbHeight() * .5f, targetEntity.getBbWidth() * .5f, .03, false);
                for (int i = 0; i < 36; i++) {
                    double x, z;
                    double theta = Math.toRadians((double) 360 / 36) * i;
                    x = Math.cos(theta) * 1.5f;
                    z = Math.sin(theta) * 1.5f;
                    MagicManager.spawnParticles(world, (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "ritual")), entity.position().x + x, entity.position().y, entity.position().z + z, 1, 0, 0, 0, 0.1, false);
                    MagicManager.spawnParticles(world, (ParticleOptions) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation("born_in_chaos_v1", "ritual")), targetEntity.position().x + x, targetEntity.position().y, targetEntity.position().z + z, 1, 0, 0, 0, 0.1, false);
                }
                float health = targetEntity.getHealth();
                int amplifier = (int)Math.floor(health * 0.025f);
                entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, (getDuration(spellLevel, entity)) * 20, amplifier, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, (getDuration(spellLevel, entity)) * 20, amplifier, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, (getDuration(spellLevel, entity)) * 20, amplifier, false, false));
                entity.heal(1+ getSpellPower(spellLevel, entity) * 0.1f);
                targetEntity.remove(Entity.RemovalReason.KILLED);
            }
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public int getDuration(int spellLevel, LivingEntity caster) {
        return (int) (getSpellPower(spellLevel, caster) * 0.6f);
    }
}
