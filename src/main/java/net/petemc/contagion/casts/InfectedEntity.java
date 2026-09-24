package net.petemc.contagion.casts;

public interface InfectedEntity {
    void contagion_setInfection(boolean infectedValue);

    boolean contagion_isPlayerInfected();

    void contagion_setPlayerDiedFromInfection(boolean value);

    boolean contagion_playerDiedFromInfection();

    void contagion_setInfectionTicks(long infectionTicks);

    public long contagion_getInfectionTicks();

    public void contagion_setInfectionCooldown(long infectionCooldown);

    public long contagion_getInfectionCooldown();

    public void contagion_setInitialInfectionDuration(long duration);

    public long contagion_getInitialInfectionDuration();

    void contagion_setInfectious(boolean infectiousValue);

    boolean contagion_isInfectious();
}
