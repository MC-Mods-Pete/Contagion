package net.petemc.contagion.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.petemc.contagion.config.ContagionConfig;
import net.petemc.contagion.damage_type.ContagionDamageTypes;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class ContagionInfectionEffect extends StatusEffect {
    public ContagionInfectionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    private static final long defaultCooldown = 60;

    public static ConcurrentHashMap<UUID, Long> ticksByPlayerUUID = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, Long> cooldownByPlayerUUID = new ConcurrentHashMap<>();


    public static void resetValues(LivingEntity pLivingEntity) {
        ticksByPlayerUUID.put(pLivingEntity.getUuid(), (long) ContagionConfig.INSTANCE.infectionDuration * 20);
        cooldownByPlayerUUID.put(pLivingEntity.getUuid(), defaultCooldown * 20);
    }


    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.getEntityWorld().isClient()) {
            if (!ticksByPlayerUUID.containsKey(pLivingEntity.getUuid())) {
                ticksByPlayerUUID.put(pLivingEntity.getUuid(), (long) ContagionConfig.INSTANCE.infectionDuration * 20);
            }
            if (!cooldownByPlayerUUID.containsKey(pLivingEntity.getUuid())) {
                cooldownByPlayerUUID.put(pLivingEntity.getUuid(), defaultCooldown * 20);
            }
            ticksByPlayerUUID.put(pLivingEntity.getUuid(), ticksByPlayerUUID.get(pLivingEntity.getUuid()) - 1);
            if (cooldownByPlayerUUID.get(pLivingEntity.getUuid()) != 0) {
                cooldownByPlayerUUID.put(pLivingEntity.getUuid(), cooldownByPlayerUUID.get(pLivingEntity.getUuid()) - 1);
            }
            if (ContagionConfig.INSTANCE.enableRandomSymptoms) {
                if ((cooldownByPlayerUUID.get(pLivingEntity.getUuid()) == 0) && (ticksByPlayerUUID.get(pLivingEntity.getUuid()) > (ContagionConfig.INSTANCE.randomSymptomsDuration * 20L))) {
                    if ((ticksByPlayerUUID.get(pLivingEntity.getUuid()) % 20) == 0) {
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
                            cooldownByPlayerUUID.put(pLivingEntity.getUuid(), (ContagionConfig.INSTANCE.randomSymptomsDuration + defaultCooldown) * 20);
                        }
                    }
                } else if (ticksByPlayerUUID.get(pLivingEntity.getUuid()) == (ContagionConfig.INSTANCE.randomSymptomsDuration * 20L / 2)) {
                    pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, (ContagionConfig.INSTANCE.randomSymptomsDuration * 20 / 2), 0));
                }
            }

            if (ticksByPlayerUUID.get(pLivingEntity.getUuid()) <= 2) {
                if (ContagionConfig.INSTANCE.totemPreventsDyingFromInfection) {
                    pLivingEntity.damage(ContagionDamageTypes.of(pLivingEntity.getWorld(), ContagionDamageTypes.INFECTION), 1000.0f);
                } else {
                    pLivingEntity.kill();
                }
                ticksByPlayerUUID.remove(pLivingEntity.getUuid());
                cooldownByPlayerUUID.remove(pLivingEntity.getUuid());
            }
        }
        super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}