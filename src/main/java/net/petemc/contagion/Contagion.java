package net.petemc.contagion;

import net.fabricmc.api.ModInitializer;

import net.petemc.contagion.config.ContagionConfig;
import net.petemc.contagion.damage_type.ContagionDamageTypes;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.potion.ContagionPotions;
import net.petemc.contagion.item.ContagionItemGroups;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.contagion.sound.ContagionSounds;
import net.petemc.contagion.util.ContagionLootTableModifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Contagion implements ModInitializer {
	public static final String MOD_ID = "contagion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Contagion Mod");
		ContagionConfig.init();
		ContagionEffects.registerEffects();
		ContagionItems.registerItems();
		ContagionItemGroups.registerItemGroups();
		ContagionSounds.registerSounds();
		ContagionDamageTypes.registerDamageTypes();
		ContagionLootTableModifiers.modifyLootTables();
		ContagionPotions.registerPotions();
	}
}