package net.petemc.contagion.potion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;

public class ContagionPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, Contagion.MOD_ID);

    public static final RegistryObject<Potion> CURE_POTION = POTIONS.register("cure_potion",
            () -> new Potion(new MobEffectInstance(ContagionEffects.IMMUNITY.get(), MainConfig.getImmunityDuration() * 20, 0)));
    public static final RegistryObject<Potion> LONG_CURE_POTION = POTIONS.register("long_cure_potion",
            () -> new Potion(new MobEffectInstance(ContagionEffects.IMMUNITY.get(), MainConfig.getImmunityDuration() * 20 * 3, 0)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
