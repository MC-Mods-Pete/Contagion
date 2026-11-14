package net.petemc.contagion.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.UniformGenerator;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.undeadnights.entity.ModEntities;

public class ContagionLootTableModifiers {
    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key.equals(EntityTypes.ZOMBIE.getDefaultLootTable().orElse(null))
                    || key.equals(EntityTypes.HUSK.getDefaultLootTable().orElse(null))
                    || key.equals(EntityTypes.DROWNED.getDefaultLootTable().orElse(null))
                    || key.equals(EntityTypes.ZOMBIE_VILLAGER.getDefaultLootTable().orElse(null))
                    || key.equals(EntityTypes.ZOMBIFIED_PIGLIN.getDefaultLootTable().orElse(null))
                    || key.equals(EntityTypes.ZOGLIN.getDefaultLootTable().orElse(null))) {
                tableBuilder.withPool(buildPool(registries));
            }

            if (ModCompatibility.undeadNightsDetected()) {
                if (key.equals(ModEntities.HORDE_ZOMBIE.getDefaultLootTable().orElse(null))
                        || key.equals(ModEntities.ELITE_ZOMBIE.getDefaultLootTable().orElse(null))
                        || key.equals(ModEntities.DEMOLITION_ZOMBIE.getDefaultLootTable().orElse(null))) {
                    tableBuilder.withPool(buildPool(registries));
                }
            }
        });
    }

    private static LootPool.Builder buildPool(net.minecraft.core.HolderLookup.Provider registries) {
        HolderGetter<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);

        Holder<LootItemCondition> dropCondition = Holder.direct(
                LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(
                        enchantments,
                        (float) MainConfig.getContagiousFleshDropChance(),
                        (float) MainConfig.getContagiousFleshLootingBonus()
                ).build()
        );

        Holder<ContextIntProvider> countProvider = Holder.direct(
                new UniformGenerator(
                        Holder.direct(new ConstantValue(1)),
                        Holder.direct(new ConstantValue(2))
                )
        );

        return LootPool.lootPool()
                .setRolls(Holder.direct(new ConstantValue(1)))
                .when(dropCondition)
                .add(LootItem.lootTableItem(ContagionItems.CONTAGIOUS_FLESH))
                .apply(Holder.direct(SetItemCountFunction.setCount(countProvider).build()));
    }
}
