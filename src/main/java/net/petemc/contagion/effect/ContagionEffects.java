package net.petemc.contagion.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.petemc.contagion.Contagion;


public class ContagionEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Contagion.MOD_ID);

    public static final Holder<MobEffect> INFECTION = MOB_EFFECTS.register("infection",
            () -> new ContagionInfectionEffect(MobEffectCategory.HARMFUL, 5592405));
    public static final Holder<MobEffect> RESET_INFECTION = MOB_EFFECTS.register("reset_infection",
            () -> new ContagionResetInfectionEffect(MobEffectCategory.BENEFICIAL, 16755200));
    public static final Holder<MobEffect> IMMUNITY = MOB_EFFECTS.register("immunity",
            () -> new ContagionImmunityEffect(MobEffectCategory.BENEFICIAL, 10118365));


    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}