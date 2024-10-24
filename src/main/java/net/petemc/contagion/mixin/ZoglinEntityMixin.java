package net.petemc.contagion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.Hoglin;
import net.minecraft.entity.mob.ZoglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.petemc.contagion.config.ContagionConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.effect.ContagionInfectionEffect;
import net.petemc.contagion.sound.ContagionSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ZoglinEntity.class)
public abstract class ZoglinEntityMixin {
    @Inject(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/ZoglinEntity;playSound(Lnet/minecraft/sound/SoundEvent;)V", shift = At.Shift.AFTER), cancellable = true)
    public void tryAttack(Entity target, CallbackInfoReturnable<Boolean> cir) {
        boolean returnValue = Hoglin.tryAttack((LivingEntity) (Object) this, (LivingEntity) target);
        cir.setReturnValue(returnValue);

        if (target instanceof PlayerEntity pPlayer) {
            if (returnValue) {
                int randomValue = MathHelper.nextInt(Random.create(), 1, 100);
                int effectiveInfectChance = getEffectiveInfectChance(pPlayer);
                if (randomValue > effectiveInfectChance) {
                    if (!pPlayer.hasStatusEffect(ContagionEffects.INFECTION)) {
                        if (pPlayer.hasStatusEffect(ContagionEffects.IMMUNITY)) {
                            pPlayer.getWorld().playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ContagionSounds.INFECTION_PREVENTED, SoundCategory.BLOCKS, 1.0F, 3);
                        } else {
                            if (!pPlayer.getWorld().isClient()) {
                                pPlayer.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, ContagionConfig.INSTANCE.infectionDuration * 20, 0));
                                ContagionInfectionEffect.resetValues(pPlayer);
                                pPlayer.sendMessage(Text.translatable("effect.contagion.infected_msg"));
                            }
                        }
                    }
                }
            }
        }
        cir.cancel();
    }

    @Unique
    private static int getEffectiveInfectChance(LivingEntity _entity) {
        int effectInfect;
        if (ContagionConfig.INSTANCE.baseInfectionChance > 100) {
            effectInfect = 0;
        } else {
            effectInfect = 100 - ContagionConfig.INSTANCE.baseInfectionChance;
        }
        if (ContagionConfig.INSTANCE.armorLowersInfectionChance) {
            effectInfect = effectInfect + (_entity.getArmor() * 3);
        }
        if (effectInfect > (100 - ContagionConfig.INSTANCE.minimumInfectionChance)) {
            effectInfect = 100 - ContagionConfig.INSTANCE.minimumInfectionChance;
        }
        return effectInfect;
    }
}
