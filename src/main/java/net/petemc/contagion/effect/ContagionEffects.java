package net.petemc.contagion.effect;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.petemc.contagion.Contagion;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

public class ContagionEffects {
    public static RegistryEntry<StatusEffect> INFECTION;
    public static RegistryEntry<StatusEffect> RESET_INFECTION;
    public static RegistryEntry<StatusEffect> IMMUNITY;

    public static void registerEffects() {
        INFECTION = registerContagionEffect("infection", new ContagionInfectionEffect(StatusEffectCategory.HARMFUL, 5592405));
        RESET_INFECTION = registerContagionEffect("reset_infection", new ContagionResetInfectionEffect(StatusEffectCategory.BENEFICIAL, 16755200));
        IMMUNITY = registerContagionEffect("immunity", new ContagionImmunityEffect(StatusEffectCategory.BENEFICIAL, 0x651b74));
    }

    private static RegistryEntry<StatusEffect> registerContagionEffect(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, new Identifier(Contagion.MOD_ID, name), statusEffect);
    }
}