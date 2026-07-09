package net.petemc.contagion.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.effect.ContagionInfectionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class RemoveStatusEffectMixin {

    @Inject(method = "onStatusEffectRemoved", at = @At("HEAD"))
    private void onRemoveStatusEffect(StatusEffectInstance effect, CallbackInfo ci) {
        if (effect.getEffectType() == ContagionEffects.INFECTION) {
            LivingEntity entity = (LivingEntity) (Object) this;
            ContagionInfectionEffect.resetValues(entity);
            if (MainConfig.isEnableDebugMessages()) {
                Contagion.LOGGER.info("Removed infection effect from entity: " + entity.getName().getString() + " (UUID: " + entity.getUuidAsString() + ")");
            }
        }
    }
}
