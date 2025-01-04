package net.petemc.contagion;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Contagion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    // Server Config
    private static final ForgeConfigSpec.Builder BUILDER_SERVER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue INFECTION_DURATION = BUILDER_SERVER
            .comment("Infection duration")
            .defineInRange("infectionDuration", 600, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue BASE_INFECTION_CHANCE = BUILDER_SERVER
            .comment("Base infection chance")
            .defineInRange("baseInfectionChance", 80, 0, 100);

    private static final ForgeConfigSpec.IntValue MINIMUM_INFECTION_CHANCE = BUILDER_SERVER
            .comment("Minimum infection chance")
            .defineInRange("minimumInfectionChance", 10, 0, 100);

    private static final ForgeConfigSpec.BooleanValue ARMOR_LOWERS_INFECTION_CHANCE = BUILDER_SERVER
            .comment("If true, wearing armor will lower the infection chance (full Netherite will lower the chance by 60%)")
            .define("armorLowersInfectionChance", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_RANDOM_SYMPTOMS = BUILDER_SERVER
            .comment("If true, random symptoms can occur when infected")
            .define("enableRandomSymptoms", true);

    private static final ForgeConfigSpec.IntValue RANDOM_SYMPTOMS_DURATION = BUILDER_SERVER
            .comment("Time in seconds a random symptom will last")
            .defineInRange("randomSymptomsDuration", 30, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue RANDOM_SYMPTOMS_CHANCE = BUILDER_SERVER
            .comment("Chance for a random symptom to occur when infected")
            .defineInRange("randomSymptomsChance", 3, 0, 100);

    // Todo fix issue with loading immunity duration
    /*
    private static final ForgeConfigSpec.IntValue IMMUNITY_DURATION = BUILDER
            .comment("Time the immunity from new infections will last")
            .defineInRange("immunityDuration", 90, 0, Integer.MAX_VALUE);
     */

    private static final ForgeConfigSpec.BooleanValue MILK_CURES_INFECTION = BUILDER_SERVER
            .comment("If true, drinking milk will cure the player if infected")
            .define("milkCuresInfection", false);

    private static final ForgeConfigSpec.BooleanValue TOTEM_PREVENTS_DYING_FROM_INFECTION = BUILDER_SERVER
            .comment("If false, holding a totem will not prevent the player from dying when the infection timer runs out")
            .define("totemPreventsDyingFromInfection", true);

    static final ForgeConfigSpec SPEC_SERVER = BUILDER_SERVER.build();


    // Client Config
    private static final ForgeConfigSpec.Builder BUILDER_CLIENT = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue DISPLAY_CURRENT_INFECTION_PROTECTION = BUILDER_CLIENT
            .comment("[Client] If true, displays the current protection value in the HUD")
            .define("displayCurrentInfectionProtection", true);

    private static final ForgeConfigSpec.IntValue DELTA_X = BUILDER_CLIENT
            .comment("[Client] Move the displayed protection value in the HUD in X-direction")
            .defineInRange("deltaX", 0, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue DELTA_Y = BUILDER_CLIENT
            .comment("[Client] Move the displayed protection value in the HUD in Y-direction")
            .defineInRange("deltaY", 0, 0, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC_CLIENT = BUILDER_CLIENT.build();

    public static int infectionDuration;
    public static int baseInfectionChance;
    public static int minimumInfectionChance;
    public static boolean armorLowersInfectionChance;
    public static boolean enableRandomSymptoms;
    public static int randomSymptomsDuration;
    public static int randomSymptomsChance;
    public static int immunityDuration = 120;
    public static boolean milkCuresInfection;
    public static boolean totemPreventsDyingFromInfection;
    public static boolean displayCurrentInfectionProtection;
    public static int deltaX;
    public static int deltaY;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        Contagion.LOGGER.info("Loading Config");
        if (SPEC_SERVER.isLoaded()) {
            infectionDuration = INFECTION_DURATION.get();
            baseInfectionChance = BASE_INFECTION_CHANCE.get();
            minimumInfectionChance = MINIMUM_INFECTION_CHANCE.get();
            armorLowersInfectionChance = ARMOR_LOWERS_INFECTION_CHANCE.get();
            enableRandomSymptoms = ENABLE_RANDOM_SYMPTOMS.get();
            randomSymptomsDuration = RANDOM_SYMPTOMS_DURATION.get();
            randomSymptomsChance = RANDOM_SYMPTOMS_CHANCE.get();
            //immunityDuration = IMMUNITY_DURATION.get();
            milkCuresInfection = MILK_CURES_INFECTION.get();
            totemPreventsDyingFromInfection = TOTEM_PREVENTS_DYING_FROM_INFECTION.get();
        }
        if (SPEC_CLIENT.isLoaded()) {
            displayCurrentInfectionProtection = DISPLAY_CURRENT_INFECTION_PROTECTION.get();
            deltaX = DELTA_X.get();
            deltaY = DELTA_Y.get();
        }
    }
}
