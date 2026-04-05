package net.petemc.contagion.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.damage_type.ContagionDamageTypes;
import net.petemc.contagion.casts.InfectedPlayer;
import org.jetbrains.annotations.NotNull;

public class ContagionInfectionEffect extends MobEffect {
    public ContagionInfectionEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
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
            infectedPlayer.contagion_setInfection(false);
            infectedPlayer.contagion_setInfectionTicks((long) MainConfig.getInfectionDuration() * 20);
            infectedPlayer.contagion_setInfectionCooldown(defaultCooldown * 20);
        }
    }

    @Override
    public boolean applyEffectTick(@NotNull ServerLevel level, @NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!level.isClientSide()) {
            if (pLivingEntity instanceof InfectedPlayer infectedPlayer) {
                if (!pLivingEntity.isAlive()) {
                    infectedPlayer.contagion_setInfection(false);
                    return super.applyEffectTick(level, pLivingEntity, pAmplifier);
                }
                if (!infectedPlayer.contagion_isPlayerInfected()) {
                    MobEffectInstance effectInstance = pLivingEntity.getEffect(ContagionEffects.INFECTION);
                    long duration = (effectInstance != null && effectInstance.getDuration() > 0)
                            ? effectInstance.getDuration()
                            : (long) MainConfig.getInfectionDuration() * 20;
                    infectedPlayer.contagion_setInitialInfectionDuration(duration);
                    infectedPlayer.contagion_setInfectionTicks(duration);
                    infectedPlayer.contagion_setInfectionCooldown(defaultCooldown * 20);
                    infectedPlayer.contagion_setInfection(true);
                }
                infectedPlayer.contagion_setInfectionTicks(infectedPlayer.contagion_getInfectionTicks() - 1);
                if (infectedPlayer.contagion_getInfectionCooldown() != 0) {
                    infectedPlayer.contagion_setInfectionCooldown(infectedPlayer.contagion_getInfectionCooldown() - 1);
                }
                if (MainConfig.isEnableRandomSymptoms()) {
                    if ((infectedPlayer.contagion_getInfectionCooldown() == 0) && (infectedPlayer.contagion_getInfectionTicks() > (MainConfig.getRandomSymptomsDuration() * 20L))) {
                        if ((infectedPlayer.contagion_getInfectionTicks() % 20) == 0) {
                            int randomValue = RandomSource.create().nextIntBetweenInclusive(1, 100);
                            if (randomValue > (100 - MainConfig.getRandomSymptomsChance())) {
                                randomValue = RandomSource.create().nextIntBetweenInclusive(1, 4);
                                switch (randomValue) {
                                    case 1:
                                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, (MainConfig.getRandomSymptomsDuration() * 20), 0));
                                        break;
                                    case 2:
                                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, (MainConfig.getRandomSymptomsDuration() * 20), 0));
                                        break;
                                    case 3:
                                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, (MainConfig.getRandomSymptomsDuration() * 20), 0));
                                        break;
                                    case 4:
                                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, (MainConfig.getRandomSymptomsDuration() * 20), 0));
                                        break;
                                }
                                infectedPlayer.contagion_setInfectionCooldown((MainConfig.getRandomSymptomsDuration() + defaultCooldown) * 20);
                            }
                        }
                    } else if (infectedPlayer.contagion_getInfectionTicks() == (MainConfig.getRandomSymptomsDuration() * 20L / 2)) {
                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, (MainConfig.getRandomSymptomsDuration() * 20 / 2), 0));
                    }
                }

                if (infectedPlayer.contagion_getInfectionTicks() <= 2) {
                    if (MainConfig.isTotemPreventsDyingFromInfection()) {
                        if (!(pLivingEntity.getMainHandItem().is(Items.TOTEM_OF_UNDYING) || pLivingEntity.getOffhandItem().is(Items.TOTEM_OF_UNDYING))) {
                            infectedPlayer.contagion_setPlayerDiedFromInfection(true);
                        }
                        pLivingEntity.setHealth(1.0f);
                        pLivingEntity.hurtServer(level, ContagionDamageTypes.of(pLivingEntity.level(), ContagionDamageTypes.INFECTION), 1000.0f);
                    } else {
                        pLivingEntity.kill(level);
                        infectedPlayer.contagion_setPlayerDiedFromInfection(true);
                    }
                    infectedPlayer.contagion_setInfection(false);
                }
            }
        }
        return super.applyEffectTick(level, pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) { return true; }
}