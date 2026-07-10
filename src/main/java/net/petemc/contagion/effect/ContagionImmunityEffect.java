package net.petemc.contagion.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.petemc.contagion.casts.InfectedEntity;
import org.jetbrains.annotations.NotNull;

public class ContagionImmunityEffect extends MobEffect {
    public ContagionImmunityEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity.hasEffect(ContagionEffects.INFECTION.get()) || pLivingEntity.hasEffect(ContagionEffects.INFECTIOUS.get())) {
                    pLivingEntity.removeEffect(ContagionEffects.INFECTION.get());
                    ContagionInfectionEffect.resetValues(pLivingEntity);
                    pLivingEntity.removeEffect(ContagionEffects.INFECTIOUS.get());
                    pLivingEntity.removeEffect(MobEffects.WEAKNESS);
                    pLivingEntity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                    if (pLivingEntity instanceof InfectedEntity infectedEntity) {
                        infectedEntity.contagion_setInfectious(false); // Revoke infectious flag on immunity cure.
                    }
                pLivingEntity.sendSystemMessage(Component.translatable("effect.contagion.cured_msg"));
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) { return true; }
}