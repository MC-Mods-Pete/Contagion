package net.petemc.contagion;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Contagion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue INFECTION_DURATION = BUILDER
            .comment("Infection duration")
            .defineInRange("infectionDuration", 600, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue BASE_INFECTION_CHANCE = BUILDER
            .comment("Base infection chance")
            .defineInRange("baseInfectionChance", 80, 0, 100);

    private static final ForgeConfigSpec.IntValue MINIMUM_INFECTION_CHANCE = BUILDER
            .comment("Minimum infection chance")
            .defineInRange("minimumInfectionChance", 10, 0, 100);

    private static final ForgeConfigSpec.BooleanValue ARMOR_LOWERS_INFECTION_CHANCE = BUILDER
            .comment("If true, wearing armor will lower the infection chance (full Netherite will lower the chance by 60%)")
            .define("armorLowersInfectionChance", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_RANDOM_SYMPTOMS = BUILDER
            .comment("If true, random symptoms can occur when infected")
            .define("enableRandomSymptoms", true);

    private static final ForgeConfigSpec.IntValue RANDOM_SYMPTOMS_DURATION = BUILDER
            .comment("Time in seconds a random symptom will last")
            .defineInRange("randomSymptomsDuration", 30, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue RANDOM_SYMPTOMS_CHANCE = BUILDER
            .comment("Chance for a random symptom to occur when infected")
            .defineInRange("randomSymptomsChance", 3, 0, 100);

    // Todo fix issue with loading immunity duration
    /*
    private static final ForgeConfigSpec.IntValue IMMUNITY_DURATION = BUILDER
            .comment("Time the immunity from new infections will last")
            .defineInRange("immunityDuration", 120, 0, Integer.MAX_VALUE);
    */

    private static final ForgeConfigSpec.BooleanValue MILK_CURES_INFECTION = BUILDER
            .comment("If true, drinking milk will cure the player if infected")
            .define("milkCuresInfection", false);

    private static final ForgeConfigSpec.BooleanValue TOTEM_PREVENTS_DYING_FROM_INFECTION = BUILDER
            .comment("If false, holding a totem will not prevent the player from dying when the infection timer runs out")
            .define("totemPreventsDyingFromInfection", true);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int infectionDuration;
    public static int baseInfectionChance;
    public static int minimumInfectionChance;
    public static boolean armorLowersInfectionChance;
    public static boolean enableRandomSymptoms;
    public static int randomSymptomsDuration;
    public static int randomSymptomsChance;
    //public static int immunityDuration;
    public static boolean milkCuresInfection;
    public static boolean totemPreventsDyingFromInfection;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        Contagion.LOGGER.info("Loading Config");
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
}
