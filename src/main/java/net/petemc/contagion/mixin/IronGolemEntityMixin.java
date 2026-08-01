package net.petemc.contagion.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin extends MobEntity {
    protected IronGolemEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void addInfectedPlayerTarget(CallbackInfo ci) {
        if (MainConfig.isIronGolemAttacksInfected()) {
            this.targetSelector.add(3, new ActiveTargetGoal<>(
                    (IronGolemEntity) (Object) this,
                    PlayerEntity.class,
                    10,
                    true,
                    false,
                    entity -> entity instanceof PlayerEntity player && player.hasStatusEffect(ContagionEffects.INFECTION)
            ));
        }
    }
}
