package net.petemc.contagion.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.casts.InfectedEntity;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.effect.ContagionInfectionEffect;

public class AerosolTransmissionEvent {

    private static final int TICK_INTERVAL = 100;
    private static final double EXPONENT_FACTOR = -0.1;

    public static void onLivingTick(LivingEntity source) {
        if (source.getWorld().isClient()) return;
        if (!MainConfig.isEnableAirborneInfection()) return;
        if (!source.hasStatusEffect(ContagionEffects.INFECTIOUS)) return;

        int tickCount = source.age;
        long entityId = source.getId();

        if ((tickCount + entityId) % TICK_INTERVAL != 0) return;

        if (!(source instanceof InfectedEntity || source instanceof ZombieEntity)) {
            return;
        }

        if ((source instanceof VillagerEntity) && !MainConfig.isAllowAirborneInfectionForVillagers()) {
            return;
        }

        String posStr = String.format("%.2f/%.2f/%.2f", source.getX(), source.getY(), source.getZ());
        debugLog("Source: {}, Pos: {}", source.getType().toString(), posStr);

        if (source.getWorld() instanceof ServerWorld serverLevel) {
            performAerosolSpread(serverLevel, source);
        }
    }

    private static void performAerosolSpread(ServerWorld serverLevel, LivingEntity source) {
        double maxRadius = MainConfig.getAirborneInfectionRadius();
        double maxRadiusSquared = maxRadius * maxRadius;
        int aerosolMaxChanceCfg = MainConfig.getAirborneMaxChance();
        if (aerosolMaxChanceCfg <= 0) aerosolMaxChanceCfg = 50;
        double maxChancePerTick = aerosolMaxChanceCfg / 100.0;

        String spreadPos = String.format("%.2f/%.2f/%.2f", source.getX(), source.getY(), source.getZ());
        debugLog("Spreading from {}, Radius: {}, MaxChancePerTick: {}", spreadPos, maxRadius, maxChancePerTick);

        java.util.List<PlayerEntity> nearbyPlayers = serverLevel.getEntitiesByClass(
                PlayerEntity.class,
                source.getBoundingBox().expand(maxRadius),
                e -> true);

        debugLog("Found {} nearby players", nearbyPlayers.size());

        for (PlayerEntity target : nearbyPlayers) {
            if (!target.isAlive() || source.equals(target)) continue;

            double distanceSquared = source.squaredDistanceTo(target);
            if (distanceSquared > maxRadiusSquared) continue;

            double prob = maxChancePerTick * Math.exp(EXPONENT_FACTOR * distanceSquared);

            if (target.hasStatusEffect(ContagionEffects.INFECTION)) continue;
            if (target.hasStatusEffect(ContagionEffects.IMMUNITY)) continue;

            float roll = serverLevel.getRandom().nextFloat();
            if (roll < prob) {
                debugLog("-> INFECTING player '{}'! pos={}/{} prob={} distSq={}",
                        target.getName().getString(),
                        String.format("%.2f", source.getX()),
                        String.format("%.2f", source.getY()),
                        prob,
                        String.format("%.2f", distanceSquared));
                applyInfectionToPlayer(target, source);
            } else {
                debugLog("-> roll failed (roll={} >= {}", roll, prob);
            }
        }

        java.util.List<VillagerEntity> nearbyVillagers = serverLevel.getEntitiesByClass(
                VillagerEntity.class,
                source.getBoundingBox().expand(maxRadius),
                e -> true);

        debugLog("Found {} nearby villagers", nearbyVillagers.size());

        for (VillagerEntity target : nearbyVillagers) {
            if (!target.isAlive() || source.equals(target)) continue;

            double distanceSquared = source.squaredDistanceTo(target);
            if (distanceSquared > maxRadiusSquared) continue;

            double prob = maxChancePerTick * Math.exp(EXPONENT_FACTOR * distanceSquared);

            if (target.hasStatusEffect(ContagionEffects.INFECTION)) continue;
            if (target.hasStatusEffect(ContagionEffects.IMMUNITY)) continue;

            float roll = serverLevel.getRandom().nextFloat();
            if (roll < prob) {
                debugLog("-> INFECTING villager at pos={}/{}! prob={} distSq={}",
                        String.format("%.2f", target.getX()),
                        String.format("%.2f", target.getY()),
                        prob,
                        String.format("%.2f", distanceSquared));
                applyInfectionToVillager(target, source);
            } else {
                debugLog("-> roll failed (roll={} >= {})", roll, prob);
            }
        }
    }

    private static void applyInfectionToPlayer(PlayerEntity target, LivingEntity source) {
        if (target.hasStatusEffect(ContagionEffects.INFECTION)) return;

        int duration = MainConfig.getInfectionDuration() * 20;
        target.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, duration, 0));
        debugLog("Added INFECTION effect (duration: {}) to player '{}'", duration, target.getName().getString());

        ContagionInfectionEffect.resetValues(target);
        target.sendMessage(Text.translatable("effect.contagion.infected_msg").formatted(Formatting.RED));
        if (target instanceof InfectedEntity infectedPlayer) {
            infectedPlayer.contagion_setInitialInfectionDuration(duration);
        }
    }

    private static void applyInfectionToVillager(VillagerEntity target, LivingEntity source) {
        int duration = MainConfig.getInfectionDuration() * 20;

        if (!target.getWorld().isClient()) {
            target.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, duration, 0));
            debugLog("Added INFECTION effect (duration: {}) to villager at pos={}/{}",
                    duration,
                    String.format("%.2f", target.getX()),
                    String.format("%.2f", target.getY()));
        }

        if (target instanceof InfectedEntity infectedVillager) {
            infectedVillager.contagion_setInfection(true);
            infectedVillager.contagion_setInfectionTicks(duration);
            infectedVillager.contagion_setInitialInfectionDuration(duration);
        }
    }

    private static void debugLog(String pFormat, Object... pArgs) {
        if (MainConfig.isEnableDebugMessages()) Contagion.LOGGER.info("[Aerosol] " + pFormat, pArgs);
    }
}
