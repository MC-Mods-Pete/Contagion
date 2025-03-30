package net.petemc.contagion.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceWithEnchantedBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.undeadnights.entity.ModEntities;

public class ContagionLootTableModifiers {
    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (EntityType.ZOMBIE.getLootTableKey().isPresent()) {
                if (EntityType.ZOMBIE.getLootTableKey().get() == key) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
            }

            if (EntityType.HUSK.getLootTableKey().isPresent()) {
                if (EntityType.HUSK.getLootTableKey().get() == key) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
            }

            if (EntityType.DROWNED.getLootTableKey().isPresent()) {
                if (EntityType.DROWNED.getLootTableKey().get() == key) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
            }

            if (EntityType.ZOMBIE_VILLAGER.getLootTableKey().isPresent()) {
                if (EntityType.ZOMBIE_VILLAGER.getLootTableKey().get() == key) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
            }

            if (EntityType.ZOMBIFIED_PIGLIN.getLootTableKey().isPresent()) {
                if (EntityType.ZOMBIFIED_PIGLIN.getLootTableKey().get() == key) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
            }

            if (EntityType.ZOGLIN.getLootTableKey().isPresent()) {
                if (EntityType.ZOGLIN.getLootTableKey().get() == key) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                            .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                    tableBuilder.pool(poolBuilder.build());
                }
            }

            if (ModCompatibility.undeadNightsDetected()) {
                String mobId = "undeadnights:horde_zombie";
                EntityType<?> mobType = Registries.ENTITY_TYPE.get(Identifier.of(mobId));
                if (mobId.contains(mobType.getUntranslatedName())) {
                    if (ModEntities.HORDE_ZOMBIE.getLootTableKey().isPresent()) {
                        if (ModEntities.HORDE_ZOMBIE.getLootTableKey().get() == key) {
                            LootPool.Builder poolBuilder = LootPool.builder()
                                    .rolls(ConstantLootNumberProvider.create(1))
                                    .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                                    .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                            tableBuilder.pool(poolBuilder.build());
                        }
                    }
                }

                mobId = "undeadnights:elite_zombie";
                mobType = Registries.ENTITY_TYPE.get(Identifier.of(mobId));
                if (mobId.contains(mobType.getUntranslatedName())) {
                    if (ModEntities.ELITE_ZOMBIE.getLootTableKey().isPresent()) {
                        if (ModEntities.ELITE_ZOMBIE.getLootTableKey().get() == key) {
                            LootPool.Builder poolBuilder = LootPool.builder()
                                    .rolls(ConstantLootNumberProvider.create(1))
                                    .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                                    .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                            tableBuilder.pool(poolBuilder.build());
                        }
                    }
                }

                mobId = "undeadnights:demolition_zombie";
                mobType = Registries.ENTITY_TYPE.get(Identifier.of(mobId));
                if (mobId.contains(mobType.getUntranslatedName())) {
                    if (ModEntities.DEMOLITION_ZOMBIE.getLootTableKey().isPresent()) {
                        if (ModEntities.DEMOLITION_ZOMBIE.getLootTableKey().get() == key) {
                            LootPool.Builder poolBuilder = LootPool.builder()
                                    .rolls(ConstantLootNumberProvider.create(1))
                                    .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(registries, 0.075f, 0.025f))
                                    .with(ItemEntry.builder(ContagionItems.CONTAGIOUS_FLESH))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)).build());
                            tableBuilder.pool(poolBuilder.build());
                        }
                    }
                }
            }
        });
    }
}



