package net.petemc.contagion.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.UniformGenerator;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.contagion.loot.AddItemModifier;
import net.petemc.contagion.util.ModCompatibility;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

// see https://github.com/Luohuayu/CatServer/blob/1c92118fcca69ffac97a48c8e1f6e1bb861b41d1/src/main/java/org/bukkit/loot/LootTables.java#L71 for some loot tables
public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Contagion.MOD_ID);
    }

    private static Holder<ContextIntProvider> uniform(int min, int max) {
        return Holder.direct(new UniformGenerator(Holder.direct(new ConstantValue(min)), Holder.direct(new ConstantValue(max))));
    }

    private static AddItemModifier addItemModifier(String lootTable, Item item) {
        return new AddItemModifier(
                Optional.of(Holder.direct(LootTableIdCondition.builder(Identifier.parse(lootTable)).build())),
                IGlobalLootModifier.DEFAULT_PRIORITY,
                item,
                List.of(Holder.direct(SetItemCountFunction.setCount(uniform(1, 2)).build())));
    }

    @Override
    protected void start() {
        add("contagious_flesh_item_from_zombie",          addItemModifier("entities/zombie",           ContagionItems.CONTAGIOUS_FLESH.get()));
        add("contagious_flesh_item_from_husk",            addItemModifier("entities/husk",             ContagionItems.CONTAGIOUS_FLESH.get()));
        add("contagious_flesh_item_from_drowned",         addItemModifier("entities/drowned",          ContagionItems.CONTAGIOUS_FLESH.get()));
        add("contagious_flesh_item_from_zombie_villager", addItemModifier("entities/zombie_villager",  ContagionItems.CONTAGIOUS_FLESH.get()));
        add("contagious_flesh_item_from_zombified_piglin",addItemModifier("entities/zombified_piglin", ContagionItems.CONTAGIOUS_FLESH.get()));
        add("contagious_flesh_item_from_zoglin",          addItemModifier("entities/zoglin",           ContagionItems.CONTAGIOUS_FLESH.get()));
        if (ModCompatibility.undeadNightsDetected()) {
            add("contagious_flesh_item_from_horde_zombie",     addItemModifier("undeadnights:entities/horde_zombie",     ContagionItems.CONTAGIOUS_FLESH.get()));
            add("contagious_flesh_item_from_elite_zombie",     addItemModifier("undeadnights:entities/elite_zombie",     ContagionItems.CONTAGIOUS_FLESH.get()));
            add("contagious_flesh_item_from_demolition_zombie",addItemModifier("undeadnights:entities/demolition_zombie",ContagionItems.CONTAGIOUS_FLESH.get()));
        }
    }
}
