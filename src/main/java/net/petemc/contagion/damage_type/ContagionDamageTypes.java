package net.petemc.contagion.damage_type;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ContagionDamageTypes {
    /*
     * Store the RegistryKey of the DamageType into a new constant called INFECTION
     * The Identifier in use here points to the JSON file infection.json.
     */
    public static final RegistryKey<DamageType> INFECTION = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("contagion", "infection"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

    public static void registerDamageTypes() {
        new ContagionDamageTypes();
    }
}
