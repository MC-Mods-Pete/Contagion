package net.petemc.contagion.event;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.petemc.contagion.casts.InfectedEntity;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;

public class GoldenAppleCureEvent {
    public static void onFinishUsingItem(LivingEntity entity, ItemStack usedItemStack) {
        if (entity.level().isClientSide()) return;

        if (!(entity instanceof Player)) return;

        if (!entity.hasEffect(ContagionEffects.INFECTION)) return;

        if (usedItemStack.isEmpty()) return;

        boolean shouldCure = false;

        if (usedItemStack.getItem() == Items.GOLDEN_APPLE && MainConfig.isGoldenAppleCuresInfection()) {
            shouldCure = true;
        } else if (usedItemStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE && MainConfig.isEnchantedGoldenAppleCuresInfection()) {
            shouldCure = true;
        }

        if (!shouldCure) return;

        curePlayerEffects(entity);
    }

    private static void curePlayerEffects(LivingEntity entity) {
        entity.removeEffect(ContagionEffects.INFECTION);
        entity.removeEffect(ContagionEffects.INFECTIOUS);
        entity.removeEffect(MobEffects.WEAKNESS);
        entity.removeEffect(MobEffects.SLOWNESS);
        if (entity instanceof Player player && player instanceof InfectedEntity infected) {
            infected.contagion_setInfection(false);
            infected.contagion_setInfectionTicks(0);
        }
    }
}