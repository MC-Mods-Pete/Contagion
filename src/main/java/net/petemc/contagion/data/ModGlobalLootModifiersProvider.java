package net.petemc.contagion.data;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.contagion.loot.AddItemModifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// see https://github.com/Luohuayu/CatServer/blob/1c92118fcca69ffac97a48c8e1f6e1bb861b41d1/src/main/java/org/bukkit/loot/LootTables.java#L71 for some loot tables
public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Contagion.MOD_ID);
    }

    @Override
    protected void start() {
        add("contagious_flesh_item_from_zombie", new AddItemModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(Identifier.parse("entities/zombie")).build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries,0.075F, 0.025F).build()},
                ContagionItems.CONTAGIOUS_FLESH.get(),
                List.of(Holder.direct(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)).build())))
                );
        add("contagious_flesh_item_from_husk", new AddItemModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(Identifier.parse("entities/husk")).build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries,0.075F, 0.025F).build()},
                ContagionItems.CONTAGIOUS_FLESH.get(),
                List.of(Holder.direct(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)).build())))
        );
        add("contagious_flesh_item_from_drowned", new AddItemModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(Identifier.parse("entities/drowned")).build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries,0.075F, 0.025F).build()},
                ContagionItems.CONTAGIOUS_FLESH.get(),
                List.of(Holder.direct(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)).build())))
        );
        add("contagious_flesh_item_from_zombie_villager", new AddItemModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(Identifier.parse("entities/zombie_villager")).build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries,0.075F, 0.025F).build()},
                ContagionItems.CONTAGIOUS_FLESH.get(),
                List.of(Holder.direct(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)).build())))
        );
        add("contagious_flesh_item_from_zombified_piglin", new AddItemModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(Identifier.parse("entities/zombified_piglin")).build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries,0.075F, 0.025F).build()},
                ContagionItems.CONTAGIOUS_FLESH.get(),
                List.of(Holder.direct(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)).build())))
        );
        add("contagious_flesh_item_from_zoglin", new AddItemModifier(new LootItemCondition[]{
                LootTableIdCondition.builder(Identifier.parse("entities/zoglin")).build(),
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries,0.075F, 0.025F).build()},
                ContagionItems.CONTAGIOUS_FLESH.get(),
                List.of(Holder.direct(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)).build())))
        );
    }
}
