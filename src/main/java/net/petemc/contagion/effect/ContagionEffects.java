package net.petemc.contagion.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.petemc.contagion.Contagion;


public class ContagionEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Contagion.MOD_ID);

    public static final RegistryObject<MobEffect> INFECTION = MOB_EFFECTS.register("infection",
            () -> new ContagionInfectionEffect(MobEffectCategory.HARMFUL, 5592405));
    public static final RegistryObject<MobEffect> RESET_INFECTION = MOB_EFFECTS.register("reset_infection",
            () -> new ContagionResetInfectionEffect(MobEffectCategory.BENEFICIAL, 16755200));
    public static final RegistryObject<MobEffect> IMMUNITY = MOB_EFFECTS.register("immunity",
            () -> new ContagionImmunityEffect(MobEffectCategory.BENEFICIAL, 10118365));


    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}