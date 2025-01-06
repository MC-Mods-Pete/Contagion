package net.petemc.contagion.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.petemc.contagion.Config;

public class ProtectionHudOverlay {
    private static final ResourceLocation texture = new ResourceLocation("contagion", "textures/hud/contagion_armor16.png");

    public static int infectionProtection = -1;
    public static int cachedInfectionProtection = -1;

    public static int receivedBaseInfectionChance = -1;
    public static int receivedMinimumInfectionChance = -1;
    public static boolean receivedArmorLowersInfectionChance = false;

    public static final IGuiOverlay HUD_PROTECTION = ((gui, guiGraphics, partialTick, width, height) -> {
        Minecraft mc = Minecraft.getInstance();

        int color = 0xffffff;
        if (infectionProtection == 100) {
            color = 0xd4af37;
        } else if (infectionProtection >= 75) {
            color = 0x3fc400;
        } else if (infectionProtection < 30) {
            color = 0xe00000;
        }

        if ((Config.displayCurrentInfectionProtection) && (receivedBaseInfectionChance != -1)) {
            assert mc.gameMode != null;
            if (mc.gameMode.hasExperience() || mc.gameMode.hasInfiniteItems()) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                guiGraphics.blit(texture, (width / 2) - 170 + Config.deltaX, height - 19 + Config.deltaY, 0, 0, 16, 16, 16, 16);
                guiGraphics.drawString(mc.font, infectionProtection + "%", (width / 2) + 18 - 170 + Config.deltaX, height - 14 + Config.deltaY, color);
            }
        }
    });
}
