package net.petemc.contagion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
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
@Mixin(PlayerEntity.class)
public class InfectedPlayerAttackMixin {

    @Inject(method = "attack", at = @At("RETURN"))
    public void attack(Entity target, CallbackInfo ci) {
        PlayerEntity attacker = (PlayerEntity) (Object) this;

        // Only spread infection if the attacking player is currently infected
        if (!attacker.hasStatusEffect(ContagionEffects.INFECTION)) return;

        if (target instanceof PlayerEntity targetPlayer) {
            if (!MainConfig.isAllowPlayersToInfectPlayers()) return;
            int randomValue = targetPlayer.getRandom().nextBetween(1, 100);
            int effectiveInfectChance = getEffectiveInfectChance(targetPlayer);
            if (randomValue > effectiveInfectChance) {
                if (!targetPlayer.hasStatusEffect(ContagionEffects.INFECTION)) {
                    if (targetPlayer.hasStatusEffect(ContagionEffects.IMMUNITY)) {
                        targetPlayer.getWorld().playSound(null, targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(),
                                ContagionSounds.INFECTION_PREVENTED, SoundCategory.BLOCKS, 1.0F, 3);
                    } else {
                        if (!targetPlayer.getWorld().isClient()) {
                            targetPlayer.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, MainConfig.getInfectionDuration() * 20, 0));
                            ContagionInfectionEffect.resetValues(targetPlayer);
                            targetPlayer.sendMessage(Text.translatable("effect.contagion.infected_msg").formatted(Formatting.RED));
                        }
                    }
                }
            }
        } else if (target instanceof VillagerEntity villagerTarget) {
            if (!MainConfig.isAllowPlayersToInfectVillagers()) return;
            int randomValue = MathHelper.nextInt(Random.create(), 1, 100);
            int effectiveInfectChance = getEffectiveInfectChance(villagerTarget);
            if (randomValue > effectiveInfectChance) {
                if (!villagerTarget.hasStatusEffect(ContagionEffects.INFECTION)) {
                    if (!villagerTarget.getWorld().isClient()) {
                        villagerTarget.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, MainConfig.getInfectionDuration() * 20, 0));
                        ContagionInfectionEffect.resetValues(villagerTarget);
                    }
                    villagerTarget.getWorld().playSound(null, villagerTarget.getX(), villagerTarget.getY(), villagerTarget.getZ(),
                            SoundEvents.ENTITY_VILLAGER_HURT, SoundCategory.BLOCKS, 1.0F, 3f);
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
            effectInfect = effectInfect + (entity.getArmor() * 3);
        }
        if (effectInfect > (100 - MainConfig.getMinimumInfectionChance())) {
            effectInfect = 100 - MainConfig.getMinimumInfectionChance();
        }
        return effectInfect;
    }
}
