package net.petemc.contagion.infection;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.ContagionConfigs;
import net.petemc.contagion.effect.ContagionEffects;

public class ContagionZombieAttackEffects {

    private static LivingEntity pPlayer = null;
    private static LivingEntity pAttacker = null;

    public ContagionZombieAttackEffects() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, damageSource, amount) -> {
            pPlayer = entity;
            pAttacker = (LivingEntity) damageSource.getAttacker();
            execute();
            return true;
        });
    }

    public static void execute() {
        if (pPlayer == null) {
            Contagion.LOGGER.warn("Failed to load Player entity!");
        } else if (pPlayer.getWorld() == null) {
            Contagion.LOGGER.warn("Failed to load World!");
        } else if (pAttacker != null) {
            int randomValue = MathHelper.nextInt(Random.create(), 1, 100);
            int effectiveInfectChance = getEffectiveInfectChance(pPlayer);
            if (pAttacker.getType() == EntityType.ZOMBIE && (randomValue > effectiveInfectChance)) {

                // Do nothing if Player is already infected
                if (pPlayer.hasStatusEffect(ContagionEffects.INFECTION)) {
                    return;
                }

                // Do nothing if the Player has the Resistance effect
                if (pPlayer.hasStatusEffect(ContagionEffects.RESISTANCE)) {
                    pPlayer.sendMessage(Text.translatable("effect.contagion.infection_resistance_msg"));
                    return;
                }

                // Give the Player the Infection effect
                if (!pPlayer.getWorld().isClient()) {
                    pPlayer.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, ContagionConfigs.DURATION_INFECTION_TOTAL * 20, 0));
                    pPlayer.sendMessage(Text.translatable("effect.contagion.infected_msg"));
                }
            }
        }
    }

    private static int getEffectiveInfectChance(LivingEntity _entity) {
        int effectInfect;
        if (ContagionConfigs.BASE_INFECTION_CHANCE > 100) {
            effectInfect = 0;
        } else {
            effectInfect = 100 - ContagionConfigs.BASE_INFECTION_CHANCE;
        }
        if (ContagionConfigs.ARMOR_PROTECTS) {
            effectInfect = effectInfect + (_entity.getArmor() * 3);
        }
        if (effectInfect > (100 - ContagionConfigs.MIN_INFECTION_CHANCE)) {
            effectInfect = 100 - ContagionConfigs.MIN_INFECTION_CHANCE;
        }
        return effectInfect;
    }

    public static void registerAttackEffects() {
        new ContagionZombieAttackEffects();
    }
}
