package net.melvinczyk.melsadditions.registry;

import net.melvinczyk.melsadditions.MelsAdditions;
import net.melvinczyk.melsadditions.entity.mobs.SummonedBoneSerpent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MAEntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MelsAdditions.MODID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static final RegistryObject<EntityType<SummonedBoneSerpent>> SUMMONED_BONE_SERPENT =
            ENTITIES.register("summoned_bone_serpent", () -> EntityType.Builder.<SummonedBoneSerpent>of(SummonedBoneSerpent::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(MelsAdditions.MODID, "summoned_bone_serpent").toString()));
}
