package net.petemc.contagion.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.ModList;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.MainConfig;
import org.spongepowered.asm.mixin.Unique;

public class ModCompatibility {
    public static void init() {
        if (undeadNightsDetected()) {
            Contagion.LOGGER.info("Undead Nights mod detected. Horde Zombies will drop Contagious Flesh.");
        }
    }

    public static boolean undeadNightsDetected() {
        return ModList.get().isLoaded("undeadnights");
    }

    /**
     * Check if the entity's mob ID contains both "infectious" and "zombie".
     */
    @Unique
    public static boolean isMobHasMutantsAndZombiesZombieId(Entity entity) {
        if (!MainConfig.isMutantsZombiesModCompat()) return false;
        ResourceLocation mobId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (mobId == null) return false;
        String id = mobId.toString().toLowerCase();
        boolean flag = id.contains("mutantszombies");
        boolean flag2 = id.contains("blister") || id.contains("split") || id.contains("zombie_brute") || id.contains("mutant_zombie");
        return flag && flag2;
    }

    /**
     * Check if the entity's mob ID contains both "infectious" and "zombie".
     */
    @Unique
    public static boolean isMobHasInfectiousZombieId(Entity entity) {
        if (!MainConfig.isInfectiousModCompat()) return false;
        ResourceLocation mobId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (mobId == null) return false;
        String id = mobId.toString().toLowerCase();
        return id.contains("infectious") && id.contains("zombie");
    }
}
