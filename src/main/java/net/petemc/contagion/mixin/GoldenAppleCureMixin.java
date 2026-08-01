package net.petemc.contagion.mixin;

import net.minecraft.component.type.FoodComponent;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class GoldenAppleCureMixin extends Entity {
    public GoldenAppleCureMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "eatFood", at = @At("HEAD"))
    private void contagion$eatFood(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {
        if (!world.isClient()) {
            if ((stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) || (stack.getItem() == Items.GOLDEN_APPLE)) {
                LivingEntity entity = (LivingEntity) (Object) this;
                GoldenAppleCureEvent.onFinishUsingItem(entity, stack);
            }
        }
    }
}
