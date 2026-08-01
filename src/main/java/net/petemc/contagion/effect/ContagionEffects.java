package net.petemc.contagion.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.petemc.contagion.Contagion;

public class ContagionEffects {
    public static Holder<MobEffect> INFECTION;
    public static Holder<MobEffect> RESET_INFECTION;
    public static Holder<MobEffect> IMMUNITY;
    public static Holder<MobEffect> INFECTIOUS;

    public static void registerEffects() {
        INFECTION = registerContagionEffect("infection", new ContagionInfectionEffect(MobEffectCategory.HARMFUL, 5592405));
        RESET_INFECTION = registerContagionEffect("reset_infection", new ContagionResetInfectionEffect(MobEffectCategory.BENEFICIAL, 16755200));
        IMMUNITY = registerContagionEffect("immunity", new ContagionImmunityEffect(MobEffectCategory.BENEFICIAL, 0x651b74));
        INFECTIOUS = registerContagionEffect("infectious", new ContagionInfectiousEffect(MobEffectCategory.HARMFUL, 0xAA0000));
    }

    private static Holder<MobEffect> registerContagionEffect(String name, MobEffect mobEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Contagion.MOD_ID, name), mobEffect);
    }
}
