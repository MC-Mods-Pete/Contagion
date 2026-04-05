package net.petemc.contagion.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.consume_effects.ClearAllStatusEffectsConsumeEffect;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.effect.ContagionInfectionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClearAllStatusEffectsConsumeEffect.class)
public class MilkBucketMixin {
    @Redirect(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;removeAllEffects()Z"))
    private boolean finishUsing(LivingEntity user) {
        if (MainConfig.isMilkCuresInfection()) {
            return user.removeAllEffects();
        }
        boolean retVal = false;
        MobEffectInstance effectInst = null;
        for (MobEffectInstance mobeffectinstance : user.getActiveEffects()) {
            if (mobeffectinstance.is(ContagionEffects.INFECTION)) {
                effectInst = mobeffectinstance;
            }
        }
        if (effectInst == null) {
            return user.removeAllEffects();
        }
        Holder<MobEffect> effectHolder = effectInst.getEffect();
        MobEffect effect = effectHolder.value();
        if (effect instanceof ContagionInfectionEffect infectEffect) {
            int localTicks = (int) infectEffect.getTicks(user);
            retVal = user.removeAllEffects();
            user.addEffect(effectInst);
            infectEffect.setTicks(user, localTicks);
        }
        return retVal;
    }
}
