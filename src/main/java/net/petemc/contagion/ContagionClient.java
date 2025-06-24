package net.petemc.contagion;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.petemc.contagion.config.ContagionConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.network.NetworkPayloads;
import org.jetbrains.annotations.NotNull;

public class ContagionClient implements ClientModInitializer {
    Identifier hudArmorTexture = Identifier.of("contagion", "textures/hud/contagion_transparent16.png");
    Identifier contagionHudElement = Identifier.of("contagion", "infection_protection_hud");

    private int hudTextColor;

    private int cachedInfectionProtection = -1;
    private int infectionProtection = -1;

    private int receivedBaseInfectionChance = -1;
    private int receivedMinimumInfectionChance = -1;
    private boolean receivedArmorLowersInfectionChance = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (receivedBaseInfectionChance != -1) {
                if (client.player != null) {
                    infectionProtection = getEffectiveInfectProtection(client.player);
                    if (infectionProtection != cachedInfectionProtection) {
                        cachedInfectionProtection = infectionProtection;
                    }
                    if (!client.player.isSpectator() && ContagionConfig.INSTANCE.displayCurrentInfectionProtection) {
                        hudArmorTexture = Identifier.of("contagion", "textures/hud/contagion_armor16.png");
                        if (infectionProtection == 100) {
                            hudTextColor = 0xffd4af37;
                        } else if (infectionProtection >= 75) {
                            hudTextColor = 0xff3fc400;
                        } else if (infectionProtection < 30) {
                            hudTextColor = 0xffff5555;
                        } else {
                            hudTextColor = 0xffffffff;
                        }
                    } else {
                        hudArmorTexture = Identifier.of("contagion", "textures/hud/contagion_transparent16.png");
                        hudTextColor = 0x0;
                    }
                }
            }
        });

        HudElementRegistry.addLast(contagionHudElement, (context, tCounter) -> {
            context.drawTexture(RenderPipelines.GUI_TEXTURED, hudArmorTexture, (context.getScaledWindowWidth() / 2) - 170 + ContagionConfig.INSTANCE.deltaX, context.getScaledWindowHeight() - 19 + ContagionConfig.INSTANCE.deltaY, 0, 0, 16, 16, 16, 16);
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, infectionProtection + "%", (context.getScaledWindowWidth() / 2) + 18 - 170 + ContagionConfig.INSTANCE.deltaX, context.getScaledWindowHeight() - 14 + ContagionConfig.INSTANCE.deltaY, hudTextColor);
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkPayloads.hudDataPayload.ID, (payload, context) -> {
            receivedBaseInfectionChance = payload.baseInfectionChance();
            receivedMinimumInfectionChance = payload.minimumInfectionChance();
            receivedArmorLowersInfectionChance = payload.armorLowersInfectionChance();

            context.client().execute(() -> {
                Contagion.LOGGER.info("Data for client HUD-received: {} {} {}:", receivedBaseInfectionChance, receivedMinimumInfectionChance, receivedArmorLowersInfectionChance);
            });
        });
    }

    private int getEffectiveInfectProtection(@NotNull ClientPlayerEntity clientPlayerEntity) {
        int effectInfectProtection;
        if (receivedBaseInfectionChance > 100) {
            effectInfectProtection = 0;
        } else {
            effectInfectProtection = 100 - receivedBaseInfectionChance;
        }
        if (receivedArmorLowersInfectionChance) {
            effectInfectProtection = effectInfectProtection + (clientPlayerEntity.getArmor() * 3);
        }
        if (effectInfectProtection > (100 - receivedMinimumInfectionChance)) {
            effectInfectProtection = 100 - receivedMinimumInfectionChance;
        }
        if (clientPlayerEntity.hasStatusEffect(ContagionEffects.IMMUNITY)) {
            effectInfectProtection = 100;
        }
        return effectInfectProtection;
    }
}
