package net.petemc.contagion.item;

import net.minecraft.component.type.FoodComponent;

public class ContagionFoodComponents {
    public static final FoodComponent CONTAGIOUS_FLESH = new FoodComponent.Builder().nutrition(4).saturationModifier(0.1f).build();
    public static final FoodComponent HEAT_TREATED_FLESH = new FoodComponent.Builder().nutrition(4).saturationModifier(0.3f).build();
    public static final FoodComponent GOLD_STREAKED_FLESH = new FoodComponent.Builder().nutrition(6).saturationModifier(1.0f).alwaysEdible().build();
}
