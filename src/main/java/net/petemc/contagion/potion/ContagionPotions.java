package net.petemc.contagion.potion;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.petemc.contagion.Config;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.effect.ContagionEffects;

public class ContagionPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, Contagion.MOD_ID);

    public static final Holder<Potion> CURE_POTION = POTIONS.register("cure_potion",
            () -> new Potion(new MobEffectInstance(ContagionEffects.IMMUNITY, Config.immunityDuration * 20, 0)));
    public static final Holder<Potion> LONG_CURE_POTION = POTIONS.register("long_cure_potion",
            () -> new Potion(new MobEffectInstance(ContagionEffects.IMMUNITY, Config.immunityDuration * 20 * 3, 0)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
