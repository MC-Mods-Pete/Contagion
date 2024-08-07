package net.petemc.contagion.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.MilkBucketItem;
import net.petemc.contagion.config.ContagionConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.effect.ContagionInfectionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(MilkBucketItem.class)
public class MilkBucketMixin {
    @Redirect(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"))
    private boolean finishUsing(LivingEntity user) {
        if (ContagionConfig.INSTANCE.milkCuresInfection) {
            return user.clearStatusEffects();
        }
        boolean retVal = false;
        StatusEffectInstance effectInst = user.getActiveStatusEffects().get(ContagionEffects.INFECTION);
        if (effectInst == null) {
            return user.clearStatusEffects();
        }
        StatusEffect effect = effectInst.getEffectType();
        if (effect instanceof ContagionInfectionEffect infectEffect) {
            int localTicks = (int) infectEffect.getTicks(user);
            retVal = user.clearStatusEffects();
            user.addStatusEffect(effectInst);
            infectEffect.setTicks(user, localTicks);
        }
        return retVal;
    }
}
