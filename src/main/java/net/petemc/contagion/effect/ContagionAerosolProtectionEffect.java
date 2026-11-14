package net.petemc.contagion.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ContagionAerosolProtectionEffect extends MobEffect {
    public ContagionAerosolProtectionEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) { return false; }
}
