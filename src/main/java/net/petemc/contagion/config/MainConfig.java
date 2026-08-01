package net.petemc.contagion.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.petemc.contagion.Contagion;

@Config(name = Contagion.MOD_ID)
public class MainConfig implements ConfigData
{
    @ConfigEntry.Gui.Excluded
    public static MainConfig INSTANCE;

    // Server Getter for all config values
    public static int getInfectionDuration() { return INSTANCE.infectionDuration; }
    public static int getBaseInfectionChance() { return INSTANCE.baseInfectionChance; }
    public static int getMinimumInfectionChance() { return INSTANCE.minimumInfectionChance; }
    public static boolean isArmorLowersInfectionChance() { return INSTANCE.armorLowersInfectionChance; }
    public static boolean isEnableRandomSymptoms() { return INSTANCE.enableRandomSymptoms; }
    public static int getRandomSymptomsDuration() { return INSTANCE.randomSymptomsDuration; }
    public static int getRandomSymptomsChance() { return INSTANCE.randomSymptomsChance; }
    public static boolean isInfiniteWeaknessAndSlowness() {return INSTANCE.infiniteWeaknessAndSlowness; }
    public static int getImmunityDuration() { return INSTANCE.immunityDuration; }
    public static boolean isMilkCuresInfection() { return INSTANCE.milkCuresInfection; }
    public static boolean isTotemPreventsDyingFromInfection() { return INSTANCE.totemPreventsDyingFromInfection; }
    public static boolean isIronGolemAttacksInfected() { return INSTANCE.ironGolemAttacksInfected; }
    public static boolean isAllowPlayersToInfectPlayers() { return INSTANCE.allowPlayersToInfectPlayers; }
    public static boolean isAllowPlayersToInfectVillagers() { return INSTANCE.allowPlayersToInfectVillagers; }
    public static boolean isAllowZombiesToInfectVillagers() { return INSTANCE.allowZombiesToInfectVillagers; }
    public static boolean isVillagerZombieConversion() { return INSTANCE.villagerZombieConversion; }
    public static boolean isEnableAirborneInfection() { return INSTANCE.enableAirborneInfection; }
    public static boolean isAllowAirborneInfectionForVillagers() { return INSTANCE.allowAirborneInfectionForVillagers; }
    public static boolean isVillagerFleeFromInfectiousEntities() { return INSTANCE.villagerFleeFromInfectiousEntities; }
    public static boolean isAllowZombiesToSpawnInfectious() { return INSTANCE.allowZombiesToSpawnInfectious; }
    public static int getChanceForZombieToSpawnInfectious() { return INSTANCE.chanceForZombieToSpawnInfectious; }
    public static int getAirborneInfectionRadius() { return INSTANCE.airborneInfectionRadius; }
    public static int getAirborneMaxChance() { return INSTANCE.airborneMaxChance; }
    public static double getContagiousFleshDropChance() { return INSTANCE.contagiousFleshDropChance; }
    public static double getContagiousFleshLootingBonus() { return INSTANCE.contagiousFleshLootingBonus; }
    public static boolean isEnableDebugMessages() { return INSTANCE.enableDebugMessages; }
    public static boolean isGoldenAppleCuresInfection() { return INSTANCE.goldenAppleCuresInfection; }
    public static boolean isEnchantedGoldenAppleCuresInfection() { return INSTANCE.enchantedGoldenAppleCuresInfection; }
    // Client getters
    public static boolean isDisplayCurrentInfectionProtection() { return INSTANCE.displayCurrentInfectionProtection; }
    public static int getDeltaX() { return INSTANCE.deltaX; }
    public static int getDeltaY() { return INSTANCE.deltaY; }

