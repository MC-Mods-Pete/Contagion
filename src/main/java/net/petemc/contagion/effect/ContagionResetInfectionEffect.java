package net.petemc.contagion.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.petemc.contagion.config.ContagionConfig;

public class ContagionResetInfectionEffect extends StatusEffect {
    public ContagionResetInfectionEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity pLivingEntity, int pAmplifier) {
        if (!world.isClient()) {
            if (pLivingEntity instanceof PlayerEntity pPlayerEntity) {
                if (pPlayerEntity.hasStatusEffect(ContagionEffects.INFECTION)) {
                    pPlayerEntity.sendMessage(Text.translatable("effect.contagion.reset_infection_msg"), false);
                    pPlayerEntity.removeStatusEffect(ContagionEffects.INFECTION);
                    ContagionInfectionEffect.resetValues(pPlayerEntity);
                    pPlayerEntity.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, ContagionConfig.INSTANCE.infectionDuration * 20, 0));
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