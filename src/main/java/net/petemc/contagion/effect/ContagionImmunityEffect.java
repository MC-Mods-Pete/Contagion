package net.petemc.contagion.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class ContagionImmunityEffect extends MobEffect {
    public ContagionImmunityEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull ServerLevel level, @NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!level.isClientSide()) {
            if (pLivingEntity instanceof ServerPlayer pPlayerEntity) {
                if (pPlayerEntity.hasEffect(ContagionEffects.INFECTION)) {
                    pPlayerEntity.removeEffect(ContagionEffects.INFECTION);
                    ContagionInfectionEffect.resetValues(pPlayerEntity);
                    pPlayerEntity.sendSystemMessage(Component.translatable("effect.contagion.cured_msg"));
                }
            }
        }
        return super.applyEffectTick(level, pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) { return true; }
}