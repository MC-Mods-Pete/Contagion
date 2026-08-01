package net.petemc.contagion.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.petemc.contagion.casts.InfectedEntity;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;

public class GoldenAppleCureEvent {
    public static void onFinishUsingItem(LivingEntity entity, ItemStack usedItemStack) {
        if (entity.getWorld().isClient()) return;

        if (!(entity instanceof PlayerEntity)) return;

        if (!entity.hasStatusEffect(ContagionEffects.INFECTION)) return;

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
        entity.removeStatusEffect(ContagionEffects.INFECTION);
        entity.removeStatusEffect(ContagionEffects.INFECTIOUS);
        entity.removeStatusEffect(StatusEffects.WEAKNESS);
        entity.removeStatusEffect(StatusEffects.SLOWNESS);
        if (entity instanceof PlayerEntity player && player instanceof InfectedEntity infected) {
            infected.contagion_setInfection(false);
            infected.contagion_setInfectionTicks(0);
        }
    }
}