package net.petemc.contagion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.petemc.contagion.config.ContagionConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.sound.ContagionSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ZombieEntity.class)
public class ZombieEntityMixin {
    @Inject(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getLocalDifficulty(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/LocalDifficulty;", shift = At.Shift.BEFORE))
    public void tryAttack(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof PlayerEntity pPlayer) {
            int randomValue = MathHelper.nextInt(Random.create(), 1, 100);
            int effectiveInfectChance = getEffectiveInfectChance(pPlayer);
            if (randomValue > effectiveInfectChance) {
                if (!pPlayer.hasStatusEffect(ContagionEffects.INFECTION)) {
                    if (pPlayer.hasStatusEffect(ContagionEffects.IMMUNITY)) {
                        pPlayer.getWorld().playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ContagionSounds.INFECTION_PREVENTED, SoundCategory.BLOCKS, 1.0F, 3);
                    } else {
                        if (!pPlayer.getWorld().isClient()) {
                            pPlayer.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, ContagionConfig.INSTANCE.infectionDuration * 20, 0));
                            pPlayer.sendMessage(Text.translatable("effect.contagion.infected_msg"));
                        }
                    }
                }
            }
        }
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
