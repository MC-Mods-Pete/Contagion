package net.petemc.contagion.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.petemc.contagion.Config;
import org.jetbrains.annotations.NotNull;

public class ContagionResetInfectionEffect extends MobEffect {
    public ContagionResetInfectionEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, @NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!level.isClientSide()) {
            if (pLivingEntity instanceof ServerPlayer pPlayerEntity) {
                if (pPlayerEntity.hasEffect(ContagionEffects.INFECTION)) {
                    pPlayerEntity.sendSystemMessage(Component.translatable("effect.contagion.reset_infection_msg"));
                    pPlayerEntity.removeEffect(ContagionEffects.INFECTION);
                    ContagionInfectionEffect.resetValues(pPlayerEntity);
                    pPlayerEntity.addEffect(new MobEffectInstance(ContagionEffects.INFECTION, Config.infectionDuration * 20, 0));
                }
            }
        }
        return super.applyEffectTick(level, pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) { return true; }
}