package net.petemc.contagion.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceWithEnchantedBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.undeadnights.entity.ModEntities;

public class ContagionLootTableModifiers {
    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((id, tableBuilder, source, registries) -> {
            if (EntityType.ZOMBIE.getLootTableId() == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, (float) MainConfig.getContagiousFleshDropChance(), (float) MainConfig.getContagiousFleshLootingBonus()))
                        .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }

            if (EntityType.HUSK.getLootTableId() == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, (float) MainConfig.getContagiousFleshDropChance(), (float) MainConfig.getContagiousFleshLootingBonus()))
                        .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }

            if (EntityType.DROWNED.getLootTableId() == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, (float) MainConfig.getContagiousFleshDropChance(), (float) MainConfig.getContagiousFleshLootingBonus()))
                        .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }

            if (EntityType.ZOMBIE_VILLAGER.getLootTableId() == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, (float) MainConfig.getContagiousFleshDropChance(), (float) MainConfig.getContagiousFleshLootingBonus()))
                        .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }

            if (EntityType.ZOMBIFIED_PIGLIN.getLootTableId() == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, (float) MainConfig.getContagiousFleshDropChance(), (float) MainConfig.getContagiousFleshLootingBonus()))
                        .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }

            if (EntityType.ZOGLIN.getLootTableId() == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, (float) MainConfig.getContagiousFleshDropChance(), (float) MainConfig.getContagiousFleshLootingBonus()))
                        .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }

            if (ModCompatibility.undeadNightsDetected()) {
                if (ModEntities.HORDE_ZOMBIE.getLootTableId() == id) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, (float) MainConfig.getContagiousFleshDropChance(), (float) MainConfig.getContagiousFleshLootingBonus()))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());

                    tableBuilder.pool(poolBuilder.build());
                }

                if (ModEntities.ELITE_ZOMBIE.getLootTableId() == id) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, (float) MainConfig.getContagiousFleshDropChance(), (float) MainConfig.getContagiousFleshLootingBonus()))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());

                    tableBuilder.pool(poolBuilder.build());
                }

                if (ModEntities.DEMOLITION_ZOMBIE.getLootTableId() == id) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, (float) MainConfig.getContagiousFleshDropChance(), (float) MainConfig.getContagiousFleshLootingBonus()))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());

                    tableBuilder.pool(poolBuilder.build());
                }
            }
        });
    }
}



