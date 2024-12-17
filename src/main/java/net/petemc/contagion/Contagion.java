package net.petemc.contagion;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.petemc.contagion.config.ContagionConfig;
import net.petemc.contagion.damage_type.ContagionDamageTypes;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.potion.ContagionPotions;
import net.petemc.contagion.item.ContagionItemGroups;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.contagion.sound.ContagionSounds;
import net.petemc.contagion.util.ContagionLootTableModifiers;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Contagion implements ModInitializer, HudRenderCallback {
	public static final String MOD_ID = "contagion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private int cachedInfectionProtection = -1;
	private int infectionProtection = -1;

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

		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				infectionProtection = getEffectiveInfectProtection(client.player);
				if (infectionProtection != cachedInfectionProtection) {
					cachedInfectionProtection = infectionProtection;
					MinecraftClient.getInstance().getFramebuffer().beginWrite(false);
					HudRenderCallback.EVENT.register(this);
				}
			}
		});
	}

	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta) {
		if (ContagionConfig.INSTANCE.displayCurrentInfectionProtection) {
			TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
			MatrixStack matrixStack = drawContext.getMatrices();

			int color = 0xffffff;
			if (infectionProtection == 100) {
				color = 0xd4af37;
			} else if (infectionProtection >= 75) {
				color = 0x3fc400;
			} else if (infectionProtection < 30) {
				color = 0xe00000;
			}

			Identifier texture = new Identifier("contagion", "textures/hud/contagion_armor16.png");

			drawContext.drawTexture(texture, (drawContext.getScaledWindowWidth()/2) - 170 + ContagionConfig.INSTANCE.deltaX, drawContext.getScaledWindowHeight()-19 + ContagionConfig.INSTANCE.deltaY, 0, 0, 16, 16, 16, 16);
			matrixStack.push();
			matrixStack.translate((float)((drawContext.getScaledWindowWidth()/2)+18 - 170 + ContagionConfig.INSTANCE.deltaX), drawContext.getScaledWindowHeight()-16 + ContagionConfig.INSTANCE.deltaY, 0);
			matrixStack.scale(1, 1, 2.5f);
			drawContext.drawTextWithShadow(textRenderer, infectionProtection + "%", 2, 2, color);
			matrixStack.pop();
		}
	}

	private static int getEffectiveInfectProtection(@NotNull ClientPlayerEntity clientPlayerEntity) {
		int effectInfectProtection;
		if (ContagionConfig.INSTANCE.baseInfectionChance > 100) {
			effectInfectProtection = 0;
		} else {
			effectInfectProtection = 100 - ContagionConfig.INSTANCE.baseInfectionChance;
		}
		if (ContagionConfig.INSTANCE.armorLowersInfectionChance) {
			effectInfectProtection = effectInfectProtection + (clientPlayerEntity.getArmor() * 3);
		}
		if (effectInfectProtection > (100 - ContagionConfig.INSTANCE.minimumInfectionChance)) {
			effectInfectProtection = 100 - ContagionConfig.INSTANCE.minimumInfectionChance;
		}
		if (clientPlayerEntity.hasStatusEffect(ContagionEffects.IMMUNITY)) {
			effectInfectProtection = 100;
		}
		return effectInfectProtection;
	}
}