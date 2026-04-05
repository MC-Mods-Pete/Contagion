package net.petemc.contagion.config;

import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.item.ContagionConsumables;
import net.petemc.contagion.potion.ContagionPotions;

@EventBusSubscriber(modid = Contagion.MOD_ID)
public class MainConfig
{
    // Getter
    public static int getInfectionDuration() { return infectionDuration; }
    public static int getBaseInfectionChance() { return baseInfectionChance; }
    public static int getMinimumInfectionChance() { return minimumInfectionChance; }
    public static boolean isArmorLowersInfectionChance() { return armorLowersInfectionChance; }
    public static boolean isEnableRandomSymptoms() { return enableRandomSymptoms; }
    public static int getRandomSymptomsDuration() { return randomSymptomsDuration; }
    public static int getRandomSymptomsChance() { return randomSymptomsChance; }
    public static int getImmunityDuration() { return immunityDuration; }
    public static boolean isMilkCuresInfection() { return milkCuresInfection; }
    public static boolean isTotemPreventsDyingFromInfection() { return totemPreventsDyingFromInfection; }
    public static boolean isIronGolemAttacksInfected() { return ironGolemAttacksInfected; }
    public static boolean isDisplayCurrentInfectionProtection() { return displayCurrentInfectionProtection; }
    public static int getDeltaX() { return deltaX; }
    public static int getDeltaY() { return deltaY; }

    // Server Config
    private static final ModConfigSpec.Builder BUILDER_SERVER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue INFECTION_DURATION = BUILDER_SERVER
            .comment("Infection duration")
            .defineInRange("infectionDuration", 600, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue BASE_INFECTION_CHANCE = BUILDER_SERVER
            .comment("Base infection chance")
            .defineInRange("baseInfectionChance", 80, 0, 100);

    private static final ModConfigSpec.IntValue MINIMUM_INFECTION_CHANCE = BUILDER_SERVER
            .comment("Minimum infection chance")
            .defineInRange("minimumInfectionChance", 10, 0, 100);

    private static final ModConfigSpec.BooleanValue ARMOR_LOWERS_INFECTION_CHANCE = BUILDER_SERVER
            .comment("If true, wearing armor will lower the infection chance (full Netherite will lower the chance by 60%)")
            .define("armorLowersInfectionChance", true);

    private static final ModConfigSpec.BooleanValue ENABLE_RANDOM_SYMPTOMS = BUILDER_SERVER
            .comment("If true, random symptoms can occur when infected")
            .define("enableRandomSymptoms", true);

    private static final ModConfigSpec.IntValue RANDOM_SYMPTOMS_DURATION = BUILDER_SERVER
            .comment("Time in seconds a random symptom will last")
            .defineInRange("randomSymptomsDuration", 30, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue RANDOM_SYMPTOMS_CHANCE = BUILDER_SERVER
            .comment("Chance for a random symptom to occur when infected")
            .defineInRange("randomSymptomsChance", 3, 0, 100);

    private static final ModConfigSpec.IntValue IMMUNITY_DURATION = BUILDER_SERVER
            .comment("Time the immunity from new infections will last")
            .defineInRange("immunityDuration", 120, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue MILK_CURES_INFECTION = BUILDER_SERVER
            .comment("If true, drinking milk will cure the player if infected")
            .define("milkCuresInfection", false);

    private static final ModConfigSpec.BooleanValue TOTEM_PREVENTS_DYING_FROM_INFECTION = BUILDER_SERVER
            .comment("If false, holding a totem will not prevent the player from dying when the infection timer runs out")
            .define("totemPreventsDyingFromInfection", true);

    private static final ModConfigSpec.BooleanValue IRON_GOLEM_ATTACKS_INFECTED = BUILDER_SERVER
            .comment("If true, Iron Golems will attack infected players")
            .define("ironGolemAttacksInfected", false);

    public static final ModConfigSpec SPEC_SERVER = BUILDER_SERVER.build();


    // Server Config
    private static final ModConfigSpec.Builder BUILDER_CLIENT = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue DISPLAY_CURRENT_INFECTION_PROTECTION = BUILDER_CLIENT
            .comment("[Client] If true, displays the current protection value in the HUD")
            .define("displayCurrentInfectionProtection", true);

    private static final ModConfigSpec.IntValue DELTA_X = BUILDER_CLIENT
            .comment("[Client] Move the displayed protection value in the HUD in X-direction")
            .defineInRange("deltaX", 0, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue DELTA_Y = BUILDER_CLIENT
            .comment("[Client] Move the displayed protection value in the HUD in Y-direction")
            .defineInRange("deltaY", 0, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC_CLIENT = BUILDER_CLIENT.build();


    private static int infectionDuration;
    private static int baseInfectionChance;
    private static int minimumInfectionChance;
    private static boolean armorLowersInfectionChance;
    private static boolean enableRandomSymptoms;
    private static int randomSymptomsDuration;
    private static int randomSymptomsChance;
    private static int immunityDuration;
    private static boolean milkCuresInfection;
    private static boolean totemPreventsDyingFromInfection;
    private static boolean ironGolemAttacksInfected;
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

            immunityDuration = IMMUNITY_DURATION.get();
            // update cure potions with immunity value loaded from config file
            MobEffectInstance cureMobEffectInstance = new MobEffectInstance(ContagionEffects.IMMUNITY, immunityDuration * 20, 0);
            MobEffectInstance longCureMobEffectInstance = new MobEffectInstance(ContagionEffects.IMMUNITY, immunityDuration * 20 * 3, 0);
            ContagionPotions.CURE_POTION.getDelegate().value().getEffects().getFirst().update(cureMobEffectInstance);
            ContagionPotions.LONG_CURE_POTION.getDelegate().value().getEffects().getFirst().update(longCureMobEffectInstance);

            milkCuresInfection = MILK_CURES_INFECTION.get();
            totemPreventsDyingFromInfection = TOTEM_PREVENTS_DYING_FROM_INFECTION.get();
            ironGolemAttacksInfected = IRON_GOLEM_ATTACKS_INFECTED.get();

            // update CONTAGIOUS_FLESH infection effect with duration loaded from config file
            ContagionConsumables.CONTAGIOUS_FLESH_INFECTION_EFFECT.update(
                    new MobEffectInstance(ContagionEffects.INFECTION, infectionDuration * 20, 0));
        }
        if (SPEC_CLIENT.isLoaded()) {
            displayCurrentInfectionProtection = DISPLAY_CURRENT_INFECTION_PROTECTION.get();
            deltaX = DELTA_X.get();
            deltaY = DELTA_Y.get();
        }
    }
}
