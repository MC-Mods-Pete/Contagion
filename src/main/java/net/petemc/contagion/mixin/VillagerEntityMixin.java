package net.petemc.contagion.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.petemc.contagion.casts.InfectedEntity;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
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
    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    private void injectContagionPanic(CallbackInfo ci) {
        if (MainConfig.isVillagerFleeFromInfectiousEntities()) {
            Villager self = (Villager) (Object) this;

            // Performance-Phasing
            if (!self.level().isClientSide() && (self.tickCount + self.getId()) % 40 == 0) {

                // Search for infectious entities in given radius
                boolean virusNearby = !self.level().getEntitiesOfClass(LivingEntity.class,
                        self.getBoundingBox().inflate(8.0),
                        e -> e.hasEffect(ContagionEffects.INFECTIOUS)
                ).isEmpty();

                if (virusNearby) {
                    // Puts the villager in the vanilla panik mode
                    self.getBrain().setMemory(MemoryModuleType.NEAREST_HOSTILE, self);
                }
            }
        }
    }

    // --- NBT Persistence (mirrors PlayerEntityMixin pattern) ---

    /** Load infection data from the villager's CompoundTag when the villager loads.*/
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(ValueInput valueInput, CallbackInfo ci) {
        this.contagion_isInfected = valueInput.getBooleanOr("contagion_is_player_infected", false);
        this.contagion_playerDiedFromInfection = valueInput.getBooleanOr("contagion_player_died_from_infection", false);
        this.contagion_infectionTicks = valueInput.getLongOr("contagion_infection_ticks", 0L);
        this.contagion_infectionCooldown = valueInput.getLongOr("contagion_infection_cooldown", 0L);
        this.contagion_initialInfectionDuration = valueInput.getLongOr("contagion_initial_infection_duration", 0L);
        this.contagion_infectious = valueInput.getBooleanOr("contagion_is_infectious", false);
    }

    /** Save infection data to the villager's CompoundTag for persistence across reloads and saves.*/
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.putBoolean("contagion_is_player_infected", this.contagion_isInfected);
        valueOutput.putBoolean("contagion_player_died_from_infection", this.contagion_playerDiedFromInfection);
        valueOutput.putLong("contagion_infection_ticks", this.contagion_infectionTicks);
        valueOutput.putLong("contagion_infection_cooldown", this.contagion_infectionCooldown);
        valueOutput.putLong("contagion_initial_infection_duration", this.contagion_initialInfectionDuration);
        valueOutput.putBoolean("contagion_is_infectious", this.contagion_infectious);
    }
}
