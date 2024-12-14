package net.petemc.contagion.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.petemc.contagion.Config;
import org.jetbrains.annotations.NotNull;

public class ContagionImmunityEffect extends MobEffect {
    public ContagionImmunityEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity.hasEffect(ContagionEffects.INFECTION)) {
                pLivingEntity.removeEffect(ContagionEffects.INFECTION);
                ContagionInfectionEffect.resetValues(pLivingEntity);
                pLivingEntity.sendSystemMessage(Component.translatable("effect.contagion.cured_msg"));
            }
        }
        return super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) { return true; }
}