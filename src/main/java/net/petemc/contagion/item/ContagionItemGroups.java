package net.petemc.contagion.item;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.potion.ContagionPotions;

public class ContagionItemGroups {
    public static final CreativeModeTab CONTAGION_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(Contagion.MOD_ID, "contagion"),
            FabricCreativeModeTab.builder().title(Component.translatable("itemgroup.contagion"))
                    .icon(() -> new ItemStack(ContagionItems.CONTAGIOUS_FLESH)).displayItems((displayContext, entries) -> {
                        entries.accept(ContagionItems.CONTAGIOUS_FLESH);
                        entries.accept(ContagionItems.HEAT_TREATED_FLESH);
                        entries.accept(ContagionItems.GOLD_STREAKED_FLESH);
                        entries.accept(PotionContents.createItemStack(Items.POTION, ContagionPotions.CURE_POTION));
                        entries.accept(PotionContents.createItemStack(Items.LINGERING_POTION, ContagionPotions.CURE_POTION));
                        entries.accept(PotionContents.createItemStack(Items.SPLASH_POTION, ContagionPotions.CURE_POTION));
                        entries.accept(PotionContents.createItemStack(Items.POTION, ContagionPotions.LONG_CURE_POTION));
                        entries.accept(PotionContents.createItemStack(Items.LINGERING_POTION, ContagionPotions.LONG_CURE_POTION));
                        entries.accept(PotionContents.createItemStack(Items.SPLASH_POTION, ContagionPotions.LONG_CURE_POTION));
                    }).build());

    public static void registerItemGroups() {
        Contagion.LOGGER.info("Registering Item Groups for " + Contagion.MOD_ID);
    }
}
