package net.petemc.contagion.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.petemc.contagion.Contagion;

public class ContagionItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Contagion.MOD_ID);

    public static final RegistryObject<Item> CONTAGIOUS_FLESH = ITEMS.register("contagious_flesh",
            () -> new Item(new Item.Properties().food(ContagionFoodComponents.CONTAGIOUS_FLESH)));
    public static final RegistryObject<Item> HEAT_TREATED_FLESH = ITEMS.register("heat_treated_flesh",
            () -> new Item(new Item.Properties().food(ContagionFoodComponents.HEAT_TREATED_FLESH)));
    public static final RegistryObject<Item> GOLD_STREAKED_FLESH = ITEMS.register("gold_streaked_flesh",
            () -> new Item(new Item.Properties().food(ContagionFoodComponents.GOLD_STREAKED_FLESH)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
