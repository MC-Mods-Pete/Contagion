package net.petemc.contagion.effect;

import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.petemc.contagion.casts.InfectedEntity;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.damage_type.ContagionDamageTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContagionInfectionEffect extends MobEffect {
    public ContagionInfectionEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    private static final long defaultCooldown = 60;

    @Override
    public List<ItemStack> getCurativeItems() {
        if (MainConfig.isMilkCuresInfection()) {
            return super.getCurativeItems();
        }
        return List.of(ItemStack.EMPTY);
    }

    public long getTicks(LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof InfectedEntity infectedPlayer) {
            return infectedPlayer.contagion_getInfectionTicks();
        }
        return 0;
    }

    public void setTicks(LivingEntity pLivingEntity, long ticksValue) {
        if (pLivingEntity instanceof InfectedEntity infectedPlayer) {
            infectedPlayer.contagion_setInfectionTicks(ticksValue);
        }
    }

    public static void resetValues(LivingEntity pLivingEntity) {
        if (pLivingEntity instanceof InfectedEntity infectedPlayer) {
            infectedPlayer.contagion_setInfection(false);
            if (infectedPlayer.contagion_getInitialInfectionDuration() == 0L) {
                infectedPlayer.contagion_setInitialInfectionDuration((long) MainConfig.getInfectionDuration() * 20);
            }
            infectedPlayer.contagion_setInfectionTicks(infectedPlayer.contagion_getInitialInfectionDuration());
            infectedPlayer.contagion_setInfectionCooldown(defaultCooldown * 20);
        }
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide()) {
            if (pLivingEntity instanceof InfectedEntity infectedEntity) {
                if (!pLivingEntity.isAlive()) {
                    infectedEntity.contagion_setInfection(false);
                    return;
                }
                if (!infectedEntity.contagion_isPlayerInfected()) {
                    MobEffectInstance effectInstance = pLivingEntity.getEffect(ContagionEffects.INFECTION.get());
                    long duration = (effectInstance != null && effectInstance.getDuration() > 0)
                            ? effectInstance.getDuration()
                            : (long) MainConfig.getInfectionDuration() * 20;
                    infectedEntity.contagion_setInitialInfectionDuration(duration);
                    infectedEntity.contagion_setInfectionTicks(duration);
                    infectedEntity.contagion_setInfectionCooldown(defaultCooldown * 20);
                    infectedEntity.contagion_setInfection(true);
                }
                long currentTicks = infectedEntity.contagion_getInfectionTicks();
                infectedEntity.contagion_setInfectionTicks(currentTicks - 1);

                // Apply INFECTIOUS effect when reaching the last 40% of infection duration
                applyInfectiousEffect(pLivingEntity, infectedEntity);

                if (infectedEntity.contagion_getInfectionCooldown() != 0) {
                    infectedEntity.contagion_setInfectionCooldown(infectedEntity.contagion_getInfectionCooldown() - 1);
                }
                if (MainConfig.isEnableRandomSymptoms()) {
                    if ((infectedEntity.contagion_getInfectionCooldown() == 0) && (infectedEntity.contagion_getInfectionTicks() > (MainConfig.getRandomSymptomsDuration() * 20L))) {
                        if ((infectedEntity.contagion_getInfectionTicks() % 20) == 0) {
                            int randomValue = RandomSource.create().nextIntBetweenInclusive(1, 100);
                            if (randomValue > (100 - MainConfig.getRandomSymptomsChance())) {
                                randomValue = RandomSource.create().nextIntBetweenInclusive(1, 4);
                                switch (randomValue) {
                                    case 1:
                                        if (!MainConfig.isInfiniteWeaknessAndSlowness()) {
                                            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, (MainConfig.getRandomSymptomsDuration() * 20), 0));
                                        } else {
                                            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, -1, 0));
                                        }
                                        break;
                                    case 2:
                                        if (!MainConfig.isInfiniteWeaknessAndSlowness()) {
                                            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (MainConfig.getRandomSymptomsDuration() * 20), 0));
                                        } else {
                                            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, -1, 0));
                                        }
                                        break;
                                    case 3:
                                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, (MainConfig.getRandomSymptomsDuration() * 20), 0));
                                        break;
                                    case 4:
                                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, (MainConfig.getRandomSymptomsDuration() * 20), 0));
                                        break;
                                }
                                infectedEntity.contagion_setInfectionCooldown((MainConfig.getRandomSymptomsDuration() + defaultCooldown) * 20);
                            }
                        }
                    } else if (infectedEntity.contagion_getInfectionTicks() == (MainConfig.getRandomSymptomsDuration() * 20L / 2)) {
                        pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, (MainConfig.getRandomSymptomsDuration() * 20 / 2), 0));
                    }
                }

                if (infectedEntity.contagion_getInfectionTicks() <= 2) {
                    // Check if this is a Villager that should convert into a ZombieVillager instead of dying normally.
                    boolean villagerConverting = false;
                    if (MainConfig.isVillagerZombieConversion() && pLivingEntity instanceof Villager) {
                        villagerConverting = true;
                    }

                    if (!villagerConverting) {
                        // Normal death path for non-Villagers (e.g. players etc) -- keep totem logic intact.
                        if (MainConfig.isTotemPreventsDyingFromInfection()) {
                            if (!(pLivingEntity.getMainHandItem().is(Items.TOTEM_OF_UNDYING) || pLivingEntity.getOffhandItem().is(Items.TOTEM_OF_UNDYING))) {
                                infectedEntity.contagion_setPlayerDiedFromInfection(true);
                                removeInfectionEffects(pLivingEntity, infectedEntity);
                            }
                            pLivingEntity.setHealth(1.0f);
                            pLivingEntity.hurt(ContagionDamageTypes.of(pLivingEntity.level(), ContagionDamageTypes.INFECTION), 1000.0f);
                        } else {
                            infectedEntity.contagion_setPlayerDiedFromInfection(true);
                            removeInfectionEffects(pLivingEntity, infectedEntity);
                            pLivingEntity.kill();
                        }
                    }

                    if (villagerConverting && !infectedEntity.contagion_playerDiedFromInfection()) {
                        if (pLivingEntity instanceof Villager originalVillager) {
                            VillagerData villagerData = originalVillager.getVillagerData();
                            ZombieVillager zombie = new ZombieVillager(EntityType.ZOMBIE_VILLAGER, pLivingEntity.level());
                            zombie.setPos(originalVillager.getX(), originalVillager.getY(), originalVillager.getZ());
                            zombie.setAggressive(true);
                            zombie.setVillagerData(villagerData);
                            pLivingEntity.level().addFreshEntity(zombie);
                            removeInfectionEffects(pLivingEntity, infectedEntity);
                            pLivingEntity.kill();
                        }
                    }

                    infectedEntity.contagion_setPlayerDiedFromInfection(true);
                    infectedEntity.contagion_setInfection(false);
                    removeInfectionEffects(pLivingEntity, infectedEntity);
                }
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    /**
     * Apply the INFECTIOUS mob effect when infection reaches >= 40% progress (i.e., ticks <= initialDuration * 0.6).
     * Duration is calculated so that both INFECTION and INFECTIOUS expire at the same time.
     */
    private void applyInfectiousEffect(LivingEntity pLivingEntity, InfectedEntity infectedEntity) {
        int infectiousThreshold = (int)(infectedEntity.contagion_getInitialInfectionDuration() * 0.6);
        long ticksRemaining = infectedEntity.contagion_getInfectionTicks();
        if (!pLivingEntity.hasEffect(ContagionEffects.INFECTIOUS.get()) && ticksRemaining <= infectiousThreshold) {
            // Apply for exactly the remaining ticks of the infection so both expire simultaneously
            pLivingEntity.addEffect(new MobEffectInstance(ContagionEffects.INFECTIOUS.get(), (int)(ticksRemaining + 1), 0));
        }
    }

    /** Remove INFECTIOUS effect from an entity. */
    private void removeInfectionEffects(LivingEntity pLivingEntity, InfectedEntity infectedEntity) {
        pLivingEntity.removeEffect(ContagionEffects.INFECTIOUS.get());
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) { return true; }
}
