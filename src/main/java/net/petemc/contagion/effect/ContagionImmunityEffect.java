package net.petemc.contagion.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class ContagionImmunityEffect extends StatusEffect {
    public ContagionImmunityEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity pLivingEntity, int pAmplifier) {
        if (!world.isClient()) {
            if (pLivingEntity instanceof PlayerEntity pPlayerEntity) {
                if (pPlayerEntity.hasStatusEffect(ContagionEffects.INFECTION)) {
                    pPlayerEntity.removeStatusEffect(ContagionEffects.INFECTION);
                    ContagionInfectionEffect.resetValues(pPlayerEntity);
                    pPlayerEntity.sendMessage(Text.translatable("effect.contagion.cured_msg"), false);
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