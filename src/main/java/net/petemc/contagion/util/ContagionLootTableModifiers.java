package net.petemc.contagion.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceWithEnchantedBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.petemc.contagion.item.ContagionItems;

public class ContagionLootTableModifiers {
    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (EntityType.ZOMBIE.getLootTableKey().isPresent()) {
                if (EntityType.ZOMBIE.getLootTableKey().get() == key && source.isBuiltin()) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    tableBuilder.pool(poolBuilder);
                }
            }

            if (EntityType.HUSK.getLootTableKey().isPresent()) {
                if (EntityType.HUSK.getLootTableKey().get() == key && source.isBuiltin()) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    tableBuilder.pool(poolBuilder);
                }
            }

            if (EntityType.DROWNED.getLootTableKey().isPresent()) {
                if (EntityType.DROWNED.getLootTableKey().get() == key && source.isBuiltin()) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    tableBuilder.pool(poolBuilder);
                }
            }

            if (EntityType.ZOMBIE_VILLAGER.getLootTableKey().isPresent()) {
                if (EntityType.ZOMBIE_VILLAGER.getLootTableKey().get() == key && source.isBuiltin()) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    tableBuilder.pool(poolBuilder);
                }
            }

            if (EntityType.ZOMBIFIED_PIGLIN.getLootTableKey().isPresent()) {
                if (EntityType.ZOMBIFIED_PIGLIN.getLootTableKey().get() == key && source.isBuiltin()) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    tableBuilder.pool(poolBuilder);
                }
            }

            if (EntityType.ZOGLIN.getLootTableKey().isPresent()) {
                if (EntityType.ZOGLIN.getLootTableKey().get() == key && source.isBuiltin()) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    tableBuilder.pool(poolBuilder);
                }
            }
        });
    }
}



