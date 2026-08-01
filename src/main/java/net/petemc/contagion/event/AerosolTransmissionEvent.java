package net.petemc.contagion.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.casts.InfectedEntity;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.effect.ContagionInfectionEffect;

public class AerosolTransmissionEvent {

    private static final int TICK_INTERVAL = 100;
    private static final double EXPONENT_FACTOR = -0.1;

    public static void onLivingTick(LivingEntity source) {
        if (source.level().isClientSide()) return;
        if (!MainConfig.isEnableAirborneInfection()) return;
        if (!source.hasEffect(ContagionEffects.INFECTIOUS)) return;

        int tickCount = source.tickCount;
        long entityId = source.getId();

        if ((tickCount + entityId) % TICK_INTERVAL != 0) return;

        if (!(source instanceof InfectedEntity || source instanceof Zombie)) {
            return;
        }

        if ((source instanceof Villager) && !MainConfig.isAllowAirborneInfectionForVillagers()) {
            return;
        }

        String posStr = String.format("%.2f/%.2f/%.2f", source.getX(), source.getY(), source.getZ());
        debugLog("Source: {}, Pos: {}", source.getType().toString(), posStr);

        if (source.level() instanceof ServerLevel serverLevel) {
            performAerosolSpread(serverLevel, source);
        }
    }

    /**
     * Core aerosol spread logic -- scans for both nearby Players and Villagers using getEntitiesOfClass(),
     * then applies exponential probability to each target based on distance.
     */
    private static void performAerosolSpread(ServerLevel serverLevel, LivingEntity source) {
        double maxRadius = MainConfig.getAirborneInfectionRadius();
        double maxRadiusSquared = maxRadius * maxRadius;
        int aerosolMaxChanceCfg = MainConfig.getAirborneMaxChance();
        if (aerosolMaxChanceCfg <= 0) aerosolMaxChanceCfg = 50; // Safe default when config is still 0 or not loaded.
        double maxChancePerTick = aerosolMaxChanceCfg / 100.0;

        String spreadPos = String.format("%.2f/%.2f/%.2f", source.getX(), source.getY(), source.getZ());
        debugLog("Spreading from {}, Radius: {}, MaxChancePerTick: {}",
                spreadPos, maxRadius, maxChancePerTick);

        // Targeted Search -- use getEntitiesOfClass() instead of iterating all world entities.
        java.util.List<ServerPlayer> nearbyPlayers = serverLevel.getEntitiesOfClass(
                ServerPlayer.class,
                source.getBoundingBox().inflate(maxRadius));

        debugLog("Found {} nearby players", nearbyPlayers.size());

        for (ServerPlayer target : nearbyPlayers) {
            if (!target.isAlive() || source.equals(target)) continue;

            double distanceSquared = source.distanceToSqr(target);
            if (distanceSquared > maxRadiusSquared) continue;

            double prob = maxChancePerTick * Math.exp(EXPONENT_FACTOR * distanceSquared);

            if (target.hasEffect(ContagionEffects.INFECTION)) {
                continue;
            }

            if (target.hasEffect(ContagionEffects.IMMUNITY)) {
                continue;
            }

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

        java.util.List<Villager> nearbyVillagers = serverLevel.getEntitiesOfClass(
                Villager.class,
                source.getBoundingBox().inflate(maxRadius));

        debugLog("Found {} nearby villagers", nearbyVillagers.size());

        for (Villager target : nearbyVillagers) {
            if (!target.isAlive() || source.equals(target)) continue;

            double distanceSquared = source.distanceToSqr(target);
            if (distanceSquared > maxRadiusSquared) continue;

            double prob = maxChancePerTick * Math.exp(EXPONENT_FACTOR * distanceSquared);

            if (target.hasEffect(ContagionEffects.INFECTION)) {
                continue;
            }

            if (target.hasEffect(ContagionEffects.IMMUNITY)) {
                continue;
            }

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

    /** Apply the Infection effect to a target Player -- mirrors ZombieEntityMixin's application logic. */
    private static void applyInfectionToPlayer(ServerPlayer target, LivingEntity source) {
        if (target.hasEffect(ContagionEffects.INFECTION)) return; // Already infected -- don't waste ticks re-applying the same effect

        int duration = MainConfig.getInfectionDuration() * 20;
        target.addEffect(new MobEffectInstance(ContagionEffects.INFECTION, duration, 0));
        debugLog("Added INFECTION effect (duration: {}) to player '{}'", duration, target.getName().getString());

        ContagionInfectionEffect.resetValues(target);
        target.sendSystemMessage(Component.translatable("effect.contagion.infected_msg").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
        if (target instanceof InfectedEntity infectedPlayer) {
            infectedPlayer.contagion_setInitialInfectionDuration(duration);
        }
    }

    /** Apply the Infection effect to a target Villager and update tracked infection state via InfectedEntity interface. */
    private static void applyInfectionToVillager(Villager target, LivingEntity source) {
        int duration = MainConfig.getInfectionDuration() * 20;

        if (!target.level().isClientSide()) {
            target.addEffect(new MobEffectInstance(ContagionEffects.INFECTION, duration, 0));
            debugLog("Added INFECTION effect (duration: {}) to villager at pos={}/{}",
                    duration,
                    String.format("%.2f", target.getX()),
                    String.format("%.2f", target.getY()));
        }

        if (target instanceof InfectedEntity infectedVillager) {
            infectedVillager.contagion_setInfection(true); // Mark villager as infected server-side.
            infectedVillager.contagion_setInfectionTicks(duration); // Set tick-decrement timer for this villager.
            infectedVillager.contagion_setInitialInfectionDuration(duration); // Record initial duration to check against it later on the server.
        }
    }

    /** Guarded debug logger only active when enableDebugMessages is true in MainConfig. */
    private static void debugLog(String pFormat, Object... pArgs) {
        if (MainConfig.isEnableDebugMessages()) Contagion.LOGGER.info("[Aerosol] " + pFormat, pArgs);
    }
}