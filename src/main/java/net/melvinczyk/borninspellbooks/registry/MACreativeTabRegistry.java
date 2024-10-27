package net.melvinczyk.borninspellbooks.registry;

import net.melvinczyk.borninspellbooks.BornInSpellbooks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = BornInSpellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MACreativeTabRegistry {

    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BornInSpellbooks.MODID);

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }

    public static final RegistryObject<CreativeModeTab> EQUIPMENT_TAB = TABS.register("equipment", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + BornInSpellbooks.MODID + ".equipment_tab"))
            .icon(() -> new ItemStack(MAItemRegistry.MISSIONER_GRIMOIRE.get()))
            .displayItems((enabledFeatures, entries) -> {
                entries.accept(MAItemRegistry.MISSIONER_GRIMOIRE.get());
                entries.accept(MAItemRegistry.DARK_METAL_SPELL_BOOK.get());
            })
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .build());
}
