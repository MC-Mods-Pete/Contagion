package net.petemc.contagion.damage_type;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class ContagionDamageTypes {
    /*
     * Store the RegistryKey of the DamageType into a new constant called INFECTION
     * The Identifier in use here points to the JSON file infection.json.
     */
    public static final ResourceKey<DamageType> INFECTION = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath("contagion", "infection"));

    public static DamageSource of(Level world, ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key));
    }

    public static void registerDamageTypes() {
        new ContagionDamageTypes();
    }
}
