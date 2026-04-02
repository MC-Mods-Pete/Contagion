package net.petemc.contagion.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;

import static net.minecraft.world.item.component.Consumables.defaultFood;

public class ContagionConsumables {
    public static final Consumable CONTAGIOUS_FLESH = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ContagionEffects.INFECTION, MainConfig.getInfectionDuration() * 20, 0), 1.0F))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 600), 0.6F))
            .build();
    public static final Consumable GOLD_STREAKED_FLESH = defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ContagionEffects.RESET_INFECTION, 1), 1.0F))
            .build();
}
