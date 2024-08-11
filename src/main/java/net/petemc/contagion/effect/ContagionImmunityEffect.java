package net.petemc.contagion.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.text.Text;

public class ContagionImmunityEffect extends StatusEffect {
    public ContagionImmunityEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.getEntityWorld().isClient()) {
            if (pLivingEntity.hasStatusEffect(ContagionEffects.INFECTION)) {
                pLivingEntity.removeStatusEffect(ContagionEffects.INFECTION);
                ContagionInfectionEffect.resetValues(pLivingEntity);
                pLivingEntity.sendMessage(Text.translatable("effect.contagion.cured_msg"));
            }
        }
        return super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}