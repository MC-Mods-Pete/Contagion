package net.petemc.contagion.potion;

import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.ContagionConfigs;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.item.ContagionItems;

public class ContagionPotions {
    public static RegistryEntry<Potion> CURE_POTION;
    public static RegistryEntry<Potion> LONG_CURE_POTION;

    public static RegistryEntry<Potion> registerPotion(String name, StatusEffectInstance statusEffectInstance) {
        return Registry.registerReference(Registries.POTION, Identifier.of(Contagion.MOD_ID, name), new Potion(statusEffectInstance));
    }

    public static void registerPotions() {
        CURE_POTION = registerPotion("cure_potion", new StatusEffectInstance(ContagionEffects.IMMUNITY, ContagionConfigs.DURATION_IMMUNITY *20, 0));
        LONG_CURE_POTION = registerPotion("long_cure_potion", new StatusEffectInstance(ContagionEffects.IMMUNITY, ContagionConfigs.DURATION_IMMUNITY * 3 * 20, 0));
        registerPotionRecipes();
    }

    private static void registerPotionRecipes() {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, ContagionItems.GOLD_STREAKED_FLESH, ContagionPotions.CURE_POTION);
            builder.registerPotionRecipe(ContagionPotions.CURE_POTION, Items.REDSTONE, ContagionPotions.LONG_CURE_POTION);
        });
    }
}
