package net.melvinczyk.borninspellbooks.spells.blood;


import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import io.redspace.ironsspellbooks.network.spell.ClientboundSyncTargetingData;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.entity.spells.lingering_stain.LingeringStainField;
import net.melvinczyk.borninspellbooks.entity.spells.malevolent_shrine.Domain;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class MalevolentShrineSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BornInSpellbooks.MODID, "malevolent_shrine");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster)
    {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getSpellPower(spellLevel, caster) * 0.1F + 2, 1))
        );
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.BLOOD_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(8)
            .build();

    public MalevolentShrineSpell()
    {
        this.manaCostPerLevel = 25;
        this.baseSpellPower = 20;
        this.spellPowerPerLevel = 4;
        this.castTime = 70;
        this.baseManaCost = 700;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.RAISE_DEAD_START.get());
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
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound()
    {
        return Optional.of(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("born_in_chaos_v1:persecutor_scream")));
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        {
            Domain domain = new Domain(world, entity);
            domain.setPos(entity.position());
            world.addFreshEntity(domain);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
}
