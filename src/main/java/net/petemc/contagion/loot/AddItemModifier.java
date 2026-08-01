package net.petemc.contagion.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.petemc.contagion.config.MainConfig;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    public static final Supplier<MapCodec<AddItemModifier>> CODEC_SUPPLIER =
            Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(instance -> AddItemModifier.codecStart(instance)
            .and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(addItemModifierInstance -> addItemModifierInstance.item))
            .and(LootItemFunctions.CODEC.listOf().optionalFieldOf("functions", List.of()).forGetter(addItemModifierInstance -> addItemModifierInstance.functions))
            .apply(instance, AddItemModifier::new))
   );

    private final Item item;
    private final List<Holder<LootItemFunction>> functions;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, List<Holder<LootItemFunction>> functions) {
        super(conditionsIn);
        this.item = item;
        this.functions = functions;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        double baseChance = MainConfig.getContagiousFleshDropChance();
        double lootingBonus = MainConfig.getContagiousFleshLootingBonus();
        int lootingLevel = Math.max(0, getLootingLevel(context));
        double finalChance = Mth.clamp(baseChance + (lootingBonus * lootingLevel), 0.0, 1.0);

        if (context.getRandom().nextDouble() >= finalChance) {
            return generatedLoot;
        }

        ItemStack itemToAdd = new ItemStack(item);

        if (!functions.isEmpty()) {
            for (Holder<LootItemFunction> functionHolder : functions) {
                LootItemFunction function = functionHolder.value();
                itemToAdd = function.apply(itemToAdd, context);
            }
        }

        generatedLoot.add(itemToAdd);
        return generatedLoot;
    }

    private static int getLootingLevel(LootContext context) {
        if (!(context.getParamOrNull(LootContextParams.ATTACKING_ENTITY) instanceof LivingEntity attackingEntity)) {
            return 0;
        }

        return EnchantmentHelper.getItemEnchantmentLevel(
                context.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING),
                attackingEntity.getWeaponItem()
        );
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC_SUPPLIER.get();
    }
}