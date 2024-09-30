package net.melvinczyk.melsadditions.registry;

import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import net.melvinczyk.melsadditions.MelsAdditions;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = MelsAdditions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MAAttributeRegistry {
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MelsAdditions.MODID);

    public static final RegistryObject<Attribute> WATER_MAGIC_RESIST = registerResistanceAttribute("water");

    public static final RegistryObject<Attribute> WATER_MAGIC_POWER = registerPowerAttribute("water");

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event)
    {
        event.getTypes().forEach(entity -> {
            event.add(entity, WATER_MAGIC_RESIST.get());
            event.add(entity, WATER_MAGIC_POWER.get());
        });
    }

    // ;_;
    private static RegistryObject<Attribute> registerResistanceAttribute(String id)
    {
        return ATTRIBUTES.register(id + "_magic_resist", () ->
                (new MagicRangedAttribute("attribute.mels_addons." + id + "_magic_resist",
                        1.0D, 0, 10).setSyncable(true)));
    }

    private static RegistryObject<Attribute> registerPowerAttribute(String id)
    {
        return ATTRIBUTES.register(id + "_spell_power", () ->
                (new MagicRangedAttribute("attribute.mels_addons." + id + "_spell_power",
                        1.0D, 0, 10).setSyncable(true)));
    }

    public static void register(IEventBus eventBus)
    {
        ATTRIBUTES.register(eventBus);
    }
}
