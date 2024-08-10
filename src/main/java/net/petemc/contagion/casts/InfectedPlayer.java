package net.petemc.contagion.casts;

public interface InfectedPlayer {
    void contagion_setInfection(boolean infectedValue);

    boolean contagion_isPlayerInfected();

    void contagion_setInfectionTicks(long infectionTicks);

    public long contagion_getInfectionTicks();

    public void contagion_setInfectionCooldown(long infectionCooldown);

    public long contagion_getInfectionCooldown();
}
