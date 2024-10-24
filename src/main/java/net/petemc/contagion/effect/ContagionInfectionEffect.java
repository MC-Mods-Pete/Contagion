package net.petemc.contagion.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.petemc.contagion.config.ContagionConfig;
import net.petemc.contagion.damage_type.ContagionDamageTypes;
import net.petemc.contagion.casts.InfectedPlayer;

public class ContagionInfectionEffect extends StatusEffect {
    public ContagionInfectionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    private static final long defaultCooldown = 60;

    public long getTicks(LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof InfectedPlayer infectedPlayer) {
            return infectedPlayer.contagion_getInfectionTicks();
        }
        return 0;
    }

    public void setTicks(LivingEntity pLivingEntity, long ticksValue) {
        if (pLivingEntity instanceof InfectedPlayer infectedPlayer) {
            infectedPlayer.contagion_setInfectionTicks(ticksValue);
        }
    }

    public static void resetValues(LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof InfectedPlayer infectedPlayer) {
            infectedPlayer.contagion_setInfectionTicks((long) ContagionConfig.INSTANCE.infectionDuration * 20);
            infectedPlayer.contagion_setInfectionCooldown(defaultCooldown * 20);
        }
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.getEntityWorld().isClient()) {
            if (pLivingEntity instanceof InfectedPlayer infectedPlayer) {
                if (!infectedPlayer.contagion_isPlayerInfected()) {
                    infectedPlayer.contagion_setInfectionTicks((long) ContagionConfig.INSTANCE.infectionDuration * 20);
                    infectedPlayer.contagion_setInfectionCooldown(defaultCooldown * 20);
                    infectedPlayer.contagion_setInfection(true);
                }
                infectedPlayer.contagion_setInfectionTicks(infectedPlayer.contagion_getInfectionTicks() - 1);
                if (infectedPlayer.contagion_getInfectionCooldown() != 0) {
                    infectedPlayer.contagion_setInfectionCooldown(infectedPlayer.contagion_getInfectionCooldown() - 1);
                }
                if (ContagionConfig.INSTANCE.enableRandomSymptoms) {
                    if ((infectedPlayer.contagion_getInfectionCooldown() == 0) && (infectedPlayer.contagion_getInfectionTicks() > (ContagionConfig.INSTANCE.randomSymptomsDuration * 20L))) {
                        if ((infectedPlayer.contagion_getInfectionTicks() % 20) == 0) {
                            int randomValue = MathHelper.nextInt(Random.create(), 1, 100);
                            if (randomValue > (100 - ContagionConfig.INSTANCE.randomSymptomsChance)) {
                                randomValue = MathHelper.nextInt(Random.create(), 1, 4);
                                switch (randomValue) {
                                    case 1:
                                        pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, (ContagionConfig.INSTANCE.randomSymptomsDuration * 20), 0));
                                        break;
                                    case 2:
                                        pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, (ContagionConfig.INSTANCE.randomSymptomsDuration * 20), 0));
                                        break;
                                    case 3:
                                        pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, (ContagionConfig.INSTANCE.randomSymptomsDuration * 20), 0));
                                        break;
                                    case 4:
                                        pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, (ContagionConfig.INSTANCE.randomSymptomsDuration * 20), 0));
                                        break;
                                }
                                infectedPlayer.contagion_setInfectionCooldown((ContagionConfig.INSTANCE.randomSymptomsDuration + defaultCooldown) * 20);
                            }
                        }
                    } else if (infectedPlayer.contagion_getInfectionTicks() == (ContagionConfig.INSTANCE.randomSymptomsDuration * 20L / 2)) {
                        pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, (ContagionConfig.INSTANCE.randomSymptomsDuration * 20 / 2), 0));
                    }
                }

                if (infectedPlayer.contagion_getInfectionTicks() <= 2) {
                    if (ContagionConfig.INSTANCE.totemPreventsDyingFromInfection) {
                        pLivingEntity.damage(world, ContagionDamageTypes.of(pLivingEntity.getWorld(), ContagionDamageTypes.INFECTION), 1000.0f);
                    } else {
                        pLivingEntity.kill(world);
                    }
                    infectedPlayer.contagion_setInfection(false);
                }
            }
        }
        return super.applyUpdateEffect(world, pLivingEntity, pAmplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}