package net.petemc.contagion.effect;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.VillagerData;
import net.petemc.contagion.casts.InfectedEntity;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.damage_type.ContagionDamageTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContagionInfectionEffect extends StatusEffect {
    public ContagionInfectionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    private static final long defaultCooldown = 60;

    public long getTicks(LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof InfectedEntity infectedPlayer) {
            return infectedPlayer.contagion_getInfectionTicks();
        }
        return 0;
    }

    public void setTicks(LivingEntity pLivingEntity, long ticksValue) {
        if (pLivingEntity instanceof InfectedEntity infectedPlayer) {
            infectedPlayer.contagion_setInfectionTicks(ticksValue);
        }
    }

    public static void resetValues(LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof InfectedEntity infectedPlayer) {
            StatusEffectInstance existingEffect = pLivingEntity.getStatusEffect(ContagionEffects.INFECTION);
            long duration = (existingEffect != null && existingEffect.getDuration() > 0L) 
                    ? (int) existingEffect.getDuration() 
                    : (long) MainConfig.getInfectionDuration() * 20;
            infectedPlayer.contagion_setInfection(false);
            if (infectedPlayer.contagion_getInitialInfectionDuration() == 0L) {
                infectedPlayer.contagion_setInitialInfectionDuration(duration);
            }
            infectedPlayer.contagion_setInfectionTicks(duration);
            infectedPlayer.contagion_setInfectionCooldown(defaultCooldown * 20);
        }
    }

    @Override
    public void applyUpdateEffect(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.getEntityWorld().isClient()) {
            if (pLivingEntity instanceof InfectedEntity infectedEntity) {
                if (!pLivingEntity.isAlive()) {
                    infectedEntity.contagion_setInfection(false);
                    return;
                }
                if (!infectedEntity.contagion_isPlayerInfected()) {
                    StatusEffectInstance effectInstance = pLivingEntity.getStatusEffect(ContagionEffects.INFECTION);
                    long duration = (effectInstance != null && effectInstance.getDuration() > 0)
                            ? effectInstance.getDuration()
                            : (long) MainConfig.getInfectionDuration() * 20;
                    infectedEntity.contagion_setInitialInfectionDuration(duration);
                    infectedEntity.contagion_setInfectionTicks(duration);
                    infectedEntity.contagion_setInfectionCooldown(defaultCooldown * 20);
                    infectedEntity.contagion_setInfection(true);
                }
                long currentTicks = infectedEntity.contagion_getInfectionTicks();
                infectedEntity.contagion_setInfectionTicks(currentTicks - 1);

                // Apply INFECTIOUS effect when reaching the last 40% of infection duration
                applyInfectiousEffect(pLivingEntity, infectedEntity);

                if (infectedEntity.contagion_getInfectionCooldown() != 0) {
                    infectedEntity.contagion_setInfectionCooldown(infectedEntity.contagion_getInfectionCooldown() - 1);
                }
                if (MainConfig.isEnableRandomSymptoms()) {
                    if ((infectedEntity.contagion_getInfectionCooldown() == 0) && (infectedEntity.contagion_getInfectionTicks() > (MainConfig.getRandomSymptomsDuration() * 20L))) {
                        if ((infectedEntity.contagion_getInfectionTicks() % 20) == 0) {
                            int randomValue = MathHelper.nextInt(Random.create(), 1, 100);
                            if (randomValue > (100 - MainConfig.getRandomSymptomsChance())) {
                                randomValue = MathHelper.nextInt(Random.create(), 1, 4);
                                switch (randomValue) {
                                    case 1:
                                        if (!MainConfig.isInfiniteWeaknessAndSlowness()) {
                                            pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, (MainConfig.getRandomSymptomsDuration() * 20), 0));
                                        } else {
                                            pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, -1, 0));
                                        }
                                        break;
                                    case 2:
                                        if (!MainConfig.isInfiniteWeaknessAndSlowness()) {
                                            pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, (MainConfig.getRandomSymptomsDuration() * 20), 0));
                                        } else {
                                            pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, -1, 0));
                                        }
                                        break;
                                    case 3:
                                        pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, (MainConfig.getRandomSymptomsDuration() * 20), 0));
                                        break;
                                    case 4:
                                        pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, (MainConfig.getRandomSymptomsDuration() * 20), 0));
                                        break;
                                }
                                infectedEntity.contagion_setInfectionCooldown((MainConfig.getRandomSymptomsDuration() + defaultCooldown) * 20);
                            }
                        }
                    } else if (infectedEntity.contagion_getInfectionTicks() == (MainConfig.getRandomSymptomsDuration() * 20L / 2)) {
                        pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, (MainConfig.getRandomSymptomsDuration() * 20 / 2), 0));
                    }
                }

                if (infectedEntity.contagion_getInfectionTicks() <= 2) {
                    // Check if this is a Villager that should convert into a ZombieVillager instead of dying normally.
                    boolean villagerConverting = false;
                    if (MainConfig.isVillagerZombieConversion() && pLivingEntity instanceof VillagerEntity) {
                        villagerConverting = true;
                    }

                    if (!villagerConverting) {
                        // Normal death path for non-Villagers (e.g. players etc) -- keep totem logic intact.
                        if (MainConfig.isTotemPreventsDyingFromInfection()) {
                            if (!(pLivingEntity.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING) || pLivingEntity.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING))) {
                                infectedEntity.contagion_setPlayerDiedFromInfection(true);
                                removeInfectionEffects(pLivingEntity, infectedEntity);
                            }
                            pLivingEntity.setHealth(1.0f);
                            pLivingEntity.damage(ContagionDamageTypes.of(pLivingEntity.getWorld(), ContagionDamageTypes.INFECTION), 1000.0f);
                        } else {
                            infectedEntity.contagion_setPlayerDiedFromInfection(true);
                            removeInfectionEffects(pLivingEntity, infectedEntity);
                            pLivingEntity.kill();
                        }
                    }

                    if (villagerConverting && !infectedEntity.contagion_playerDiedFromInfection()) {
                        if (pLivingEntity instanceof VillagerEntity originalVillager) {
                            VillagerData villagerData = originalVillager.getVillagerData();
                            ZombieVillagerEntity zombie = new ZombieVillagerEntity(EntityType.ZOMBIE_VILLAGER, pLivingEntity.getWorld());
                            zombie.setPos(originalVillager.getX(), originalVillager.getY(), originalVillager.getZ());
                            zombie.setAttacking(true);
                            zombie.setVillagerData(villagerData);
                            pLivingEntity.getWorld().spawnEntity(zombie);
                            removeInfectionEffects(pLivingEntity, infectedEntity);
                            pLivingEntity.kill();
                        }
                    }

                    infectedEntity.contagion_setPlayerDiedFromInfection(true);
                    infectedEntity.contagion_setInfection(false);
                    removeInfectionEffects(pLivingEntity, infectedEntity);
                }
            }
        }
        super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    /**
     * Apply the INFECTIOUS mob effect when infection reaches >= 40% progress (i.e., ticks <= initialDuration * 0.6).
     * Duration is calculated so that both INFECTION and INFECTIOUS expire at the same time.
     */
    private void applyInfectiousEffect(LivingEntity pLivingEntity, InfectedEntity infectedEntity) {
        int infectiousThreshold = (int)(infectedEntity.contagion_getInitialInfectionDuration() * 0.6);
        long ticksRemaining = infectedEntity.contagion_getInfectionTicks();
        if (!pLivingEntity.hasStatusEffect(ContagionEffects.INFECTIOUS) && ticksRemaining <= infectiousThreshold) {
            // Apply for exactly the remaining ticks of the infection so both expire simultaneously
            pLivingEntity.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTIOUS, (int)(ticksRemaining + 1), 0));
        }
    }

    /** Remove INFECTIOUS effect from an entity. */
    private void removeInfectionEffects(LivingEntity pLivingEntity, InfectedEntity infectedEntity) {
        pLivingEntity.removeStatusEffect(ContagionEffects.INFECTIOUS);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}
