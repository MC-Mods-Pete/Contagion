package net.petemc.contagion;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.petemc.contagion.config.ContagionConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.network.NetworkPayloads;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2fStack;

public class ContagionClient implements ClientModInitializer, HudRenderCallback {
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
                        HudRenderCallback.EVENT.register(this);
                    }
                }
            }
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

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        if (ContagionConfig.INSTANCE.displayCurrentInfectionProtection) {
            MinecraftClient mcClient = MinecraftClient.getInstance();
            assert mcClient.player != null;
            if (!mcClient.player.isSpectator()) {
                TextRenderer textRenderer = mcClient.textRenderer;
                Matrix3x2fStack matrixStack = drawContext.getMatrices();

                int color = 0xffffffff;
                if (infectionProtection == 100) {
                    color = 0xffd4af37;
                } else if (infectionProtection >= 75) {
                    color = 0xff3fc400;
                } else if (infectionProtection < 30) {
                    color = 0xffff5555;
                }

                Identifier texture = Identifier.of("contagion", "textures/hud/contagion_armor16.png");

                drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, texture, (drawContext.getScaledWindowWidth() / 2) - 170 + ContagionConfig.INSTANCE.deltaX, drawContext.getScaledWindowHeight() - 19 + ContagionConfig.INSTANCE.deltaY, 0, 0, 16, 16, 16, 16);

                matrixStack.pushMatrix();
                matrixStack.translate((float) ((drawContext.getScaledWindowWidth() / 2) + 18 - 170 + ContagionConfig.INSTANCE.deltaX), drawContext.getScaledWindowHeight() - 16 + ContagionConfig.INSTANCE.deltaY, matrixStack);
                matrixStack.scale(1, 1, matrixStack);
                drawContext.drawTextWithShadow(textRenderer, infectionProtection + "%", 2, 2, color);
                matrixStack.popMatrix();
            }
        }
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
