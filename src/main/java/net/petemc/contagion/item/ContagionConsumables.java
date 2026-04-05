package net.petemc.contagion.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.petemc.contagion.effect.ContagionEffects;

public class ContagionConsumables {
    public static final MobEffectInstance CONTAGIOUS_FLESH_INFECTION_EFFECT =
            new MobEffectInstance(ContagionEffects.INFECTION, 1, 0);

    public static final Consumable CONTAGIOUS_FLESH = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(CONTAGIOUS_FLESH_INFECTION_EFFECT, 1.0F))
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.HUNGER, 600), 0.6F))
            .build();
    public static final Consumable GOLD_STREAKED_FLESH = Consumables.defaultFood()
            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(ContagionEffects.RESET_INFECTION, 1), 1.0F))
            .build();
}
