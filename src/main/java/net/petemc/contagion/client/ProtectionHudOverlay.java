package net.petemc.contagion.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.Contagion;
import org.jetbrains.annotations.NotNull;

public class ProtectionHudOverlay implements LayeredDraw.Layer {
    public static ProtectionHudOverlay HUD_INSTANCE;

    private static final ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Contagion.MOD_ID, "textures/hud/contagion_armor16.png");

    public static int infectionProtection = -1;
    public static int cachedInfectionProtection = -1;

    public static int receivedBaseInfectionChance = -1;
    public static int receivedMinimumInfectionChance = -1;
    public static boolean receivedArmorLowersInfectionChance = false;

    public static void init() {
        HUD_INSTANCE = new ProtectionHudOverlay();
    }

    public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int color = 0xffffff;
        if (infectionProtection == 100) {
            color = 0xd4af37;
        } else if (infectionProtection >= 75) {
            color = 0x3fc400;
        } else if (infectionProtection < 30) {
            color = 0xff5555;
        }

        if ((MainConfig.isDisplayCurrentInfectionProtection()) && (receivedBaseInfectionChance != -1)) {
            assert mc.gameMode != null;
            if (mc.gameMode.hasExperience() || mc.gameMode.hasInfiniteItems()) {
                // Image
                guiGraphics.blit(texture, (screenWidth / 2) - 170 + MainConfig.getDeltaX(), screenHeight - 19 + MainConfig.getDeltaY(), 0, 0, 16, 16, 16, 16);
                // Text
                guiGraphics.pose().pushPose();
                guiGraphics.pose().scale(1F, 1F, 2.5f);
                guiGraphics.drawString(mc.font, infectionProtection + "%", (screenWidth / 2) + 18 - 170 + MainConfig.getDeltaX(), screenHeight - 14 + MainConfig.getDeltaY(), color);
                guiGraphics.pose().popPose();
            }
        }
    }
}
