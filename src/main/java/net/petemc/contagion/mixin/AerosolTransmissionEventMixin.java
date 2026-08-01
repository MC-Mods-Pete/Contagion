package net.petemc.contagion.mixin;

import net.minecraft.entity.LivingEntity;
import net.petemc.contagion.event.AerosolTransmissionEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class AerosolTransmissionEventMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void onLivingTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        AerosolTransmissionEvent.onLivingTick(entity);
    }
}
