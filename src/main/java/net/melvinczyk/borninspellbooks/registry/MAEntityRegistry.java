package net.melvinczyk.borninspellbooks.registry;

import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.melvinczyk.borninspellbooks.entity.mobs.*;
import net.melvinczyk.borninspellbooks.entity.spells.gnaw.GluttonFishGnawEntity;
import net.melvinczyk.borninspellbooks.entity.spells.infernal_arrow.InfernalArrowProjectile;
import net.melvinczyk.borninspellbooks.entity.spells.infernal_bolt.InfernalBoltProjectile;
import net.melvinczyk.borninspellbooks.entity.spells.infernal_bomb.InfernalBombEntity;
import net.melvinczyk.borninspellbooks.entity.spells.infernal_bomb.InfernalFireField;
import net.melvinczyk.borninspellbooks.entity.spells.maggot.MaggotProjectile;
import net.melvinczyk.borninspellbooks.entity.spells.swarm.SwarmProjectile;
import net.melvinczyk.borninspellbooks.entity.spells.trident.TridentProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MAEntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BornInSpellbooks.MODID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    // Mobs
    public static final RegistryObject<EntityType<SummonedBoneSerpent>> SUMMONED_BONE_SERPENT =
            ENTITIES.register("summoned_bone_serpent", () -> EntityType.Builder.<SummonedBoneSerpent>of(SummonedBoneSerpent::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "summoned_bone_serpent").toString()));


    public static final RegistryObject<EntityType<SummonedRavager>> SUMMONED_RAVAGER =
            ENTITIES.register("summoned_ravager", () -> EntityType.Builder.<SummonedRavager>of(SummonedRavager::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "summoned_ravager").toString()));


    public static final RegistryObject<EntityType<SummonedDreadHound>> SUMMONED_DREAD_HOUND =
            ENTITIES.register("summoned_dread_hound", () -> EntityType.Builder.<SummonedDreadHound>of(SummonedDreadHound::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "summoned_dread_hound").toString()));


    public static final RegistryObject<EntityType<SummonedSkeletonThrasher>> SUMMONED_SKELETON_THRASHER =
            ENTITIES.register("summoned_skeleton_thrasher", () -> EntityType.Builder.<SummonedSkeletonThrasher>of(SummonedSkeletonThrasher::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "summoned_skeleton_thrasher").toString()));


    public static final RegistryObject<EntityType<ScarletPersecutor>> SCARLET_PERSECUTOR =
            ENTITIES.register("scarlet_persecutor", () -> EntityType.Builder.<ScarletPersecutor>of(ScarletPersecutor::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "scarlet_persecutor").toString()));


    public static final RegistryObject<EntityType<CorpseFlyPathFinder>> CORPSEFLY_PATHFINDER =
            ENTITIES.register("corpsefly_pathfinder", () -> EntityType.Builder.<CorpseFlyPathFinder>of(CorpseFlyPathFinder::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "corpsefly_pathfinder").toString()));


    public static final RegistryObject<EntityType<SpawnedMaggot>> SPAWNED_MAGGOT =
            ENTITIES.register("spawned_maggot", () -> EntityType.Builder.<SpawnedMaggot>of(SpawnedMaggot::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "spawned_maggot").toString()));


    public static final RegistryObject<EntityType<SummonedBarrelZombie>> SUMMONED_BARREL_ZOMBIE =
            ENTITIES.register("summoned_barrel_zombie", () -> EntityType.Builder.<SummonedBarrelZombie>of(SummonedBarrelZombie::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "summoned_barrel_zombie").toString()));


    public static final RegistryObject<EntityType<CorpseFlyBarrier>> CORPSE_FLY_BARRIER =
            ENTITIES.register("corpse_fly_barrier", () -> EntityType.Builder.<CorpseFlyBarrier>of(CorpseFlyBarrier::new, MobCategory.MONSTER)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "corpse_fly_barrier").toString()));



    // Projectiles
    public static final RegistryObject<EntityType<TridentProjectile>> TRIDENT_PROJECTILE =
            ENTITIES.register("trident_projectile", () -> EntityType.Builder.<TridentProjectile>of(TridentProjectile::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "summoned_trident").toString()));


    public static final RegistryObject<EntityType<InfernalBombEntity>> INFERNAL_BOMB =
            ENTITIES.register("fel_bomb", () -> EntityType.Builder.<InfernalBombEntity>of(InfernalBombEntity::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "fel_bomb").toString()));


    public static final RegistryObject<EntityType<MaggotProjectile>> MAGGOT_PROJECTILE =
            ENTITIES.register("maggot_projectile", () -> EntityType.Builder.<MaggotProjectile>of(MaggotProjectile::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "maggot_projectile").toString()));


    public static final RegistryObject<EntityType<InfernalBoltProjectile>> INFERNAL_BOLT_PROJECTILE =
            ENTITIES.register("infernal_bolt_projectile", () -> EntityType.Builder.<InfernalBoltProjectile>of(InfernalBoltProjectile::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "infernal_bolt_projectile").toString()));


    public static final RegistryObject<EntityType<GluttonFishGnawEntity>> GLUTTON_FISH_GNAW =
            ENTITIES.register("glutton_fish_gnaw", () -> EntityType.Builder.<GluttonFishGnawEntity>of(GluttonFishGnawEntity::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "glutton_fish_gnaw").toString()));


    public static final RegistryObject<EntityType<InfernalArrowProjectile>> INFERNAL_ARROW_PROJECTILE =
            ENTITIES.register("infernal_arrow_projectile", () -> EntityType.Builder.<InfernalArrowProjectile>of(InfernalArrowProjectile::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "infernal_arrow_projectile").toString()));


    public static final RegistryObject<EntityType<SwarmProjectile>> SWARM_PROJECTILE =
            ENTITIES.register("swarm_projectile", () -> EntityType.Builder.<SwarmProjectile>of(SwarmProjectile::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "swarm_projectile").toString()));



    // Misc

    public static final RegistryObject<EntityType<InfernalFireField>> INFERNAL_FIRE =
            ENTITIES.register("fel_fire", () -> EntityType.Builder.<InfernalFireField>of(InfernalFireField::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BornInSpellbooks.MODID, "fel_fire").toString()));

}
