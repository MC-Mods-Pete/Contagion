package net.petemc.contagion.effect;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.petemc.contagion.Contagion;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

public class ContagionEffects {
    public static StatusEffect INFECTION;
    public static StatusEffect RESET_INFECTION;
    public static StatusEffect IMMUNITY;

    public static StatusEffect registerInfectionStatusEffect(String name) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(Contagion.MOD_ID, name),
                new ContagionInfectionEffect(StatusEffectCategory.HARMFUL, 5592405));
    }

    public static StatusEffect registerResetInfectionStatusEffect(String name) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(Contagion.MOD_ID, name),
                new ContagionResetInfectionEffect(StatusEffectCategory.BENEFICIAL, 16755200));
    }

    public static StatusEffect registerResistanceStatusEffect(String name) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(Contagion.MOD_ID, name),
                new ContagionImmunityEffect(StatusEffectCategory.BENEFICIAL, 0x651b74));
    }

    public static void registerEffects() {
        INFECTION = registerInfectionStatusEffect("infection");
        RESET_INFECTION = registerResetInfectionStatusEffect("reset_infection");
        IMMUNITY = registerResistanceStatusEffect("immunity");
    }
}