package net.petemc.contagion.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTabs;
import net.petemc.contagion.Contagion;

public class ContagionItems {
    private static final String nameContagiousFlesh = "contagious_flesh";
    private static final String nameHeatTreatedFlesh = "heat_treated_flesh";
    private static final String nameGoldStreakedFlesh = "gold_streaked_flesh";

    public static final Item CONTAGIOUS_FLESH = registerItem(nameContagiousFlesh, new Item(new Item.Properties()
            .food(ContagionFoodComponents.CONTAGIOUS_FLESH, ContagionConsumables.CONTAGIOUS_FLESH)
            .useItemDescriptionPrefix()
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Contagion.MOD_ID, nameContagiousFlesh)))));
    public static final Item HEAT_TREATED_FLESH = registerItem(nameHeatTreatedFlesh, new Item(new Item.Properties()
            .food(ContagionFoodComponents.HEAT_TREATED_FLESH)
            .useItemDescriptionPrefix()
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Contagion.MOD_ID, nameHeatTreatedFlesh)))));
    public static final Item GOLD_STREAKED_FLESH = registerItem(nameGoldStreakedFlesh, new Item(new Item.Properties()
            .food(ContagionFoodComponents.GOLD_STREAKED_FLESH, ContagionConsumables.GOLD_STREAKED_FLESH)
            .useItemDescriptionPrefix()
            .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Contagion.MOD_ID, nameGoldStreakedFlesh)))));

    private static void addItemsToFoodDrinkItemGroup(FabricCreativeModeTabOutput entries) {
        entries.accept(CONTAGIOUS_FLESH);
        entries.accept(HEAT_TREATED_FLESH);
        entries.accept(GOLD_STREAKED_FLESH);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Contagion.MOD_ID, name), item);
    }

    public static void registerItems() {
        Contagion.LOGGER.info("Registering Mod Items for " + Contagion.MOD_ID);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(ContagionItems::addItemsToFoodDrinkItemGroup);
    }
}
