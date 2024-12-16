package net.petemc.contagion.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.contagion.loot.AddItemModifier;

// see https://github.com/Luohuayu/CatServer/blob/1c92118fcca69ffac97a48c8e1f6e1bb861b41d1/src/main/java/org/bukkit/loot/LootTables.java#L71 for some loot tables
public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, Contagion.MOD_ID);
    }

    @Override
    protected void start() {
        add("contagious_flesh_item_from_zombie", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/zombie")).build() ,
                LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.075f, 0.025f).build()},
                ContagionItems.CONTAGIOUS_FLESH.get(),
                1, 2));
        add("contagious_flesh_item_from_husk", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/husk")).build() ,
                LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.075f, 0.025f).build()},
                ContagionItems.CONTAGIOUS_FLESH.get(),
                1, 2));
        add("contagious_flesh_item_from_drowned", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/drowned")).build() ,
                LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.075f, 0.025f).build()},
                ContagionItems.CONTAGIOUS_FLESH.get(),
                1, 2));
        add("contagious_flesh_item_from_zombie_villager", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/zombie_villager")).build() ,
                LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.075f, 0.025f).build()},
                ContagionItems.CONTAGIOUS_FLESH.get(),
                1, 2));
        add("contagious_flesh_item_from_zombified_piglin", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/zombified_piglin")).build() ,
                LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.075f, 0.025f).build()},
                ContagionItems.CONTAGIOUS_FLESH.get(),
                1, 2));
        add("contagious_flesh_item_from_zoglin", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("entities/zoglin")).build() ,
                LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.075f, 0.025f).build()},
                ContagionItems.CONTAGIOUS_FLESH.get(),
                1, 2));
    }
}

