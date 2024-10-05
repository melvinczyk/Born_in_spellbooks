package net.melvinczyk.borninspellbooks.registry;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.damage.MADamageTypes;
import net.melvinczyk.borninspellbooks.util.MATags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import io.redspace.ironsspellbooks.registries.SoundRegistry;


public class MASchoolRegistry extends SchoolRegistry {
    private static final DeferredRegister<SchoolType> MELS_ADDONS_SCHOOLS = DeferredRegister.create(SCHOOL_REGISTRY_KEY, BornInSpellbooks.MODID);

    public static void register(IEventBus eventBus)
    {
        MELS_ADDONS_SCHOOLS.register(eventBus);
        eventBus.addListener(SchoolRegistry::clientSetup);
    }

    private static RegistryObject<SchoolType> registerSchool(SchoolType schoolType) {
        return MELS_ADDONS_SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }

    public static final ResourceLocation WATER_RESOURCE = BornInSpellbooks.id("water");

    public static final RegistryObject<SchoolType> WATER = registerSchool(new SchoolType(
            WATER_RESOURCE,
            MATags.WATER_FOCUS,
            Component.translatable("school.mels_additions.water").withStyle(ChatFormatting.BLUE),
            LazyOptional.of(MAAttributeRegistry.WATER_MAGIC_POWER::get),
            LazyOptional.of(MAAttributeRegistry.WATER_MAGIC_RESIST::get),
            LazyOptional.of(SoundRegistry.ACID_ORB_IMPACT::get),
            MADamageTypes.WATER_MAGIC
    ));

    public static void clientSetup(ModelEvent.RegisterAdditional event)
    {

    }
}
