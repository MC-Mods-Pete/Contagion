package net.petemc.contagion.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.petemc.contagion.Contagion;

public class ContagionItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Contagion.MOD_ID);

    public static final DeferredItem<Item> CONTAGIOUS_FLESH = ITEMS.register("contagious_flesh",
            () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Contagion.MOD_ID, "contagious_flesh")))
                    .food(ContagionFoodComponents.CONTAGIOUS_FLESH, ContagionConsumables.CONTAGIOUS_FLESH)));
    public static final DeferredItem<Item> HEAT_TREATED_FLESH = ITEMS.register("heat_treated_flesh",
            () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Contagion.MOD_ID, "heat_treated_flesh")))
                    .food(ContagionFoodComponents.HEAT_TREATED_FLESH)));
    public static final DeferredItem<Item> GOLD_STREAKED_FLESH = ITEMS.register("gold_streaked_flesh",
            () -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Contagion.MOD_ID, "gold_streaked_flesh")))
                    .food(ContagionFoodComponents.GOLD_STREAKED_FLESH, ContagionConsumables.GOLD_STREAKED_FLESH)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
