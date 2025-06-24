package net.petemc.contagion.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.petemc.contagion.casts.InfectedPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements InfectedPlayer {
    @Unique
    private boolean infected = false;
    @Unique
    private boolean playerDiedFromInfection = false;
    @Unique
    private long infectionTicks = 0;
    @Unique
    private long infectionCooldown = 0;

    @Unique
    public void contagion_setInfection(boolean infectedValue) {
        this.infected = infectedValue;
    }

    @Unique
    public boolean contagion_isPlayerInfected() {
        return this.infected;
    }

    @Unique
    public void contagion_setPlayerDiedFromInfection(boolean value) {
        this.playerDiedFromInfection = value;
    }

    @Unique
    public boolean contagion_playerDiedFromInfection() {
        return this.playerDiedFromInfection;
    }

    @Unique
    public void contagion_setInfectionTicks(long infectionTicks) {
        this.infectionTicks = infectionTicks;
    }

    @Unique
    public long contagion_getInfectionTicks() {
        return infectionTicks;
    }

    @Unique
    public void contagion_setInfectionCooldown(long infectionCooldown) {
        this.infectionCooldown = infectionCooldown;
    }

    @Unique
    public long contagion_getInfectionCooldown() {
        return infectionCooldown;
    }

    @Inject(method = "readCustomData", at = @At("TAIL"))
    private void injectToReadCustomData(ReadView view, CallbackInfo ci) {
        this.infected = view.getBoolean("contagion_is_player_infected", false);
        this.playerDiedFromInfection = view.getBoolean("contagion_player_died_from_infection", false);
        this.infectionTicks = view.getLong("contagion_infection_ticks", 0L);
        this.infectionCooldown = view.getLong("contagion_infection_cooldown", 0L);
    }

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    private void injectToWriteCustomData(WriteView view, CallbackInfo ci) {
        view.putBoolean("contagion_is_player_infected", this.infected);
        view.putBoolean("contagion_player_died_from_infection", this.playerDiedFromInfection);
        view.putLong("contagion_infection_ticks", this.infectionTicks);
        view.putLong("contagion_infection_cooldown", this.infectionCooldown);
    }
}
