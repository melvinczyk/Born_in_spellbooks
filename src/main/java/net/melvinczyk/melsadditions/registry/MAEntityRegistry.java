package net.melvinczyk.melsadditions.registry;

import net.melvinczyk.melsadditions.MelsAdditions;
import net.melvinczyk.melsadditions.entity.mobs.SummonedBoneSerpent;
import net.melvinczyk.melsadditions.entity.mobs.SummonedDreadHound;
import net.melvinczyk.melsadditions.entity.mobs.SummonedRavager;
import net.melvinczyk.melsadditions.entity.mobs.SummonedSkeletonThrasher;
import net.melvinczyk.melsadditions.entity.spells.trident.TridentProjectile;
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

    // Mobs
    public static final RegistryObject<EntityType<SummonedBoneSerpent>> SUMMONED_BONE_SERPENT =
            ENTITIES.register("summoned_bone_serpent", () -> EntityType.Builder.<SummonedBoneSerpent>of(SummonedBoneSerpent::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(MelsAdditions.MODID, "summoned_bone_serpent").toString()));


    public static final RegistryObject<EntityType<SummonedRavager>> SUMMONED_RAVAGER =
            ENTITIES.register("summoned_ravager", () -> EntityType.Builder.<SummonedRavager>of(SummonedRavager::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(MelsAdditions.MODID, "summoned_ravager").toString()));


    public static final RegistryObject<EntityType<SummonedDreadHound>> SUMMONED_DREAD_HOUND =
            ENTITIES.register("summoned_dread_hound", () -> EntityType.Builder.<SummonedDreadHound>of(SummonedDreadHound::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(MelsAdditions.MODID, "summoned_dread_hound").toString()));

    public static final RegistryObject<EntityType<SummonedSkeletonThrasher>> SUMMONED_SKELETON_THRASHER =
            ENTITIES.register("summoned_skeleton_thrasher", () -> EntityType.Builder.<SummonedSkeletonThrasher>of(SummonedSkeletonThrasher::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(MelsAdditions.MODID, "summoned_skeleton_thrasher").toString()));


    // Projectiles
    public static final RegistryObject<EntityType<TridentProjectile>> TRIDENT_PROJECTILE =
            ENTITIES.register("trident_projectile", () -> EntityType.Builder.<TridentProjectile>of(TridentProjectile::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(MelsAdditions.MODID, "summoned_trident").toString()));

}
