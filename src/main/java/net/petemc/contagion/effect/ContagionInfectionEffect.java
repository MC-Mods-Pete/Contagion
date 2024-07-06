package net.petemc.contagion.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.ContagionConfig;
import net.petemc.contagion.damage_type.ContagionDamageTypes;

public class ContagionInfectionEffect extends StatusEffect {
    public ContagionInfectionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    private final long defaultCooldown = 60;

    private long ticks = (long) ContagionConfig.INSTANCE.infectionDuration * 20;
    private long coolDown = defaultCooldown * 20;

    public long getTicks() {
        return ticks;
    }

    public void setTicks(long ticksVal) {
        ticks = ticksVal;
    }

    @Override
    public void onApplied(LivingEntity pLivingEntity, int pAmplifier) {
        ticks = (long) ContagionConfig.INSTANCE.infectionDuration * 20;
        coolDown = defaultCooldown * 20;
    }


    @Override
    public boolean applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.getEntityWorld().isClient()) {
            --this.ticks;
            if (this.coolDown != 0) {
                --this.coolDown;
            }
            if (ContagionConfig.INSTANCE.enableRandomSymptoms) {
                if ((coolDown == 0) && (this.ticks > (ContagionConfig.INSTANCE.randomSymptomsDuration * 20L))) {
                    if ((this.ticks % 20) == 0) {
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
                            this.coolDown = (ContagionConfig.INSTANCE.randomSymptomsDuration + defaultCooldown) * 20L;
                        }
                    }
                } else if (this.ticks == (ContagionConfig.INSTANCE.randomSymptomsDuration * 20L / 2)) {
                    pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, (ContagionConfig.INSTANCE.randomSymptomsDuration * 20 / 2), 0));
                }
            }

            if (this.ticks <= 2) {
                if (ContagionConfig.INSTANCE.totemPreventsDyingFromInfection) {
                    pLivingEntity.damage(ContagionDamageTypes.of(pLivingEntity.getWorld(), ContagionDamageTypes.INFECTION), 1000.0f);
                } else {
                    pLivingEntity.kill();
                }
            }
        }
        return super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}