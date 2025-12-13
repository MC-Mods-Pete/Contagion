package net.petemc.contagion.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.GameType;
import net.petemc.contagion.Config;
import net.petemc.contagion.Contagion;
import org.jetbrains.annotations.NotNull;

public class ProtectionHudOverlay {
    public static ProtectionHudOverlay HUD_INSTANCE;

    private static final Identifier texture = Identifier.fromNamespaceAndPath(Contagion.MOD_ID, "textures/hud/contagion_armor16.png");

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

        int color = 0xffffffff;
        if (infectionProtection == 100) {
            color = 0xffd4af37;
        } else if (infectionProtection >= 75) {
            color = 0xff3fc400;
        } else if (infectionProtection < 30) {
            color = 0xffFF5555;
        }

        if ((Config.displayCurrentInfectionProtection) && (receivedBaseInfectionChance != -1)) {
            assert mc.gameMode != null;
            if (mc.gameMode.getPlayerMode() == GameType.SURVIVAL || mc.gameMode.getPlayerMode() == GameType.CREATIVE) {
                // Image
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, (screenWidth / 2) - 170 + Config.deltaX, screenHeight - 19 + Config.deltaY, 0, 0, 16, 16 , 16, 16);
                // Text
                guiGraphics.pose().pushMatrix();
                guiGraphics.pose().scale(1F, 1F, guiGraphics.pose());
                guiGraphics.drawString(mc.font, infectionProtection + "%", (screenWidth / 2) + 18 - 170 + Config.deltaX, screenHeight - 14 + Config.deltaY, color);
                guiGraphics.pose().popMatrix();
            }
        }
    }
}
