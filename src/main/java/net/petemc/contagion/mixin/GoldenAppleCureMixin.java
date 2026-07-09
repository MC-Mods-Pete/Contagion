package net.petemc.contagion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.petemc.contagion.event.GoldenAppleCureEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class GoldenAppleCureMixin extends Entity {
    public GoldenAppleCureMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "applyFoodEffects", at = @At("HEAD"))
    private void contagion$onFinishUsingItem(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
        if (!world.isClient()) {
            if ((stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) || (stack.getItem() == Items.GOLDEN_APPLE)) {
                LivingEntity entity = (LivingEntity) (Object) this;
                GoldenAppleCureEvent.onFinishUsingItem(entity, stack);
            }
        }
    }
}
