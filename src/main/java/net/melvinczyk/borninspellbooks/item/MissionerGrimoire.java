package net.melvinczyk.borninspellbooks.item;

import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.item.UniqueSpellBook;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.melvinczyk.borninspellbooks.registry.MASpellRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MissionerGrimoire extends UniqueSpellBook {
    public MissionerGrimoire() {
        super(SpellRarity.LEGENDARY, SpellDataRegistryHolder.of(
                new SpellDataRegistryHolder(SpellRegistry.RAISE_DEAD_SPELL, 3),
                new SpellDataRegistryHolder(SpellRegistry.FANG_STRIKE_SPELL, 5),
                new SpellDataRegistryHolder(MASpellRegistry.RITUAl, 5)
        ), 7, () -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 200, AttributeModifier.Operation.ADDITION));
            builder.put(AttributeRegistry.EVOCATION_SPELL_POWER.get(), new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.075, AttributeModifier.Operation.MULTIPLY_BASE));
            builder.put(AttributeRegistry.BLOOD_SPELL_POWER.get(), new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon modifier", 0.075, AttributeModifier.Operation.MULTIPLY_BASE));
            return builder.build();
        });
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> lines, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, level, lines, flag);
        var affinityData = AffinityData.getAffinityData(itemStack);
        var spell = affinityData.getSpell();
        if (spell != SpellRegistry.none()) {
            int i = TooltipsUtils.indexOfComponent(lines, "tooltip.irons_spellbooks.spellbook_spell_count");
            lines.add(i < 0 ? lines.size() : i+1, Component.translatable("tooltip.irons_spellbooks.enhance_spell_level", spell.getDisplayName(MinecraftInstanceHelper.instance.player()).withStyle(spell.getSchoolType().getDisplayName().getStyle())).withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        super.initializeSpellContainer(itemStack);
        AffinityData.setAffinityData(itemStack, MASpellRegistry.SUMMON_ZOMBIE_BRUISER.get());
        AffinityData.setAffinityData(itemStack, MASpellRegistry.SUMMON_SKELETON_THRASHER.get());

    }
}
