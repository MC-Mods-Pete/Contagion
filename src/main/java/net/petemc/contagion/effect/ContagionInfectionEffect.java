package net.petemc.contagion.effect;

import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.EffectCure;
import net.petemc.contagion.Config;
import net.petemc.contagion.damage_type.ContagionDamageTypes;
import net.petemc.contagion.casts.InfectedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ContagionInfectionEffect extends MobEffect {
    public ContagionInfectionEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    private static final long defaultCooldown = 60;

    @Override
    public void fillEffectCures(@NotNull Set<EffectCure> cures, @NotNull MobEffectInstance effectInstance) {
        if (Config.milkCuresInfection) {
            super.fillEffectCures(cures, effectInstance);
        }
    }

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
            infectedPlayer.contagion_setInfectionTicks((long) Config.infectionDuration * 20);
            infectedPlayer.contagion_setInfectionCooldown(defaultCooldown * 20);
        }
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity instanceof InfectedPlayer infectedPlayer) {
                if (!infectedPlayer.contagion_isPlayerInfected()) {
                    infectedPlayer.contagion_setInfectionTicks((long) Config.infectionDuration * 20);
                    infectedPlayer.contagion_setInfectionCooldown(defaultCooldown * 20);
                    infectedPlayer.contagion_setInfection(true);
                }
                infectedPlayer.contagion_setInfectionTicks(infectedPlayer.contagion_getInfectionTicks() - 1);
                if (infectedPlayer.contagion_getInfectionCooldown() != 0) {
                    infectedPlayer.contagion_setInfectionCooldown(infectedPlayer.contagion_getInfectionCooldown() - 1);
                }
                if (Config.enableRandomSymptoms) {
                    if ((infectedPlayer.contagion_getInfectionCooldown() == 0) && (infectedPlayer.contagion_getInfectionTicks() > (Config.randomSymptomsDuration * 20L))) {
                        if ((infectedPlayer.contagion_getInfectionTicks() % 20) == 0) {
                            int randomValue = RandomSource.create().nextIntBetweenInclusive(1, 100);
                            if (randomValue > (100 - Config.randomSymptomsChance)) {
                                randomValue = RandomSource.create().nextIntBetweenInclusive(1, 4);
                                switch (randomValue) {
                                    case 1:
                                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, (Config.randomSymptomsDuration * 20), 0));
                                        break;
                                    case 2:
                                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (Config.randomSymptomsDuration * 20), 0));
                                        break;
                                    case 3:
                                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, (Config.randomSymptomsDuration * 20), 0));
                                        break;
                                    case 4:
                                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, (Config.randomSymptomsDuration * 20), 0));
                                        break;
                                }
                                infectedPlayer.contagion_setInfectionCooldown((Config.randomSymptomsDuration + defaultCooldown) * 20);
                            }
                        }
                    } else if (infectedPlayer.contagion_getInfectionTicks() == (Config.randomSymptomsDuration * 20L / 2)) {
                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, (Config.randomSymptomsDuration * 20 / 2), 0));
                    }
                }

                if (infectedPlayer.contagion_getInfectionTicks() <= 2) {
                    if (Config.totemPreventsDyingFromInfection) {
                        if (!(pLivingEntity.getMainHandItem().is(Items.TOTEM_OF_UNDYING) || pLivingEntity.getOffhandItem().is(Items.TOTEM_OF_UNDYING))) {
                            infectedPlayer.contagion_setPlayerDiedFromInfection(true);
                        }
                        pLivingEntity.hurt(ContagionDamageTypes.of(pLivingEntity.level(), ContagionDamageTypes.INFECTION), 1000.0f);
                    } else {
                        infectedPlayer.contagion_setPlayerDiedFromInfection(true);
                        pLivingEntity.kill();
                    }
                    infectedPlayer.contagion_setInfection(false);
                }
            }
        }
        return super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) { return true; }
}