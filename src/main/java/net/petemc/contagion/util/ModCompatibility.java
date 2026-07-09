package net.petemc.contagion.util;

import net.fabricmc.loader.api.FabricLoader;
import net.petemc.contagion.Contagion;

public class ModCompatibility {
    public static void init() {
        if (undeadNightsDetected()) {
            Contagion.LOGGER.info("Undead Nights mod detected. Horde Zombies will drop Contagious Flesh.");
        }
    }

    public static boolean undeadNightsDetected() {
        return FabricLoader.getInstance().isModLoaded("undeadnights");
    }
}
