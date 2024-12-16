package net.petemc.contagion.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.potion.ContagionPotions;

import java.util.function.Supplier;

public class ContagionCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Contagion.MOD_ID);

    public static final Supplier<CreativeModeTab> CONTAGION_ITEMS_TAB = CREATIVE_MODE_TAB.register("contagion_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ContagionItems.CONTAGIOUS_FLESH.get()))
                    .title(Component.translatable("itemgroup.contagion"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ContagionItems.CONTAGIOUS_FLESH.get());
                        output.accept(ContagionItems.HEAT_TREATED_FLESH.get());
                        output.accept(ContagionItems.GOLD_STREAKED_FLESH.get());
                        output.accept(PotionUtils.setPotion(new ItemStack(Items.POTION), ContagionPotions.CURE_POTION.get()));
                        output.accept(PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), ContagionPotions.CURE_POTION.get()));
                        output.accept(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), ContagionPotions.CURE_POTION.get()));
                        output.accept(PotionUtils.setPotion(new ItemStack(Items.POTION), ContagionPotions.LONG_CURE_POTION.get()));
                        output.accept(PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), ContagionPotions.LONG_CURE_POTION.get()));
                        output.accept(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), ContagionPotions.LONG_CURE_POTION.get()));
                    }).build());


    public static void register(IEventBus eventBus) {
        Contagion.LOGGER.info("Registering Item Groups for " + Contagion.MOD_ID);
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
