package net.petemc.contagion.potion;

import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.item.ContagionItems;

public class ContagionPotions {
    public static Holder<Potion> CURE_POTION;
    public static Holder<Potion> LONG_CURE_POTION;

    public static Holder<Potion> registerPotion(String name, MobEffectInstance mobEffectInstance) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, Identifier.fromNamespaceAndPath(Contagion.MOD_ID, name), new Potion(name, mobEffectInstance));
    }

    public static void registerPotions() {
        CURE_POTION = registerPotion("cure_potion", new MobEffectInstance(ContagionEffects.IMMUNITY, MainConfig.getImmunityDuration() * 20, 0));
        LONG_CURE_POTION = registerPotion("long_cure_potion", new MobEffectInstance(ContagionEffects.IMMUNITY, MainConfig.getImmunityDuration() * 3 * 20, 0));
        registerPotionRecipes();
    }

    private static void registerPotionRecipes() {
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(ContagionItems.GOLD_STREAKED_FLESH), ContagionPotions.CURE_POTION);
            builder.registerPotionRecipe(ContagionPotions.CURE_POTION, Ingredient.of(Items.REDSTONE), ContagionPotions.LONG_CURE_POTION);
        });
    }
}
