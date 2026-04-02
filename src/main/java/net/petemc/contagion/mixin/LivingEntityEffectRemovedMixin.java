package net.petemc.contagion.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.petemc.contagion.effect.ContagionInfectionEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

/**
 * When the INFECTION effect is removed (e.g. via /effect clear, cure, immunity),
 * reset the internal infection state. This ensures that the init block in
 * ContagionInfectionEffect.applyEffectTick runs correctly on the next /effect give
 * and picks up the new duration instead of continuing with the old MainConfig value.
 */
@Mixin(LivingEntity.class)
public class LivingEntityEffectRemovedMixin {

    @Inject(method = "onEffectsRemoved", at = @At("HEAD"))
    private void onEffectsRemoved(Collection<MobEffectInstance> effects, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        for (MobEffectInstance effectInstance : effects) {
            if (effectInstance.getEffect().value() instanceof ContagionInfectionEffect) {
                ContagionInfectionEffect.resetValues(entity);
                break;
            }
        }
    }
}

