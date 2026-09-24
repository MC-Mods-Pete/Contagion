package net.petemc.contagion.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
    public boolean applyEffectTick(@NotNull ServerLevel level, @NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity.hasEffect(ContagionEffects.INFECTION) || pLivingEntity.hasEffect(ContagionEffects.INFECTIOUS)) {
                    pLivingEntity.removeEffect(ContagionEffects.INFECTION);
                    ContagionInfectionEffect.resetValues(pLivingEntity);
                    pLivingEntity.removeEffect(ContagionEffects.INFECTIOUS);
                    pLivingEntity.removeEffect(MobEffects.WEAKNESS);
                    pLivingEntity.removeEffect(MobEffects.SLOWNESS);
                    if (pLivingEntity instanceof InfectedEntity infectedPlayer) {
                        infectedPlayer.contagion_setInfectious(false); // Revoke infectious flag on immunity cure.
                    }
                    if (pLivingEntity instanceof ServerPlayer serverPlayer) {
                        serverPlayer.sendSystemMessage(Component.translatable("effect.contagion.cured_msg"));
                    }
            }
        }
        return super.applyEffectTick(level, pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) { return true; }
}