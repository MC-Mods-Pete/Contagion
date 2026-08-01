package net.petemc.contagion.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.undeadnights.UndeadNights;
import net.petemc.undeadnights.entity.ModEntities;

public class ContagionLootTableModifiers {
    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (EntityTypes.ZOMBIE.getDefaultLootTable().isPresent()) {
                if (EntityTypes.ZOMBIE.getDefaultLootTable().get() == key) {
                    tableBuilder.withPool(buildPool(registries));
                }
            }
            if (EntityTypes.HUSK.getDefaultLootTable().isPresent()) {
                if (EntityTypes.HUSK.getDefaultLootTable().get() == key) {
                    tableBuilder.withPool(buildPool(registries));
                }
            }
            if (EntityTypes.DROWNED.getDefaultLootTable().isPresent()) {
                if (EntityTypes.DROWNED.getDefaultLootTable().get() == key) {
                    tableBuilder.withPool(buildPool(registries));
                }
            }
            if (EntityTypes.ZOMBIE_VILLAGER.getDefaultLootTable().isPresent()) {
                if (EntityTypes.ZOMBIE_VILLAGER.getDefaultLootTable().get() == key) {
                    tableBuilder.withPool(buildPool(registries));
                }
            }
            if (EntityTypes.ZOMBIFIED_PIGLIN.getDefaultLootTable().isPresent()) {
                if (EntityTypes.ZOMBIFIED_PIGLIN.getDefaultLootTable().get() == key) {
                    tableBuilder.withPool(buildPool(registries));
                }
            }
            if (EntityTypes.ZOGLIN.getDefaultLootTable().isPresent()) {
                if (EntityTypes.ZOGLIN.getDefaultLootTable().get() == key) {
                    tableBuilder.withPool(buildPool(registries));
                }
            }

            if (ModCompatibility.undeadNightsDetected()) {
                if (ModEntities.HORDE_ZOMBIE.getDefaultLootTable().isPresent()) {
                    if (ModEntities.HORDE_ZOMBIE.getDefaultLootTable().get() == key) {
                        tableBuilder.withPool(buildPool(registries));
                    }
                }

                if (ModEntities.ELITE_ZOMBIE.getDefaultLootTable().isPresent()) {
                    if (ModEntities.ELITE_ZOMBIE.getDefaultLootTable().get() == key) {
                        tableBuilder.withPool(buildPool(registries));
                    }
                }

                if (ModEntities.DEMOLITION_ZOMBIE.getDefaultLootTable().isPresent()) {
                    if (ModEntities.DEMOLITION_ZOMBIE.getDefaultLootTable().get() == key) {
                        tableBuilder.withPool(buildPool(registries));
                    }
                }
            }
        });
    }

    private static LootPool.Builder buildPool(net.minecraft.core.HolderLookup.Provider registries) {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, (float) MainConfig.getContagiousFleshDropChance(), (float) MainConfig.getContagiousFleshLootingBonus()))
                .add(LootItem.lootTableItem(ContagionItems.CONTAGIOUS_FLESH))
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)));
    }
}
