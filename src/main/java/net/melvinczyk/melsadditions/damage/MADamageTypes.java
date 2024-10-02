package net.melvinczyk.melsadditions.damage;

import net.melvinczyk.melsadditions.MelsAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class MADamageTypes {
    public static ResourceKey<DamageType> register(String name)
    {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(MelsAdditions.MODID, name));
    }

    public static final ResourceKey<DamageType> WATER_MAGIC = register("water_magic");

    public static void bootstrap(BootstapContext<DamageType> context)
    {
        context.register(WATER_MAGIC, new DamageType(WATER_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f));
    }

}