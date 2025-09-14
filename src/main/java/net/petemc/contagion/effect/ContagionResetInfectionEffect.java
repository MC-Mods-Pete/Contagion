package net.petemc.contagion.effect;

import net.minecraft.network.chat.Component;
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
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level.isClientSide()) {
            if (pLivingEntity.hasEffect(ContagionEffects.INFECTION.get())) {
                pLivingEntity.sendSystemMessage(Component.translatable("effect.contagion.reset_infection_msg"));
                pLivingEntity.removeEffect(ContagionEffects.INFECTION.get());
                ContagionInfectionEffect.resetValues(pLivingEntity);
                pLivingEntity.addEffect(new MobEffectInstance(ContagionEffects.INFECTION.get(), Config.infectionDuration * 20, 0));
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) { return true; }
}