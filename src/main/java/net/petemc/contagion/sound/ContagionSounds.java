package net.petemc.contagion.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.petemc.contagion.Contagion;

public class ContagionSounds {

    public static final SoundEvent INFECTION_PREVENTED = registerSoundEvent("immunity_prevents_infection");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(Contagion.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSounds() {
        Contagion.LOGGER.info("Registering Sounds for " + Contagion.MOD_ID);
    }
}
