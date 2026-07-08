package net.petemc.contagion.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Zombie.class)
public class ZombieEntityMixin {

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    public void finalizeSpawn(CallbackInfoReturnable<LivingEntity> cir) {
        if (MainConfig.isAllowZombiesToSpawnInfectious()) {
            Zombie zombie = (Zombie) (Object) this;
            if (zombie.getRandom().nextIntBetweenInclusive(1, 1000) <= MainConfig.getChanceForZombieToSpawnInfectious()) {
                zombie.addEffect(new MobEffectInstance(ContagionEffects.INFECTIOUS.get(), 600 * 20 ,0));
            }
        }
    }

    @Inject(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getCurrentDifficultyAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/DifficultyInstance;", shift = At.Shift.BEFORE))
    public void doHurtTarget(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof Player pPlayer) {
            int randomValue = RandomSource.create().nextIntBetweenInclusive(1, 100);
            int effectiveInfectChance = getEffectiveInfectChance(pPlayer);
            if (randomValue > effectiveInfectChance) {
                if (!pPlayer.hasEffect(ContagionEffects.INFECTION.get())) {
                    if (pPlayer.hasEffect(ContagionEffects.IMMUNITY.get())) {
                        pPlayer.level().playSound((Player) null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ContagionSounds.INFECTION_PREVENTED.get(), SoundSource.BLOCKS, 1.0F, 3);
                    } else {
                        if (!pPlayer.level().isClientSide()) {
                            pPlayer.addEffect(new MobEffectInstance(ContagionEffects.INFECTION.get(), MainConfig.getInfectionDuration() * 20, 0));
                            ContagionInfectionEffect.resetValues(pPlayer);
                            pPlayer.sendSystemMessage(Component.translatable("effect.contagion.infected_msg").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                        }
                    }
                }
            }
        } else if (target instanceof Villager villagerTarget) {
            if (!MainConfig.isAllowZombiesToInfectVillagers()) return;
            int randomValue = RandomSource.create().nextIntBetweenInclusive(1, 100);
            int effectiveInfectChance = getEffectiveInfectChance(villagerTarget);
            if (randomValue > effectiveInfectChance) {
                if (!villagerTarget.hasEffect(ContagionEffects.INFECTION.get())) {
                    if (!villagerTarget.level().isClientSide()) {
                        villagerTarget.addEffect(new MobEffectInstance(ContagionEffects.INFECTION.get(), MainConfig.getInfectionDuration() * 20, 0));
                        ContagionInfectionEffect.resetValues(villagerTarget);
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
