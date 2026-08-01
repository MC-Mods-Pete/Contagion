package net.petemc.contagion.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;

public class ContagionFoodComponents {
    public static final FoodProperties CONTAGIOUS_FLESH = new FoodProperties.Builder().nutrition(4).saturationModifier(0.1f)
            .effect(new MobEffectInstance(ContagionEffects.INFECTION, MainConfig.getInfectionDuration() * 20), 1.0f)
            .effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.6f).build();
    public static final FoodProperties HEAT_TREATED_FLESH = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).build();
    public static final FoodProperties GOLD_STREAKED_FLESH = new FoodProperties.Builder().nutrition(6).saturationModifier(1.0f)
            .effect(new MobEffectInstance(ContagionEffects.RESET_INFECTION, 1), 1.0f).alwaysEdible().build();
}
