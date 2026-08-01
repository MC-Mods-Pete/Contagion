package net.petemc.contagion.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionInfectionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class RemoveStatusEffectMixin {

    @Inject(method = "onEffectsRemoved", at = @At("HEAD"))
    private void onRemoveStatusEffect(Collection<MobEffectInstance> effects, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        for (MobEffectInstance effect : effects) {
            if (effect.getEffect().value() instanceof ContagionInfectionEffect) {
                ContagionInfectionEffect.resetValues(entity);
                if (MainConfig.isEnableDebugMessages()) {
                    Contagion.LOGGER.info("Removed infection effect from entity: " + entity.getName().getString() + " (UUID: " + entity.getStringUUID() + ")");
                }
                break;
            }
        }
    }
}
