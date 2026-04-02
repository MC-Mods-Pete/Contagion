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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;

@Mixin(ClearAllStatusEffectsConsumeEffect.class)
public class MilkBucketMixin {
    @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
    private void apply(Level level, ItemStack itemStack, LivingEntity user, CallbackInfoReturnable<Boolean> cir) {
        if (MainConfig.isMilkCuresInfection()) {
            return; // default: cure everything including infection
        }
        MobEffectInstance effectInst = user.getActiveEffectsMap().get(ContagionEffects.INFECTION);
        if (effectInst == null) {
            return; // no infection present, default behavior
        }
        Holder<MobEffect> effectReg = effectInst.getEffect();
        MobEffect effect = effectReg.value();
        if (effect instanceof ContagionInfectionEffect infectEffect) {
            int localTicks = (int) infectEffect.getTicks(user);
            boolean retVal = user.removeAllEffects();
            user.addEffect(effectInst);
            infectEffect.setTicks(user, localTicks);
            cir.setReturnValue(retVal);
            cir.cancel();
        }
    }
}
