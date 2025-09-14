package net.petemc.contagion.util;

import net.minecraftforge.fml.ModList;
import net.petemc.contagion.Contagion;

public class ModCompatibility {
    public static void init() {
        if (undeadNightsDetected()) {
            Contagion.LOGGER.info("Undead Nights mod detected. Horde Zombies will drop Contagious Flesh.");
        }
    }

    public static boolean undeadNightsDetected() {
        return ModList.get().isLoaded("undeadnights");
    }
}
