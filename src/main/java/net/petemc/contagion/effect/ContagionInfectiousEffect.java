package net.petemc.contagion.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import org.jetbrains.annotations.NotNull;

/**
 * Applied to entities that have reached the infectious stage of infection.
 * Duration is calculated to match the remaining 40% of the infection timer,
 * so both effects expire simultaneously when the infection ends.
 */
public class ContagionInfectiousEffect extends StatusEffect {
    public ContagionInfectiousEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean canApplyUpdateEffect(@NotNull int duration, int amplifier) {
        return true;
    }
}
