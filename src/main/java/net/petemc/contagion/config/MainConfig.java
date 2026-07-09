package net.petemc.contagion.config;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.potion.ContagionPotions;

@Mod.EventBusSubscriber(modid = Contagion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MainConfig
{
    // Getter for all config values
    public static int getInfectionDuration() { return infectionDuration; }
    public static int getBaseInfectionChance() { return baseInfectionChance; }
    public static int getMinimumInfectionChance() { return minimumInfectionChance; }
    public static boolean isArmorLowersInfectionChance() { return armorLowersInfectionChance; }
    public static boolean isEnableRandomSymptoms() { return enableRandomSymptoms; }
    public static int getRandomSymptomsDuration() { return randomSymptomsDuration; }
    public static int getRandomSymptomsChance() { return randomSymptomsChance; }
    public static boolean isInfiniteWeaknessAndSlowness() {return infiniteWeaknessAndSlowness; }
    public static int getImmunityDuration() { return immunityDuration; }
    public static boolean isMilkCuresInfection() { return milkCuresInfection; }
    public static boolean isTotemPreventsDyingFromInfection() { return totemPreventsDyingFromInfection; }
    public static boolean isIronGolemAttacksInfected() { return ironGolemAttacksInfected; }
    public static boolean isAllowPlayersToInfectPlayers() { return allowPlayersToInfectPlayers; }
    public static boolean isAllowPlayersToInfectVillagers() { return allowPlayersToInfectVillagers; }
    public static boolean isAllowZombiesToInfectVillagers() { return allowZombiesToInfectVillagers; }
    public static boolean isVillagerZombieConversion() { return villagerZombieConversion; }
    public static boolean isEnableAirborneInfection() { return enableAirborneInfection; }
    public static boolean isAllowAirborneInfectionForVillagers() { return allowAirborneInfectionForVillagers; }
    public static boolean isVillagerFleeFromInfectiousEntities() { return villagerFleeFromInfectiousEntities; }
    public static boolean isAllowZombiesToSpawnInfectious() { return allowZombiesToSpawnInfectious; }
    public static int getChanceForZombieToSpawnInfectious() { return chanceForZombieToSpawnInfectious; }
    public static int getAirborneInfectionRadius() { return airborneInfectionRadius; }
    public static int getAirborneMaxChance() { return airborneMaxChance; }
    public static double getContagiousFleshDropChance() { return contagiousFleshDropChance; }
    public static double getContagiousFleshLootingBonus() { return contagiousFleshLootingBonus; }
    public static boolean isEnableDebugMessages() { return enableDebugMessages; }
    public static boolean isDisplayCurrentInfectionProtection() { return displayCurrentInfectionProtection; }
    public static int getDeltaX() { return deltaX; }
    public static int getDeltaY() { return deltaY; }
    public static boolean isGoldenAppleCuresInfection() { return goldenAppleCuresInfection; }
    public static boolean isEnchantedGoldenAppleCuresInfection() { return enchantedGoldenAppleCuresInfection; }

    // Server Config
    private static final ForgeConfigSpec.Builder BUILDER_SERVER = new ForgeConfigSpec.Builder();
    public static final double DEFAULT_CONTAGIOUS_FLESH_DROP_CHANCE = 0.075;
    public static final double DEFAULT_CONTAGIOUS_FLESH_LOOTING_BONUS = 0.025;

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

    private static final ForgeConfigSpec.BooleanValue INFINITE_WEAKNESS_AND_SLOWNESS = BUILDER_SERVER
            .comment("If true, the random effects weakness and slowness effects will last until the infection is cured or drinking milk")
            .define("infiniteWeaknessAndSlowness", false);

    private static final ForgeConfigSpec.IntValue IMMUNITY_DURATION = BUILDER_SERVER
            .comment("Time the immunity from new infections will last")
            .defineInRange("immunityDuration", 120, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.BooleanValue MILK_CURES_INFECTION = BUILDER_SERVER
            .comment("If true, drinking milk will cure the player if infected")
            .define("milkCuresInfection", false);

    private static final ForgeConfigSpec.BooleanValue TOTEM_PREVENTS_DYING_FROM_INFECTION = BUILDER_SERVER
            .comment("If true, holding a totem prevents the player from dying when the infection timer expires | default: true")
            .define("totemPreventsDyingFromInfection", true);

    private static final ForgeConfigSpec.BooleanValue IRON_GOLEM_ATTACKS_INFECTED = BUILDER_SERVER
            .comment("If true, Iron Golems will attack infected players")
            .define("ironGolemAttacksInfected", false);

    private static final ForgeConfigSpec.BooleanValue ALLOW_PLAYERS_TO_INFECT_PLAYERS = BUILDER_SERVER
            .comment("If true, infected players can infect other players by attacking them")
            .define("allowPlayersToInfectPlayers", false);

    private static final ForgeConfigSpec.BooleanValue ALLOW_PLAYERS_TO_INFECT_VILLAGERS = BUILDER_SERVER
            .comment("If true, infected players can infect Villagers by attacking them")
            .define("allowPlayersToInfectVillagers", false);

    private static final ForgeConfigSpec.BooleanValue ALLOW_ZOMBIES_TO_INFECT_VILLAGERS = BUILDER_SERVER
            .comment("If true, Zombies can infect Villagers by attacking them")
            .define("allowZombiesToInfectVillagers", false);

    private static final ForgeConfigSpec.BooleanValue VILLAGER_ZOMBIE_CONVERSION = BUILDER_SERVER
            .comment("If true, an infected Villager will convert into a Zombie Entity when it dies from the infection")
            .define("villagerZombieConversion", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_AIRBORNE_INFECTION = BUILDER_SERVER
            .comment("If true, enables airborne (aerosol) transmission of infection between nearby players")
            .define("enableAirborneInfection", false);

    private static final ForgeConfigSpec.BooleanValue ALLOW_AIRBORNE_INFECTION_FOR_VILLAGERS = BUILDER_SERVER
            .comment("If true, Villages are also able to transmit and receive the infection via airborne (aerosol) transmission")
            .define("allowAirborneInfectionForVillagers", false);

    private static final ForgeConfigSpec.BooleanValue VILLAGER_FLEE_FROM_INFECTIOUS_ENTITIES = BUILDER_SERVER
            .comment("If true, Villagers flee from infectious entities")
            .define("villagerFleeFromInfectiousEntities", true);

    private static final ForgeConfigSpec.BooleanValue ALLOW_ZOMBIES_TO_SPAWN_INFECTIOUS = BUILDER_SERVER
            .comment("If true, Zombies will spawn with the infectious effect (as Spreaders)")
            .define("allowZombiesToSpawnInfectious", false);

    private static final ForgeConfigSpec.IntValue CHANCE_FOR_ZOMBIE_TO_SPAWN_INFECTIOUS = BUILDER_SERVER
            .comment("Chance (X in 1000) for a Zombie to spawn with the infectious effect (as Spreaders)")
            .defineInRange("chanceForZombieToSpawnInfectious", 155, 1, 1000);

    private static final ForgeConfigSpec.IntValue AIRBORNE_INFECTION_RADIUS = BUILDER_SERVER
            .comment("Maximum radius (in blocks) for aerosol transmission of infection via breathing/spores (default: 6, min: 1)")
            .defineInRange("airborneInfectionRadius", 6, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue AIRBORNE_MAX_CHANCE = BUILDER_SERVER
            .comment("Maximum infection chance at (directly adjacent a spreader) for aerosol transmission (default: 50, range: 0-100)")
            .defineInRange("airborneMaxChance", 50, 0, 100);

    private static final ForgeConfigSpec.DoubleValue CONTAGIOUS_FLESH_DROP_CHANCE = BUILDER_SERVER
            .comment("Base drop chance for Contagious Flesh from zombie-type mobs (0.0 to 1.0)")
            .defineInRange("contagiousFleshDropChance", DEFAULT_CONTAGIOUS_FLESH_DROP_CHANCE, 0.0, 1.0);

    private static final ForgeConfigSpec.DoubleValue CONTAGIOUS_FLESH_LOOTING_BONUS = BUILDER_SERVER
            .comment("Looting bonus for Contagious Flesh drops per Looting level (0.0 to 1.0)")
            .defineInRange("contagiousFleshLootingBonus", DEFAULT_CONTAGIOUS_FLESH_LOOTING_BONUS, 0.0, 1.0);

    private static final ForgeConfigSpec.BooleanValue GOLDEN_APPLE_CURES_INFECTION = BUILDER_SERVER
            .comment("If true, eating a golden apple will cure the player's infection")
            .define("goldenAppleCuresInfection", false);

    private static final ForgeConfigSpec.BooleanValue ENCHANTED_GOLDEN_APPLE_CURES_INFECTION = BUILDER_SERVER
            .comment("If true, eating an enchanted golden apple (god apple) will cure the player's infection")
            .define("enchantedGoldenAppleCuresInfection", false);

    private static final ForgeConfigSpec.BooleanValue ENABLE_DEBUG_MESSAGES = BUILDER_SERVER
            .comment("If true, writes debug messages to the log")
            .define("enableDebugMessages", false);

    public static final ForgeConfigSpec SPEC_SERVER = BUILDER_SERVER.build();


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

    public static final ForgeConfigSpec SPEC_CLIENT = BUILDER_CLIENT.build();


    private static int infectionDuration;
    private static int baseInfectionChance;
    private static int minimumInfectionChance;
    private static boolean armorLowersInfectionChance;
    private static boolean enableRandomSymptoms;
    private static int randomSymptomsDuration;
    private static int randomSymptomsChance;
    private static boolean infiniteWeaknessAndSlowness;
    private static int immunityDuration;
    private static boolean milkCuresInfection;
    private static boolean totemPreventsDyingFromInfection;
    private static boolean ironGolemAttacksInfected;
    private static boolean allowPlayersToInfectPlayers;
    private static boolean allowPlayersToInfectVillagers;
    private static boolean allowZombiesToInfectVillagers;
    private static boolean villagerZombieConversion;
    private static boolean enableAirborneInfection;
    private static boolean allowAirborneInfectionForVillagers;
    private static boolean villagerFleeFromInfectiousEntities;
    private static boolean allowZombiesToSpawnInfectious;
    private static int chanceForZombieToSpawnInfectious;
    private static int airborneInfectionRadius;
    private static int airborneMaxChance;
    private static double contagiousFleshDropChance;
    private static double contagiousFleshLootingBonus;
    private static boolean goldenAppleCuresInfection;
    private static boolean enchantedGoldenAppleCuresInfection;
    private static boolean enableDebugMessages;
    private static boolean displayCurrentInfectionProtection;
    private static int deltaX;
    private static int deltaY;

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
            infiniteWeaknessAndSlowness = INFINITE_WEAKNESS_AND_SLOWNESS.get();

            immunityDuration = IMMUNITY_DURATION.get();
            // update cure potions with immunity value loaded from config file
            MobEffectInstance cureMobEffectInstance = new MobEffectInstance(ContagionEffects.IMMUNITY.get(), immunityDuration * 20, 0);
            MobEffectInstance longCureMobEffectInstance = new MobEffectInstance(ContagionEffects.IMMUNITY.get(), immunityDuration * 20 * 3, 0);
            ContagionPotions.CURE_POTION.get().getEffects().get(0).update(cureMobEffectInstance);
            ContagionPotions.LONG_CURE_POTION.get().getEffects().get(0).update(longCureMobEffectInstance);

            milkCuresInfection = MILK_CURES_INFECTION.get();
            totemPreventsDyingFromInfection = TOTEM_PREVENTS_DYING_FROM_INFECTION.get();
            ironGolemAttacksInfected = IRON_GOLEM_ATTACKS_INFECTED.get();
            allowPlayersToInfectPlayers = ALLOW_PLAYERS_TO_INFECT_PLAYERS.get();
            allowPlayersToInfectVillagers = ALLOW_PLAYERS_TO_INFECT_VILLAGERS.get();
            allowZombiesToInfectVillagers = ALLOW_ZOMBIES_TO_INFECT_VILLAGERS.get();
            villagerZombieConversion = VILLAGER_ZOMBIE_CONVERSION.get();
            enableAirborneInfection = ENABLE_AIRBORNE_INFECTION.get();
            allowAirborneInfectionForVillagers = ALLOW_AIRBORNE_INFECTION_FOR_VILLAGERS.get();
            villagerFleeFromInfectiousEntities = VILLAGER_FLEE_FROM_INFECTIOUS_ENTITIES.get();
            allowZombiesToSpawnInfectious = ALLOW_ZOMBIES_TO_SPAWN_INFECTIOUS.get();
            chanceForZombieToSpawnInfectious = CHANCE_FOR_ZOMBIE_TO_SPAWN_INFECTIOUS.get();
            airborneInfectionRadius = AIRBORNE_INFECTION_RADIUS.get();
            airborneMaxChance = AIRBORNE_MAX_CHANCE.get();
            contagiousFleshDropChance = CONTAGIOUS_FLESH_DROP_CHANCE.get();
            contagiousFleshLootingBonus = CONTAGIOUS_FLESH_LOOTING_BONUS.get();
            goldenAppleCuresInfection = GOLDEN_APPLE_CURES_INFECTION.get();
            enchantedGoldenAppleCuresInfection = ENCHANTED_GOLDEN_APPLE_CURES_INFECTION.get();
            enableDebugMessages = ENABLE_DEBUG_MESSAGES.get();
        }
        if (SPEC_CLIENT.isLoaded()) {
            displayCurrentInfectionProtection = DISPLAY_CURRENT_INFECTION_PROTECTION.get();
            deltaX = DELTA_X.get();
            deltaY = DELTA_Y.get();
        }
    }
}
