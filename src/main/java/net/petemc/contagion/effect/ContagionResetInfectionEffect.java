package net.petemc.contagion.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.petemc.contagion.casts.InfectedEntity;
import net.petemc.contagion.config.MainConfig;

public class ContagionResetInfectionEffect extends StatusEffect {
    public ContagionResetInfectionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.getEntityWorld().isClient()) {
            if (pLivingEntity instanceof ServerPlayerEntity pPlayerEntity) {
                if (pPlayerEntity.hasStatusEffect(ContagionEffects.INFECTION)) {
                    pPlayerEntity.sendMessage(Text.translatable("effect.contagion.reset_infection_msg"));

                    StatusEffectInstance existingEffect = pPlayerEntity.getStatusEffect(ContagionEffects.INFECTION);
                    long customDuration = (existingEffect != null && existingEffect.getDuration() > 0)
                            ? (int) existingEffect.getDuration()
                            : MainConfig.getInfectionDuration() * 20L;

                    if (pPlayerEntity instanceof InfectedEntity infectedPlayer) {
                        infectedPlayer.contagion_setInfection(false);
                        infectedPlayer.contagion_setInitialInfectionDuration(customDuration);
                        infectedPlayer.contagion_setInfectionTicks(customDuration);
                        infectedPlayer.contagion_setInfectionCooldown(60L * 20);
                    }

                    pPlayerEntity.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, (int) customDuration, 0));
                }
            }
        }
        super.applyUpdateEffect(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}