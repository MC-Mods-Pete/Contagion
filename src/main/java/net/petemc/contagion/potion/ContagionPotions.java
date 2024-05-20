package net.petemc.contagion.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.ContagionConfigs;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.contagion.mixin.BrewingRecipeRegistryMixin;


public class ContagionPotions {
    public static final Potion CURE_POTION = registerPotion("cure_potion",
            new Potion(new StatusEffectInstance(ContagionEffects.RESISTANCE, ContagionConfigs.DURATION_RESISTANCE*20, 0)));

    private static Potion registerPotion(String name, Potion potion) {
        return Registry.register(Registries.POTION, new Identifier(Contagion.MOD_ID, name), potion);
    }

    public static void registerPotions() {
        Contagion.LOGGER.info("Registering Mod Potions for " + Contagion.MOD_ID);
        registerPotionRecipes();
    }

    private static void registerPotionRecipes() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, ContagionItems.GOLD_STREAKED_FLESH,
                ContagionPotions.CURE_POTION);
    }
}
