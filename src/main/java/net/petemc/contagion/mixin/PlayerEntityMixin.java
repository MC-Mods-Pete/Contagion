package net.petemc.contagion.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.petemc.contagion.casts.InfectedPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Player.class)
public abstract class PlayerEntityMixin implements InfectedPlayer {
    @Unique
    private boolean infected = false;
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

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectToReadNbt(CompoundTag nbt, CallbackInfo ci) {
        this.infected = nbt.getBoolean("contagion_is_player_infected");
        this.infectionTicks = nbt.getLong("contagion_infection_ticks");
        this.infectionCooldown = nbt.getLong("contagion_infection_cooldown");
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectToWriteNbt(CompoundTag nbt, CallbackInfo ci) {
        nbt.putBoolean("contagion_is_player_infected", this.infected);
        nbt.putLong("contagion_infection_ticks", this.infectionTicks);
        nbt.putLong("contagion_infection_cooldown", this.infectionCooldown);
    }
}
