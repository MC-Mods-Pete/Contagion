package net.petemc.contagion;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.petemc.contagion.damage_type.ContagionDamageTypes;
import net.petemc.contagion.data.DataGenerators;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.item.ContagionCreativeModeTabs;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.contagion.loot.ContagionLootModifiers;
import net.petemc.contagion.potion.ContagionPotions;
import net.petemc.contagion.sound.ContagionSounds;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Contagion.MOD_ID)
public class Contagion {
	public static final String MOD_ID = "contagion";
	public static final String MOD_NAME = "Contagion";
	public static final Logger LOGGER = LogUtils.getLogger();

	public Contagion(IEventBus modEventBus, ModContainer modContainer) {
		// Register the commonSetup method for modloading
		modEventBus.addListener(this::commonSetup);

		// Register ourselves for server and other game events we are interested in.
		// Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
		// Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
		NeoForge.EVENT_BUS.register(this);

		ContagionEffects.register(modEventBus);
		ContagionItems.register(modEventBus);
		ContagionCreativeModeTabs.register(modEventBus);
		ContagionSounds.register(modEventBus);
		ContagionDamageTypes.registerDamageTypes();
		ContagionPotions.register(modEventBus);
		ContagionLootModifiers.LOOT_MODIFIERS.register(modEventBus);

		modEventBus.addListener(DataGenerators::gatherData);

		// Register the item to a creative tab
		modEventBus.addListener(this::addCreative);
		// Register our mod's ModConfigSpec so that FML can create and load the config file for us
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	// common setup
	private void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.info("Initializing the {} Mod", MOD_NAME);
	}

	// Add the example block item to the building blocks tab
	private void addCreative(BuildCreativeModeTabContentsEvent event) {

	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {

	}

	// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
	@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
		}
	}
}
