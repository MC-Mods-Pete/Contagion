package net.petemc.contagion.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.Contagion;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {

    public static final Supplier<Codec<AddItemModifier>> CODEC_SUPPLIER =
            Suppliers.memoize(() -> RecordCodecBuilder.create(instance -> AddItemModifier.codecStart(instance)
            .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(addItemModifierInstance -> addItemModifierInstance.item))
            .and(Codec.INT.optionalFieldOf("min_count", -1).forGetter(addItemModifierInstance -> addItemModifierInstance.minCount))
            .and(Codec.INT.optionalFieldOf("max_count", -1).forGetter(addItemModifierInstance -> addItemModifierInstance.maxCount))
            .apply(instance, AddItemModifier::new))
   );

    private final Item item;
    private final int minCount;
    private final int maxCount;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item, int minCount, int maxCount) {
        super(conditionsIn);
        this.item = item;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        RandomSource random = context.getRandom();
        int itemCount;

        if (minCount >= 0 && maxCount >= 0) {
            itemCount = random.nextIntBetweenInclusive(minCount, maxCount);

        } else {
            // Fallback to a default value if neither count nor range is specified
            itemCount = 1;
        }

        // Calculate drop chance using config values
        float dropChance = (float) MainConfig.getContagiousFleshDropChance();
        int lootingLevel = context.getLootingModifier();
        float lootingBonus = (float) MainConfig.getContagiousFleshLootingBonus() * lootingLevel;
        float finalChance = Math.max(0.0f, Math.min(1.0f, dropChance + lootingBonus));
        float roll = random.nextFloat();
        boolean dropped = roll < finalChance;

        // Check if the item should drop
        if (dropped) {
            ItemStack itemToAdd = new ItemStack(item, itemCount);
            generatedLoot.add(itemToAdd);
        }

        if (MainConfig.isEnableDebugMessages()) {
            Contagion.LOGGER.info(
                    "Contagious Flesh roll: roll={} finalChance={} baseChance={} lootingBonus={} lootingLevel={} dropped={} amount={}",
                    roll,
                    finalChance,
                    dropChance,
                    lootingBonus,
                    lootingLevel,
                    dropped,
                    dropped ? itemCount : 0
            );
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC_SUPPLIER.get();
    }
}
