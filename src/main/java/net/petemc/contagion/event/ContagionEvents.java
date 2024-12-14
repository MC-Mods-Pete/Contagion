package net.petemc.contagion.event;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.contagion.potion.ContagionPotions;

@EventBusSubscriber (modid = Contagion.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ContagionEvents {

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, ContagionItems.GOLD_STREAKED_FLESH.get(), ContagionPotions.CURE_POTION);
        builder.addMix(ContagionPotions.CURE_POTION, Items.REDSTONE, ContagionPotions.LONG_CURE_POTION);
    }
}
