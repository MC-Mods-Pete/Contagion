package net.petemc.contagion.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import net.petemc.contagion.config.ContagionConfig;

public class ContagionResetInfectionEffect extends StatusEffect {
    public ContagionResetInfectionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.getEntityWorld().isClient()) {
            if (pLivingEntity.hasStatusEffect(ContagionEffects.INFECTION)) {
                pLivingEntity.sendMessage(Text.translatable("effect.contagion.reset_infection_msg"));
                pLivingEntity.removeStatusEffect(ContagionEffects.INFECTION);
                ContagionInfectionEffect.resetValues();
                pLivingEntity.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, ContagionConfig.INSTANCE.infectionDuration * 20, 0));
            }
        }
        super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}