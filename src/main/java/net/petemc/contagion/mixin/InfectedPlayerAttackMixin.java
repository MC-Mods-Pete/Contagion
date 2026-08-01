package net.petemc.contagion.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.effect.ContagionInfectionEffect;
import net.petemc.contagion.sound.ContagionSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * When an infected player attacks another player or Villager, there is a chance to spread
 * the infection. Uses the same chance mechanics as ZombieEntityMixin.
 * Can be disabled via the config options "infectedPlayerInfectsPlayers" and "infectedPlayerInfectsVillagers".
 */
@Mixin(Player.class)
public class InfectedPlayerAttackMixin {

    @Inject(method = "attack", at = @At("RETURN"))
    public void attack(Entity target, CallbackInfo ci) {
        Player attacker = (Player) (Object) this;

        // Only spread infection if the attacking player is currently infected
        if (!attacker.hasEffect(ContagionEffects.INFECTION)) return;

        if (target instanceof Player targetPlayer) {
            if (!MainConfig.isAllowPlayersToInfectPlayers()) return;
            int randomValue = Mth.nextInt(RandomSource.create(), 1, 100);
            int effectiveInfectChance = getEffectiveInfectChance(targetPlayer);
            if (randomValue > effectiveInfectChance) {
                if (!targetPlayer.hasEffect(ContagionEffects.INFECTION)) {
                    if (targetPlayer.hasEffect(ContagionEffects.IMMUNITY)) {
                        targetPlayer.level().playSound(null, targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(),
                                ContagionSounds.INFECTION_PREVENTED.get(), SoundSource.BLOCKS, 1.0F, 3);
                    } else {
                        if (!targetPlayer.level().isClientSide()) {
                            targetPlayer.addEffect(new MobEffectInstance(ContagionEffects.INFECTION, MainConfig.getInfectionDuration() * 20, 0));
                            ContagionInfectionEffect.resetValues(targetPlayer);
                            targetPlayer.sendSystemMessage(Component.translatable("effect.contagion.infected_msg").withStyle(ChatFormatting.RED));
                        }
                    }
                }
            }
        } else if (target instanceof Villager villagerTarget) {
            if (!MainConfig.isAllowPlayersToInfectVillagers()) return;
            int randomValue = Mth.nextInt(RandomSource.create(), 1, 100);
            int effectiveInfectChance = getEffectiveInfectChance(villagerTarget);
            if (randomValue > effectiveInfectChance) {
                if (!villagerTarget.hasEffect(ContagionEffects.INFECTION)) {
                    if (!villagerTarget.level().isClientSide()) {
                        villagerTarget.addEffect(new MobEffectInstance(ContagionEffects.INFECTION, MainConfig.getInfectionDuration() * 20, 0));
                        ContagionInfectionEffect.resetValues(villagerTarget);
                    }
                    villagerTarget.level().playSound(null, villagerTarget.getX(), villagerTarget.getY(), villagerTarget.getZ(),
                            SoundEvents.VILLAGER_HURT, SoundSource.BLOCKS, 1.0F, 3f);
                }
            }
        }
    }

    @Unique
    private static int getEffectiveInfectChance(LivingEntity entity) {
        int effectInfect;
        if (MainConfig.getBaseInfectionChance() > 100) {
            effectInfect = 0;
        } else {
            effectInfect = 100 - MainConfig.getBaseInfectionChance();
        }
        if (MainConfig.isArmorLowersInfectionChance()) {
            effectInfect = effectInfect + (entity.getArmorValue() * 3);
        }
        if (effectInfect > (100 - MainConfig.getMinimumInfectionChance())) {
            effectInfect = 100 - MainConfig.getMinimumInfectionChance();
        }
        return effectInfect;
    }
}
