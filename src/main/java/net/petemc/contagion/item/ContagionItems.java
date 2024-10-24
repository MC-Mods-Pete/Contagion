package net.petemc.contagion.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.petemc.contagion.Contagion;

public class ContagionItems {
    private static final String nameContagiousFlesh = "contagious_flesh";
    private static final String nameHeatTreatedFlesh = "heat_treated_flesh";
    private static final String nameGoldStreakedFlesh = "gold_streaked_flesh";

    public static final Item CONTAGIOUS_FLESH = registerItem(nameContagiousFlesh, new Item(new Item.Settings()
            .food(ContagionFoodComponents.CONTAGIOUS_FLESH, ContagionConsumableComponents.CONTAGIOUS_FLESH)
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Contagion.MOD_ID, nameContagiousFlesh)))));
    public static final Item HEAT_TREATED_FLESH = registerItem(nameHeatTreatedFlesh, new Item(new Item.Settings()
            .food(ContagionFoodComponents.HEAT_TREATED_FLESH)
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Contagion.MOD_ID, nameHeatTreatedFlesh)))));
    public static final Item GOLD_STREAKED_FLESH = registerItem(nameGoldStreakedFlesh, new Item(new Item.Settings()
            .food(ContagionFoodComponents.GOLD_STREAKED_FLESH, ContagionConsumableComponents.GOLD_STREAKED_FLESH)
            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Contagion.MOD_ID, nameGoldStreakedFlesh)))));

    private static void addItemsToFoodDrinkItemGroup(FabricItemGroupEntries entries) {
        entries.add(CONTAGIOUS_FLESH);
        entries.add(HEAT_TREATED_FLESH);
        entries.add(GOLD_STREAKED_FLESH);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Contagion.MOD_ID, name), item);
    }

    public static void registerItems() {
        Contagion.LOGGER.info("Registering Mod Items for " + Contagion.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(ContagionItems::addItemsToFoodDrinkItemGroup);
    }
}
