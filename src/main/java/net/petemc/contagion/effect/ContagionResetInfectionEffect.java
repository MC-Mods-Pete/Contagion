package net.petemc.contagion.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.petemc.contagion.casts.InfectedEntity;
import org.jetbrains.annotations.NotNull;

public class ContagionResetInfectionEffect extends MobEffect {
    public ContagionResetInfectionEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity instanceof ServerPlayer pPlayerEntity) {
                if (pPlayerEntity.hasEffect(ContagionEffects.INFECTION.get())) {
                    pPlayerEntity.sendSystemMessage(Component.translatable("effect.contagion.reset_infection_msg"));

                    pLivingEntity.removeEffect(ContagionEffects.INFECTION.get());
                    pLivingEntity.removeEffect(ContagionEffects.INFECTIOUS.get());
                    ContagionInfectionEffect.resetValues(pLivingEntity);
                    if (pPlayerEntity instanceof InfectedEntity infectedEntity) {
                        pPlayerEntity.addEffect(new MobEffectInstance(ContagionEffects.INFECTION.get(), ( int) infectedEntity.contagion_getInitialInfectionDuration() ,0));
                    }
                }
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) { return true; }
}