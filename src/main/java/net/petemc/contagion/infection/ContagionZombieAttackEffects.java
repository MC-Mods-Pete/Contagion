package net.petemc.contagion.infection;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.ContagionConfig;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.effect.ContagionInfectionEffect;

public class ContagionZombieAttackEffects {

    private static LivingEntity pPlayer = null;
    private static LivingEntity pAttacker = null;
    private static DamageSource pSource = null;
    private static float pAmount = 0.0f;

    public ContagionZombieAttackEffects() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, damageSource, amount) -> {
            pPlayer = entity;
            pAttacker = damageSource.getAttacker() instanceof LivingEntity ? ((LivingEntity) damageSource.getAttacker()) : null;
            pSource = damageSource;
            pAmount = amount;
            execute();
            return true;
        });
    }


    public static void execute() {
        // currently not used, because getting infected is handled via injecting code into the Entity tryAttack methods (mixin)
        // TODO: remove if no issues arise

        if (pPlayer == null) {
            Contagion.LOGGER.warn("Failed to load Player entity!");
        } else if (pPlayer.getWorld() == null) {
            Contagion.LOGGER.warn("Failed to load World!");
        }  else if (pAttacker != null) {
            int randomValue = MathHelper.nextInt(Random.create(), 1, 100);
            int effectiveInfectChance = getEffectiveInfectChance(pPlayer);
            if (pAttacker.getGroup() == EntityGroup.UNDEAD && (randomValue > effectiveInfectChance)) {

                // Attack was blocked and infection prevented
                if (pAmount > 0.0f && pPlayer.blockedByShield(pSource)) {
                    return;
                }

                // if attack was caused by trident, check if infecting by trident is enabled
                if (pSource.isOf(DamageTypes.TRIDENT)) {
                    return;
                }

                // Do nothing if Player is already infected
                if (pPlayer.hasStatusEffect(ContagionEffects.INFECTION)) {
                    return;
                }

                // Do nothing if the Player has the Resistance effect
                if (pPlayer.hasStatusEffect(ContagionEffects.IMMUNITY)) {
                    pPlayer.sendMessage(Text.translatable("effect.contagion.infection_immunity_msg"));
                    return;
                }

                // Give the Player the Infection effect
                if (!pPlayer.getWorld().isClient()) {
                    ContagionInfectionEffect.resetValues();
                    pPlayer.addStatusEffect(new StatusEffectInstance(ContagionEffects.INFECTION, ContagionConfig.INSTANCE.infectionDuration * 20, 0));
                    pPlayer.sendMessage(Text.translatable("effect.contagion.infected_msg"));
                }
            }
        }
    }

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

    public static void registerAttackEffects() {
        new ContagionZombieAttackEffects();
    }
}
