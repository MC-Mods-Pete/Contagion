package net.petemc.contagion.data;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.petemc.contagion.Contagion;

@Mod.EventBusSubscriber(modid = Contagion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        try {
            DataGenerator generator = event.getGenerator();
            //PackOutput packOutput = generator.getPackOutput();
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
            //CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
            /*
            generator.addProvider(true, new ModEnLangProvider(output));
            generator.addProvider(true, new ModItemStateProvider(output, existingFileHelper));
            generator.addProvider(true, new ModBlockStateProvider(output, existingFileHelper));
            ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(output, event.getLookupProvider(), existingFileHelper);
            generator.addProvider(true, modBlockTagsProvider);
            generator.addProvider(true, new ModItemTagProvider(output, event.getLookupProvider(), modBlockTagsProvider, existingFileHelper));
            generator.addProvider(true, new ModLootTables(output, event.getLookupProvider()));
            generator.addProvider(true, new ModWorldGenProvider(output, event.getLookupProvider()));
            generator.addProvider(true, new MainModRecipeProvider(generator, event.getLookupProvider()));
            */
            generator.addProvider(event.includeServer(), new ModGlobalLootModifiersProvider(generator));
        } catch (RuntimeException e) {
            Contagion.LOGGER.error("Failed to gather data", e);
        }
    }
}
