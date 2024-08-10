package net.petemc.contagion.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.petemc.contagion.Contagion;

public class ContagionSounds {

    public static final SoundEvent INFECTION_PREVENTED = registerSoundEvent("immunity_prevents_infection");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(Contagion.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        Contagion.LOGGER.info("Registering Sounds for " + Contagion.MOD_ID);
    }
}
