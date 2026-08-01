package net.petemc.contagion.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.petemc.contagion.casts.InfectedEntity;

public class ContagionResetInfectionEffect extends StatusEffect {
    public ContagionResetInfectionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.getEntityWorld().isClient()) {
            if (pLivingEntity instanceof ServerPlayerEntity pPlayerEntity) {
                if (pPlayerEntity.hasStatusEffect(ContagionEffects.INFECTION)) {
                    pPlayerEntity.sendMessage(Text.translatable("effect.contagion.reset_infection_msg"));

                    pLivingEntity.removeStatusEffect(ContagionEffects.INFECTION);
                    pLivingEntity.removeStatusEffect(ContagionEffects.INFECTIOUS);
                    ContagionInfectionEffect.resetValues(pLivingEntity);
                    if (pPlayerEntity instanceof InfectedEntity infectedEntity) {
                        pPlayerEntity.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, ( int) infectedEntity.contagion_getInitialInfectionDuration() ,0));
                    }
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