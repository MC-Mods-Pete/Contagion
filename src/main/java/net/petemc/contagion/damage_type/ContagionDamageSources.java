package net.petemc.contagion.damage_type;

import net.minecraft.world.damagesource.DamageSource;

public class ContagionDamageSources {
    /*
     * Store the RegistryKey of the DamageType into a new constant called INFECTION
     * The Identifier in use here points to the JSON file infection.json.
     */

    public static final DamageSource INFECTION = (new DamageSource("infection").bypassArmor());

    public static void registerDamageTypes() {
        new ContagionDamageSources();
    }
}
