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
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity instanceof ServerPlayer pPlayerEntity) {
                if (pPlayerEntity.hasEffect(ContagionEffects.INFECTION.get())) {
                    pPlayerEntity.sendSystemMessage(Component.translatable("effect.contagion.reset_infection_msg"));

                    MobEffectInstance existingEffect = pPlayerEntity.getEffect(ContagionEffects.INFECTION.get());
                    long customDuration = (existingEffect != null && existingEffect.getDuration() > 0)
                            ? (int) existingEffect.getDuration()
                            : MainConfig.getInfectionDuration() * 20;

                    if (pPlayerEntity instanceof InfectedEntity infectedPlayer) {
                        infectedPlayer.contagion_setInfection(false);
                        infectedPlayer.contagion_setInitialInfectionDuration(customDuration);
                        infectedPlayer.contagion_setInfectionTicks(customDuration);
                        infectedPlayer.contagion_setInfectionCooldown(60L * 20);
                    }

                    pPlayerEntity.addEffect(new MobEffectInstance(ContagionEffects.INFECTION.get(), (int) customDuration, 0));
                }
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) { return true; }
}