package net.melvinczyk.melsadditions.tags;

import net.melvinczyk.melsadditions.MelsAdditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MATags {
    public static final TagKey<Item> WATER_FOCUS = ItemTags.create(new ResourceLocation(MelsAdditions.MODID, "water_focus"));
}
