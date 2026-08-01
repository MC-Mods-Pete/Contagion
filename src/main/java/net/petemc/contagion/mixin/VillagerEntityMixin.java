package net.petemc.contagion.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.petemc.contagion.casts.InfectedEntity;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin implements InfectedEntity {

    @Unique
    private boolean contagion_isInfected = false;
    @Unique
    private boolean contagion_playerDiedFromInfection = false;
    @Unique
    private long contagion_infectionTicks = 0L;
    @Unique
    private long contagion_infectionCooldown = 0L;
    @Unique
    private long contagion_initialInfectionDuration = 0L;
    @Unique
    private boolean contagion_infectious = false;

    /** Override: sets whether the villager is infected. */
    @Override public void contagion_setInfection(boolean infectedValue) { this.contagion_isInfected = infectedValue; }

    /** Override: returns true if the villager is currently infected. */
    @Override public boolean contagion_isPlayerInfected() { return this.contagion_isInfected; }

    /** Override: sets whether the villager died from infection. */
    @Override public void contagion_setPlayerDiedFromInfection(boolean value) { this.contagion_playerDiedFromInfection = value; }

    /** Override: returns true if the villager died from infection. */
    @Override public boolean contagion_playerDiedFromInfection() { return this.contagion_playerDiedFromInfection; }

    /** Override: sets current remaining infection ticks. */
    @Override public void contagion_setInfectionTicks(long infectionTicks) { this.contagion_infectionTicks = infectionTicks; }

    /** Override: returns the number of ticks until death from infection. */
    @Override public long contagion_getInfectionTicks() { return this.contagion_infectionTicks; }

    /** Override: sets the cooldown between random symptoms. */
    @Override public void contagion_setInfectionCooldown(long infectionCooldown) { this.contagion_infectionCooldown = infectionCooldown; }

    /** Override: returns current infection cooldown counter. */
    @Override public long contagion_getInfectionCooldown() { return this.contagion_infectionCooldown; }

    /** Override: records how long the infection started with (initial duration before ticks are decremented). */
    @Override public void contagion_setInitialInfectionDuration(long duration) { this.contagion_initialInfectionDuration = duration; }

    /** Override: returns the initial infection duration set when infection began. */
    @Override public long contagion_getInitialInfectionDuration() { return this.contagion_initialInfectionDuration; }

    /** Override: sets whether the villager can transmit the infection to others. */
    @Override public void contagion_setInfectious(boolean infectiousValue) { this.contagion_infectious = infectiousValue; }

    /** Override: returns true if the villager is currently a valid infection spreader. */
    @Override public boolean contagion_isInfectious() { return this.contagion_infectious; }

    
    /** Let the villager flee, when an infectious entity is nearby.*/
    @Inject(method = "mobTick", at = @At("HEAD"))
    private void injectContagionPanic(CallbackInfo ci) {
        if (MainConfig.isVillagerFleeFromInfectiousEntities()) {
            VillagerEntity self = (VillagerEntity) (Object) this;

            // Performance-Phasing
            if (!self.getWorld().isClient() && (self.age + self.getId()) % 40 == 0) {

                // Search for infectious entities in given radius
                boolean virusNearby = !self.getWorld().getEntitiesByClass(LivingEntity.class,
                        self.getBoundingBox().expand(8.0),
                        e -> e.hasStatusEffect(ContagionEffects.INFECTIOUS)
                ).isEmpty();

                if (virusNearby) {
                    // Puts the villager in the vanilla panik mode
                    self.getBrain().remember(MemoryModuleType.NEAREST_HOSTILE, self);
                }
            }
        }
    }

    // --- NBT Persistence (mirrors PlayerEntityMixin pattern) ---

    /** Load infection data from the villager's CompoundTag when the villager loads.*/
    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectToReadNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound t = nbt.getCompound("contagion_villager_data");
        if(!t.isEmpty()) {
            this.contagion_isInfected = t.getBoolean("contagion_is_player_infected");
            this.contagion_playerDiedFromInfection = t.getBoolean("contagion_player_died_from_infection");
            this.contagion_infectionTicks = t.getLong("contagion_infection_ticks");
            this.contagion_infectionCooldown = t.getLong("contagion_infection_cooldown");
            this.contagion_initialInfectionDuration = t.getLong("contagion_initial_infection_duration");
            this.contagion_infectious = t.getBoolean("contagion_is_infectious");
        }
    }

    /** Save infection data to the villager's CompoundTag for persistence across reloads and saves.*/
    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectToWriteNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound t = new NbtCompound();
        t.putBoolean("contagion_is_player_infected", this.contagion_isInfected);
        t.putBoolean("contagion_player_died_from_infection", this.contagion_playerDiedFromInfection);
        t.putLong("contagion_infection_ticks", this.contagion_infectionTicks);
        t.putLong("contagion_infection_cooldown", this.contagion_infectionCooldown);
        t.putLong("contagion_initial_infection_duration", this.contagion_initialInfectionDuration);
        t.putBoolean("contagion_is_infectious", this.contagion_infectious);
        nbt.put("contagion_villager_data", t);
    }
}
