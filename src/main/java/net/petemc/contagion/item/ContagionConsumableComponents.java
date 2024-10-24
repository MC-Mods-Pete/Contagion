package net.petemc.contagion.item;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.petemc.contagion.config.ContagionConfig;
import net.petemc.contagion.effect.ContagionEffects;

import static net.minecraft.component.type.ConsumableComponents.food;

public class ContagionConsumableComponents {
    public static final ConsumableComponent CONTAGIOUS_FLESH = food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(ContagionEffects.INFECTION, ContagionConfig.INSTANCE.infectionDuration * 20, 0), 1.0F))
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.HUNGER, 600), 0.6F))
            .build();
    public static final ConsumableComponent GOLD_STREAKED_FLESH = food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(ContagionEffects.RESET_INFECTION, 1), 1.0F))
            .build();
}
