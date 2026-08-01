package net.petemc.contagion.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.petemc.contagion.casts.InfectedEntity;
import net.petemc.contagion.config.MainConfig;
import org.jetbrains.annotations.NotNull;

public class ContagionResetInfectionEffect extends MobEffect {
    public ContagionResetInfectionEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity instanceof ServerPlayer pPlayerEntity) {
                if (pPlayerEntity.hasEffect(ContagionEffects.INFECTION)) {
                    pPlayerEntity.sendSystemMessage(Component.translatable("effect.contagion.reset_infection_msg"));

                    pLivingEntity.removeEffect(ContagionEffects.INFECTION);
                    pLivingEntity.removeEffect(ContagionEffects.INFECTIOUS);
                    ContagionInfectionEffect.resetValues(pLivingEntity);
                    if (pPlayerEntity instanceof InfectedEntity infectedEntity) {
                        pPlayerEntity.addEffect(new MobEffectInstance(ContagionEffects.INFECTION, ( int) infectedEntity.contagion_getInitialInfectionDuration() ,0));
                    }
                }
            }
        }
        return super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) { return true; }
}