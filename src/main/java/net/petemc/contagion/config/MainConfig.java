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
    // Getter functions
    public static int getInfectionDuration() { return INSTANCE.infectionDuration; }
    public static int getBaseInfectionChance() { return INSTANCE.baseInfectionChance; }
    public static int getMinimumInfectionChance() { return INSTANCE.minimumInfectionChance; }
    public static boolean isArmorLowersInfectionChance() { return INSTANCE.armorLowersInfectionChance; }
    public static boolean isEnableRandomSymptoms() { return INSTANCE.enableRandomSymptoms; }
    public static int getRandomSymptomsDuration() { return INSTANCE.randomSymptomsDuration; }
    public static int getRandomSymptomsChance() { return INSTANCE.randomSymptomsChance; }
    public static int getImmunityDuration() { return INSTANCE.immunityDuration; }
    public static boolean isMilkCuresInfection() { return INSTANCE.milkCuresInfection; }
    public static boolean isTotemPreventsDyingFromInfection() { return INSTANCE.totemPreventsDyingFromInfection; }
    public static boolean isIronGolemAttacksInfected() { return INSTANCE.ironGolemAttacksInfected; }
    public static boolean isDisplayCurrentInfectionProtection() { return INSTANCE.displayCurrentInfectionProtection; }
    public static int getDeltaX() { return INSTANCE.deltaX; }
    public static int getDeltaY() { return INSTANCE.deltaY; }
    
    @ConfigEntry.Gui.Excluded
    public static MainConfig INSTANCE;

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

    @ConfigEntry.Gui.PrefixText

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
    @Comment("If true, holding a totem will not prevent the player from dying when the infection timer runs out | default: true")
    private boolean totemPreventsDyingFromInfection = true;

    @ConfigEntry.Gui.Tooltip()
    @Comment("If true, Iron Golems will attack infected players | default: false")
    private boolean ironGolemAttacksInfected = false;

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
