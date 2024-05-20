package net.petemc.contagion.config;

import com.mojang.datafixers.util.Pair;
import net.petemc.contagion.Contagion;

public class ContagionConfigs {
    public static SimpleConfig CONFIG;
    private static ContagionConfigProvider configs;

    public static int DURATION_INFECTION_TOTAL;
    public static int DURATION_INFECTION_SYMPTOMS;
    public static int BASE_INFECTION_CHANCE;
    public static int MIN_INFECTION_CHANCE;
    public static int RANDOM_SYMPTOMS_CHANCE;
    public static boolean RANDOM_SYMPTOMS;
    public static boolean ARMOR_PROTECTS;
    public static int DURATION_RESISTANCE;

    public static void registerConfigs() {
        configs = new ContagionConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(Contagion.MOD_ID + "_config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("infection.duration.total", 900), "seconds");
        configs.addKeyValuePair(new Pair<>("infection.duration.symptoms", 60), "seconds");
        configs.addKeyValuePair(new Pair<>("infection.chance.base", 80), "percent");
        configs.addKeyValuePair(new Pair<>("infection.chance.min", 10), "percent");
        configs.addKeyValuePair(new Pair<>("infection.chance.random.symptoms", 3), "percent");
        configs.addKeyValuePair(new Pair<>("infection.random.symptoms", true), "boolean");
        configs.addKeyValuePair(new Pair<>("infection.armor.protection", true), "boolean");
        configs.addKeyValuePair(new Pair<>("resistance.duration", 120), "seconds");
    }

    private static void assignConfigs() {
        DURATION_INFECTION_TOTAL = CONFIG.getOrDefault("infection.duration.total", 900);
        DURATION_INFECTION_SYMPTOMS = CONFIG.getOrDefault("infection.duration.symptoms", 60);
        BASE_INFECTION_CHANCE = CONFIG.getOrDefault("infection.chance.base", 80);
        MIN_INFECTION_CHANCE = CONFIG.getOrDefault("infection.chance.min", 10);
        RANDOM_SYMPTOMS_CHANCE = CONFIG.getOrDefault("infection.chance.random.symptoms", 3);
        RANDOM_SYMPTOMS = CONFIG.getOrDefault("infection.random.symptoms", true);
        ARMOR_PROTECTS = CONFIG.getOrDefault("infection.armor.protection", true);
        DURATION_RESISTANCE = CONFIG.getOrDefault("resistance.duration", 120);

        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }
}
