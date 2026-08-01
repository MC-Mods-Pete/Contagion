package net.petemc.contagion.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolem.class)
public abstract class IronGolemEntityMixin extends Mob {

    // Dummy constructor for the compiler - never actually called by the Mixin framework
    protected IronGolemEntityMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void addInfectedPlayerTarget(CallbackInfo ci) {
        if (!MainConfig.isIronGolemAttacksInfected()) return;
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<Player>(
                (IronGolem) (Object) this,
                Player.class,
                true,
                (entity, serverLevel) -> entity instanceof Player player && player.hasEffect(ContagionEffects.INFECTION)
        ));
    }
}
