package net.petemc.contagion.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ContagionImmunityEffect extends MobEffect {
    public ContagionImmunityEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel world, LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Player pPlayerEntity) {
            if (pPlayerEntity.hasEffect(ContagionEffects.INFECTION)) {
                pPlayerEntity.removeEffect(ContagionEffects.INFECTION);
                ContagionInfectionEffect.resetValues(pPlayerEntity);
                pPlayerEntity.sendSystemMessage(Component.translatable("effect.contagion.cured_msg"));
            }
        }
        return super.applyEffectTick(world, pLivingEntity, pAmplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return true;
    }
}