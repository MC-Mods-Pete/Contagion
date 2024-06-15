package net.petemc.contagion.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.potion.ContagionPotions;

public class ContagionItemGroups {
    public static final ItemGroup CONTAGION_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Contagion.MOD_ID, "contagion"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.contagion"))
                    .icon(() -> new ItemStack(ContagionItems.CONTAGIOUS_FLESH)).entries((displayContext, entries) -> {
                        entries.add(ContagionItems.CONTAGIOUS_FLESH);
                        entries.add(ContagionItems.HEAT_TREATED_FLESH);
                        entries.add(ContagionItems.GOLD_STREAKED_FLESH);
                        entries.add(PotionContentsComponent.createStack(Items.POTION, ContagionPotions.CURE_POTION));
                        entries.add(PotionContentsComponent.createStack(Items.LINGERING_POTION, ContagionPotions.CURE_POTION));
                        entries.add(PotionContentsComponent.createStack(Items.SPLASH_POTION, ContagionPotions.CURE_POTION));
                        entries.add(PotionContentsComponent.createStack(Items.POTION, ContagionPotions.LONG_CURE_POTION));
                        entries.add(PotionContentsComponent.createStack(Items.LINGERING_POTION, ContagionPotions.LONG_CURE_POTION));
                        entries.add(PotionContentsComponent.createStack(Items.SPLASH_POTION, ContagionPotions.LONG_CURE_POTION));
                    }).build());

    public static void registerItemGroups() {
        Contagion.LOGGER.info("Registering Item Groups for " + Contagion.MOD_ID);
    }
}
