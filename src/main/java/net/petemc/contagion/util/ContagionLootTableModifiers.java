package net.petemc.contagion.util;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.petemc.contagion.item.ContagionItems;

public class ContagionLootTableModifiers {
    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((id, tableBuilder, source) -> {
            if (EntityType.ZOMBIE.getLootTableId() == id && source.isBuiltin()) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.075f, 0.025f))
                        .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder);
            }

            if (EntityType.HUSK.getLootTableId() == id && source.isBuiltin()) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.075f, 0.025f))
                        .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder);
            }

            if (EntityType.DROWNED.getLootTableId() == id && source.isBuiltin()) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.075f, 0.025f))
                        .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder);
            }
            if (EntityType.ZOMBIE_VILLAGER.getLootTableId() == id && source.isBuiltin()) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.075f, 0.025f))
                        .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder);
            }
            if (EntityType.ZOMBIFIED_PIGLIN.getLootTableId() == id && source.isBuiltin()) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.075f, 0.025f))
                        .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder);
            }
            if (EntityType.ZOGLIN.getLootTableId() == id && source.isBuiltin()) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.075f, 0.025f))
                        .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder);
            }
        });
    }
}



