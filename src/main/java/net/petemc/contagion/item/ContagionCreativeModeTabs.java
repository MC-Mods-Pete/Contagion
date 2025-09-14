package net.petemc.contagion.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ContagionCreativeModeTabs extends CreativeModeTab {

    public static final ContagionCreativeModeTabs CONTAGION_CREATIVE_MODE_TAB = new ContagionCreativeModeTabs(CreativeModeTab.TABS.length, "contagion");

    public ContagionCreativeModeTabs(int length, String label) {
        super(length, label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(ContagionItems.CONTAGIOUS_FLESH.getHolder().orElseThrow());
    }
}
