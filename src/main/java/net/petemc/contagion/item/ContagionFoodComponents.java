package net.petemc.contagion.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.petemc.contagion.config.ContagionConfigs;
import net.petemc.contagion.effect.ContagionEffects;

public class ContagionFoodComponents {
    public static final FoodComponent CONTAGIOUS_FLESH = new FoodComponent.Builder().hunger(4).saturationModifier(0.1f)
            .statusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, ContagionConfigs.DURATION_INFECTION_TOTAL * 20), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.8f).meat().build();
    public static final FoodComponent HEAT_TREATED_FLESH = new FoodComponent.Builder().hunger(4).saturationModifier(0.1f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.4f).meat().build();
    public static final FoodComponent GOLD_STREAKED_FLESH = new FoodComponent.Builder().hunger(4).saturationModifier(0.4f)
            .statusEffect(new StatusEffectInstance(ContagionEffects.RESET_INFECTION, 20), 1.0f).alwaysEdible().build();
}
