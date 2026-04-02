package net.petemc.contagion;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.network.NetworkPayloads;
import org.jspecify.annotations.Nullable;

public class ContagionClient implements ClientModInitializer {
    Identifier hudArmorTexture = Identifier.fromNamespaceAndPath(Contagion.MOD_ID, "textures/hud/contagion_transparent16.png");
    Identifier contagionHudElement = Identifier.fromNamespaceAndPath(Contagion.MOD_ID, "infection_protection_hud");

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
                    if (!client.player.isSpectator() && MainConfig.isDisplayCurrentInfectionProtection()) {
                        hudArmorTexture = Identifier.fromNamespaceAndPath(Contagion.MOD_ID, "textures/hud/contagion_armor16.png");
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
                        hudArmorTexture = Identifier.fromNamespaceAndPath(Contagion.MOD_ID, "textures/hud/contagion_transparent16.png");
                        hudTextColor = 0x0;
                    }
                }
            }
        });

        HudElementRegistry.addLast(contagionHudElement, (context, tCounter) -> {
            context.blit(RenderPipelines.GUI_TEXTURED, hudArmorTexture, (context.guiWidth() / 2) - 170 + MainConfig.getDeltaX(), context.guiHeight() - 19 + MainConfig.getDeltaY(), 0f, 0f, 16, 16, 16, 16);
            context.text(Minecraft.getInstance().font, infectionProtection + "%", (context.guiWidth() / 2) + 18 - 170 + MainConfig.getDeltaX(), context.guiHeight() - 14 + MainConfig.getDeltaY(), hudTextColor, true);
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkPayloads.hudDataPayload.TYPE, (payload, context) -> {
            receivedBaseInfectionChance = payload.baseInfectionChance();
            receivedMinimumInfectionChance = payload.minimumInfectionChance();
            receivedArmorLowersInfectionChance = payload.armorLowersInfectionChance();

            context.client().execute(() -> {
                Contagion.LOGGER.info("Data for client HUD-received: {} {} {}:", receivedBaseInfectionChance, receivedMinimumInfectionChance, receivedArmorLowersInfectionChance);
            });
        });
    }

    private int getEffectiveInfectProtection(@Nullable LocalPlayer clientPlayerEntity) {
        int effectInfectProtection;
        if (receivedBaseInfectionChance > 100) {
            effectInfectProtection = 0;
        } else {
            effectInfectProtection = 100 - receivedBaseInfectionChance;
        }
        if (receivedArmorLowersInfectionChance) {
            effectInfectProtection = effectInfectProtection + (clientPlayerEntity.getArmorValue() * 3);
        }
        if (effectInfectProtection > (100 - receivedMinimumInfectionChance)) {
            effectInfectProtection = 100 - receivedMinimumInfectionChance;
        }
        if (clientPlayerEntity.hasEffect(ContagionEffects.IMMUNITY)) {
            effectInfectProtection = 100;
        }
        return effectInfectProtection;
    }
}
