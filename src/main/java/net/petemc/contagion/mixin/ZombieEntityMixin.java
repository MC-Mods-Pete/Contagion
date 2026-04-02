package net.petemc.contagion.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.effect.ContagionInfectionEffect;
import net.petemc.contagion.sound.ContagionSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zombie.class)
public class ZombieEntityMixin {
    @Inject(method = "doHurtTarget", at = @At("RETURN"))
    public void doHurtTarget(ServerLevel world, Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        if (target instanceof Player pPlayer) {
            int randomValue = Mth.nextInt(RandomSource.create(), 1, 100);
            int effectiveInfectChance = getEffectiveInfectChance(pPlayer);
            if (randomValue > effectiveInfectChance) {
                if (!pPlayer.hasEffect(ContagionEffects.INFECTION)) {
                    if (pPlayer.hasEffect(ContagionEffects.IMMUNITY)) {
                        pPlayer.level().playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ContagionSounds.INFECTION_PREVENTED, SoundSource.BLOCKS, 1.0F, 3);
                    } else {
                        if (!pPlayer.level().isClientSide()) {
                            pPlayer.addEffect(new MobEffectInstance(ContagionEffects.INFECTION, MainConfig.getInfectionDuration() * 20, 0));
                            ContagionInfectionEffect.resetValues(pPlayer);
                            pPlayer.sendSystemMessage(Component.translatable("effect.contagion.infected_msg").withStyle(ChatFormatting.RED));
                        }
                    }
                }
            }
        }
    }

    @Unique
    private static int getEffectiveInfectChance(LivingEntity _entity) {
        int effectInfect;
        if (MainConfig.getBaseInfectionChance() > 100) {
            effectInfect = 0;
        } else {
            effectInfect = 100 - MainConfig.getBaseInfectionChance();
        }
        if (MainConfig.isArmorLowersInfectionChance()) {
            effectInfect = effectInfect + (_entity.getArmorValue() * 3);
        }
        if (effectInfect > (100 - MainConfig.getMinimumInfectionChance())) {
            effectInfect = 100 - MainConfig.getMinimumInfectionChance();
        }
        return effectInfect;
    }
}