    public static void init() {
        AutoConfig.register(MainConfig.class, JanksonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(MainConfig.class).getConfig();
        if (INSTANCE.infectionDuration < 0) {
            Contagion.LOGGER.warn("Value for infectionDuration is smaller than 0, using default.");
            INSTANCE.infectionDuration = 600;
        }
        if ((INSTANCE.baseInfectionChance < 0) || (INSTANCE.baseInfectionChance > 100)) {
            Contagion.LOGGER.warn("Value for baseInfectionChance not in range (valid range 0 - 100), using default.");
            INSTANCE.baseInfectionChance = 80;
        }
        if ((INSTANCE.minimumInfectionChance < 0) || (INSTANCE.minimumInfectionChance > 100)) {
            Contagion.LOGGER.warn("Value for minimumInfectionChance not in range (valid range 0 - 100), using default.");
            INSTANCE.minimumInfectionChance = 10;
        }
        if (INSTANCE.randomSymptomsDuration < 0) {
            Contagion.LOGGER.warn("Value for randomSymptomsDuration is smaller than 0, using default.");
            INSTANCE.randomSymptomsDuration = 30;
        }
        if ((INSTANCE.randomSymptomsChance < 0) || (INSTANCE.randomSymptomsChance > 100)) {
            Contagion.LOGGER.warn("Value for randomSymptomsChance not in range (valid range 0 - 100), using default.");
            INSTANCE.randomSymptomsChance = 3;
        }
        if (INSTANCE.immunityDuration < 0) {
            Contagion.LOGGER.warn("Value for immunityDuration is smaller than 0, using default.");
            INSTANCE.immunityDuration = 120;
        }
    }

    public static final double DEFAULT_CONTAGIOUS_FLESH_DROP_CHANCE = 0.075;
    public static final double DEFAULT_CONTAGIOUS_FLESH_LOOTING_BONUS = 0.025;

    @ConfigEntry.Gui.PrefixText

    // Server config values
    @ConfigEntry.Gui.Tooltip()
    @Comment("Time in seconds until the infection kills the player | default: 600")
    private int infectionDuration = 600;

    @ConfigEntry.Gui.Tooltip()
    @Comment("Base chance in % an attack will infect the player | default: 80 | range: 0 - 100")
    private int baseInfectionChance = 80;

    @ConfigEntry.Gui.Tooltip()
    @Comment("Minimum infection chance in % (even with armor the chance can not be lower than this minimum) | default: 10 | range: 0 - 100")
    private int minimumInfectionChance = 10;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, wearing armor will lower the infection chance (full Netherite will lower the chance by 60%)")
    private boolean armorLowersInfectionChance = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, random symptoms can occur when infected")
    private boolean enableRandomSymptoms = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, the random effects weakness and slowness effects will last until the infection is cured or drinking milk")
    private boolean infiniteWeaknessAndSlowness = false;

    @ConfigEntry.Gui.Tooltip()
    @Comment("Time in seconds a random symptom will last | default: 30")
    private int randomSymptomsDuration = 30;

    @ConfigEntry.Gui.Tooltip()
    @Comment("Chance in % a random symptom can occur when infected | default: 3 | range: 0 - 100")
    private int randomSymptomsChance = 3;

    @ConfigEntry.Gui.Tooltip()
    @Comment("Time in seconds after drinking a cure the player is immune to new infections | default: 120")
    private int immunityDuration = 120;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, drinking milk will cure the player if infected | default: false")
    private boolean milkCuresInfection = false;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, holding a totem prevents the player from dying when the infection timer expires | default: true")
    private boolean totemPreventsDyingFromInfection = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, Iron Golems will attack infected players")
    private boolean ironGolemAttacksInfected = false;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, infected players can infect other players by attacking them")
    private boolean allowPlayersToInfectPlayers = false;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, infected players can infect Villagers by attacking them")
    private boolean allowPlayersToInfectVillagers = false;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, Zombies can infect Villagers by attacking them")
    private boolean allowZombiesToInfectVillagers = false;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, an infected Villager will convert into a Zombie Entity when it dies from the infection")
    private boolean villagerZombieConversion = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, enables airborne (aerosol) transmission of infection between nearby players")
    private boolean enableAirborneInfection = false;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, Villages are also able to transmit and receive the infection via airborne (aerosol) transmission")
    private boolean allowAirborneInfectionForVillagers = false;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, Villagers flee from infectious entities")
    private boolean villagerFleeFromInfectiousEntities = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, Zombies will spawn with the infectious effect (as Spreaders)")
    private boolean allowZombiesToSpawnInfectious = false;

    @ConfigEntry.Gui.Tooltip()
    @Comment("Chance (X in 1000) for a Zombie to spawn with the infectious effect (as Spreaders) | default: 155 | range: 1 - 1000")
    private int chanceForZombieToSpawnInfectious = 155;

    @ConfigEntry.Gui.Tooltip()
    @Comment("Maximum radius (in blocks) for aerosol transmission of infection via breathing/spores | default: 6 | min: 1")
    private int airborneInfectionRadius = 6;

    @ConfigEntry.Gui.Tooltip()
    @Comment("Maximum infection chance at (directly adjacent a spreader) for aerosol transmission | default: 50 | range: 0 - 100")
    private int airborneMaxChance = 50;

    @ConfigEntry.Gui.Tooltip()
    @Comment("Base drop chance for Contagious Flesh from zombie-type mobs (0.0 to 1.0) | default: " + DEFAULT_CONTAGIOUS_FLESH_DROP_CHANCE)
    private double contagiousFleshDropChance = DEFAULT_CONTAGIOUS_FLESH_DROP_CHANCE;

    @ConfigEntry.Gui.Tooltip()
    @Comment("Looting bonus for Contagious Flesh drops per Looting level (0.0 to 1.0) | default: " + DEFAULT_CONTAGIOUS_FLESH_LOOTING_BONUS)
    private double contagiousFleshLootingBonus = DEFAULT_CONTAGIOUS_FLESH_LOOTING_BONUS;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, eating a golden apple will cure the player's infection")
    private boolean goldenAppleCuresInfection = false;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, eating an enchanted golden apple (god apple) will cure the player's infection")
    private boolean enchantedGoldenAppleCuresInfection = false;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, writes debug messages to the log")
    private boolean enableDebugMessages = false;

    // Client config values
    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip()
    @Comment("[Client] If true, displays the current protection value in the HUD | default: true")
    private boolean displayCurrentInfectionProtection = true;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip()
    @Comment("[Client] Move the displayed protection value in the HUD in X-direction | default: 0")
    private int deltaX = 0;

    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.Tooltip()
    @Comment("[Client] Move the displayed protection value in the HUD in Y-direction | default: 0")
    private int deltaY = 0;
}
