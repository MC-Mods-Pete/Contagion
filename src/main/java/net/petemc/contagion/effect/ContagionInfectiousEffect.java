package net.petemc.contagion.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;

/**
 * Applied to entities that have reached the infectious stage of infection.
 * Duration is calculated to match the remaining 40% of the infection timer,
 * so both effects expire simultaneously when the infection ends.
 */
public class ContagionInfectiousEffect extends MobEffect {
    public ContagionInfectiousEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean isDurationEffectTick(@NotNull int duration, int amplifier) {
        return true;
    }
}
