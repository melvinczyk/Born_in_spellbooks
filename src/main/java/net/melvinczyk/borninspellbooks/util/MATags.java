package net.melvinczyk.borninspellbooks.util;

import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MATags {
    public static final TagKey<Item> WATER_FOCUS = ItemTags.create(new ResourceLocation(BornInSpellbooks.MODID, "water_focus"));
}
