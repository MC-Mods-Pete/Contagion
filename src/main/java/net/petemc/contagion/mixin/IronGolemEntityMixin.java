package net.petemc.contagion.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolem.class)
public abstract class IronGolemEntityMixin {

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void addInfectedPlayerTarget(CallbackInfo ci) {
        if (MainConfig.isIronGolemAttacksInfected()) {
            ((Mob) (Object) this).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(
                    (IronGolem) (Object) this,
                    Player.class,
                    10,
                    true,
                    false,
                    entity -> entity instanceof Player player && player.hasEffect(ContagionEffects.INFECTION.get())
            ));
        }
    }
}
