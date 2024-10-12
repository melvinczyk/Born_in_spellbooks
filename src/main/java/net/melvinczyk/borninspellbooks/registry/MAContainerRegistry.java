package net.melvinczyk.borninspellbooks.registry;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


@Mod.EventBusSubscriber(modid = "born_in_spellbooks", bus = Mod.EventBusSubscriber.Bus.MOD)
public class MAContainerRegistry {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "born_in_spellbooks");

    //public static final RegistryObject<MenuType<BarrelZombieContainer>> BARREL_ZOMBIE_CONTAINER = CONTAINERS.register("summoned_barrel_zombie", () -> new MenuType<>(BarrelZombieContainer::new));
}
