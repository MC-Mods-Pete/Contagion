package net.petemc.contagion.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.petemc.contagion.config.ContagionConfigs;
import net.petemc.contagion.damage_type.ContagionDamageTypes;

public class ContagionInfectionEffect extends StatusEffect {
    public ContagionInfectionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    private long ticks = (long) ContagionConfigs.DURATION_INFECTION_TOTAL * 20;
    private long coolDown = 60 * 20;

    @Override
    public void onApplied(LivingEntity pLivingEntity, int pAmplifier) {
        ticks = (long) ContagionConfigs.DURATION_INFECTION_TOTAL * 20;
        coolDown = 60 * 20;
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.getEntityWorld().isClient()) {
            --this.ticks;
            if (this.coolDown != 0) {
                --this.coolDown;
            }
            if (ContagionConfigs.RANDOM_SYMPTOMS) {
                if ((coolDown == 0) && (this.ticks > (ContagionConfigs.DURATION_INFECTION_SYMPTOMS * 20L))) {
                    if ((this.ticks % 20) == 0) {
                        int randomValue = MathHelper.nextInt(Random.create(), 1, 100);
                        if (randomValue > (100 - ContagionConfigs.RANDOM_SYMPTOMS_CHANCE)) {
                            randomValue = MathHelper.nextInt(Random.create(), 1, 4);
                            switch (randomValue) {
                                case 1:
                                    pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, (ContagionConfigs.DURATION_INFECTION_SYMPTOMS * 20), 0));
                                    break;
                                case 2:
                                    pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, (ContagionConfigs.DURATION_INFECTION_SYMPTOMS * 20), 0));
                                    break;
                                case 3:
                                    pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, (ContagionConfigs.DURATION_INFECTION_SYMPTOMS * 20), 0));
                                    break;
                                case 4:
                                    pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, (ContagionConfigs.DURATION_INFECTION_SYMPTOMS * 20), 0));
                                    break;
                            }
                            this.coolDown = (ContagionConfigs.DURATION_INFECTION_SYMPTOMS + 60) * 20L;
                        }
                    }
                } else if (this.ticks == (ContagionConfigs.DURATION_INFECTION_SYMPTOMS * 20L / 2)) {
                    pLivingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, (ContagionConfigs.DURATION_INFECTION_SYMPTOMS * 20 / 2), 0));
                }
            }

            if (this.ticks == 1) {
                pLivingEntity.damage(ContagionDamageTypes.of(pLivingEntity.getWorld(), ContagionDamageTypes.INFECTION), 500.0f);
            }
        }

        super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}