package net.melvinczyk.melsadditions.registry;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.util.ModTags;
import net.melvinczyk.melsadditions.MelsAdditions;
import net.melvinczyk.melsadditions.tags.MATags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import io.redspace.ironsspellbooks.registries.SoundRegistry;


public class MASchoolRegistry extends SchoolRegistry {
    private static final DeferredRegister<SchoolType> MELS_ADDONS_SCHOOLS = DeferredRegister.create(SCHOOL_REGISTRY_KEY, MelsAdditions.MODID);

    public static void register(IEventBus eventBus)
    {
        MELS_ADDONS_SCHOOLS.register(eventBus);
        eventBus.addListener(SchoolRegistry::clientSetup);
    }

    private static RegistryObject<SchoolType> registerSchool(SchoolType schoolType) {
        return MELS_ADDONS_SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }

    public static final ResourceLocation WATER_RESOURCE = MelsAdditions.id("water");

    public static final RegistryObject<SchoolType> WATER = registerSchool(new SchoolType
            (
                WATER_RESOURCE,
                MATags.WATER_FOCUS,
                Component.translatable("school.mels_addons.water").withStyle(Style.EMPTY.withColor(0xebded)),
                LazyOptional.of(MAAttributeRegistry.WATER_MAGIC_POWER::get),
                LazyOptional.of(MAAttributeRegistry.WATER_MAGIC_RESIST::get),
                LazyOptional.of(SoundRegistry.FIRE_CAST::get)
            ));
}
