package net.petemc.contagion.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.petemc.contagion.Config;
import net.petemc.contagion.effect.ContagionEffects;

public class ContagionFoodComponents {
    public static final FoodProperties CONTAGIOUS_FLESH = new FoodProperties.Builder().nutrition(4).saturationModifier(0.1f).build();
    public static final FoodProperties HEAT_TREATED_FLESH = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).build();
    public static final FoodProperties GOLD_STREAKED_FLESH = new FoodProperties.Builder().nutrition(6).saturationModifier(1.0f).build();
}
