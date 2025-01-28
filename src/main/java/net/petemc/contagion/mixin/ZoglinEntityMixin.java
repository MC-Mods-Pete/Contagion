package net.petemc.contagion.mixin;


import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.entity.player.Player;
import net.petemc.contagion.Config;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.effect.ContagionInfectionEffect;
import net.petemc.contagion.sound.ContagionSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Zoglin.class)
public abstract class ZoglinEntityMixin {
    @Inject(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Zoglin;makeSound(Lnet/minecraft/sounds/SoundEvent;)V", shift = At.Shift.AFTER), cancellable = true)
    public void doHurtTarget(Entity target, CallbackInfoReturnable<Boolean> cir) {
        boolean returnValue = HoglinBase.hurtAndThrowTarget((LivingEntity) (Object) this, (LivingEntity) target);
        cir.setReturnValue(returnValue);

        if (target instanceof Player pPlayer) {
            if (returnValue) {
                int randomValue = RandomSource.create().nextIntBetweenInclusive(1, 100);
                int effectiveInfectChance = getEffectiveInfectChance(pPlayer);
                if (randomValue > effectiveInfectChance) {
                    if (!pPlayer.hasEffect(ContagionEffects.INFECTION)) {
                        if (pPlayer.hasEffect(ContagionEffects.IMMUNITY)) {
                            pPlayer.level().playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ContagionSounds.INFECTION_PREVENTED.get(), SoundSource.BLOCKS, 1.0F, 3);
                        } else {
                            if (!pPlayer.level().isClientSide()) {
                                pPlayer.addEffect(new MobEffectInstance(ContagionEffects.INFECTION, Config.infectionDuration * 20, 0));
                                ContagionInfectionEffect.resetValues(pPlayer);
                                pPlayer.sendSystemMessage(Component.translatable("effect.contagion.infected_msg").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
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
        if (Config.baseInfectionChance > 100) {
            effectInfect = 0;
        } else {
            effectInfect = 100 - Config.baseInfectionChance;
        }
        if (Config.armorLowersInfectionChance) {
            effectInfect = effectInfect + (_entity.getArmorValue() * 3);
        }
        if (effectInfect > (100 - Config.minimumInfectionChance)) {
            effectInfect = 100 - Config.minimumInfectionChance;
        }
        return effectInfect;
    }
}
