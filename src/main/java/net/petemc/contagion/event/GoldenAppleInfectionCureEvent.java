package net.petemc.contagion.event;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.casts.InfectedEntity;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionEffects;

@EventBusSubscriber(modid = Contagion.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class GoldenAppleInfectionCureEvent {

    @SubscribeEvent
    public static void onItemFinishUse(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity().level().isClientSide()) return;
        
        if (!(event.getEntity() instanceof Player)) return;

        if (!event.getEntity().hasEffect(ContagionEffects.INFECTION)) return;

        net.minecraft.world.item.ItemStack usedItemStack = event.getItem();
        if (usedItemStack.isEmpty()) return;

        boolean shouldCure = false;

        if (usedItemStack.getItem() == Items.GOLDEN_APPLE && MainConfig.isGoldenAppleCuresInfection()) {
            shouldCure = true;
        } else if (usedItemStack.getItem() == Items.ENCHANTED_GOLDEN_APPLE && MainConfig.isEnchantedGoldenAppleCuresInfection()) {
            shouldCure = true;
        }

        if (!shouldCure) return;

        curePlayerEffects(event.getEntity());
    }

    private static void curePlayerEffects(LivingEntity entity) {
        entity.removeEffect(ContagionEffects.INFECTION);
        entity.removeEffect(ContagionEffects.INFECTIOUS);
        entity.removeEffect(MobEffects.WEAKNESS);
        entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        if (entity instanceof Player player && player instanceof InfectedEntity infected) {
            infected.contagion_setInfection(false);
            infected.contagion_setInfectionTicks(0);
        }
    }
}

