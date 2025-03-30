package net.petemc.contagion.event;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.petemc.contagion.Config;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.client.ProtectionHudOverlay;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.contagion.network.ProtectionHUDInfoNetworkPayload;
import net.petemc.contagion.potion.ContagionPotions;
import net.petemc.contagion.util.ModCompatibility;
import net.petemc.undeadnights.entity.ModEntities;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber (modid = Contagion.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ContagionEvents {
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                PacketDistributor.sendToPlayer(player, new ProtectionHUDInfoNetworkPayload(Config.baseInfectionChance, Config.minimumInfectionChance, Config.armorLowersInfectionChance));
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, ContagionItems.GOLD_STREAKED_FLESH.get(), ContagionPotions.CURE_POTION);
        builder.addMix(ContagionPotions.CURE_POTION, Items.REDSTONE, ContagionPotions.LONG_CURE_POTION);
    }


    @SubscribeEvent
    public static void test(LootTableLoadEvent event) {
        if (ModCompatibility.undeadNightsDetected()) {
            if (ModEntities.HORDE_ZOMBIE.get().getDefaultLootTable().isPresent()) {
                if (ModEntities.HORDE_ZOMBIE.get().getDefaultLootTable().get() == event.getKey()) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(event.getRegistries(), 0.075f, 0.025f))
                            .add(LootItem.lootTableItem(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)));
                    event.setTable(LootTable.lootTable().withPool(poolBuilder).build());
                }
            }

            if (ModEntities.ELITE_ZOMBIE.get().getDefaultLootTable().isPresent()) {
                if (ModEntities.ELITE_ZOMBIE.get().getDefaultLootTable().get() == event.getKey()) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(event.getRegistries(), 0.075f, 0.025f))
                            .add(LootItem.lootTableItem(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)));
                    event.setTable(LootTable.lootTable().withPool(poolBuilder).build());
                }
            }

            if (ModEntities.DEMOLITION_ZOMBIE.get().getDefaultLootTable().isPresent()) {
                if (ModEntities.DEMOLITION_ZOMBIE.get().getDefaultLootTable().get() == event.getKey()) {
                    LootPool.Builder poolBuilder = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(event.getRegistries(), 0.075f, 0.025f))
                            .add(LootItem.lootTableItem(ContagionItems.CONTAGIOUS_FLESH))
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)));
                    event.setTable(LootTable.lootTable().withPool(poolBuilder).build());
                }
            }
        }
    }
}
