package net.petemc.contagion.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.client.ProtectionHudOverlay;
import net.petemc.contagion.effect.ContagionEffects;
import org.jetbrains.annotations.NotNull;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = Contagion.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.side.isClient()) {
                Minecraft mc = Minecraft.getInstance();
                assert mc.player != null;
                ProtectionHudOverlay.infectionProtection = getEffectiveInfectProtection(mc.player);
                if (ProtectionHudOverlay.infectionProtection != ProtectionHudOverlay.cachedInfectionProtection) {
                    ProtectionHudOverlay.cachedInfectionProtection = ProtectionHudOverlay.infectionProtection;
                }
            }
        }

        private static int getEffectiveInfectProtection(@NotNull Player clientPlayerEntity) {
            int effectInfectProtection;
            if (ProtectionHudOverlay.receivedBaseInfectionChance > 100) {
                effectInfectProtection = 0;
            } else {
                effectInfectProtection = 100 - ProtectionHudOverlay.receivedBaseInfectionChance;
            }
            if (ProtectionHudOverlay.receivedArmorLowersInfectionChance) {
                effectInfectProtection = effectInfectProtection + (clientPlayerEntity.getArmorValue() * 3);
            }
            if (effectInfectProtection > (100 - ProtectionHudOverlay.receivedMinimumInfectionChance)) {
                effectInfectProtection = 100 - ProtectionHudOverlay.receivedMinimumInfectionChance;
            }
            if (clientPlayerEntity.hasEffect(ContagionEffects.IMMUNITY.get())) {
                effectInfectProtection = 100;
            }
            return effectInfectProtection;
        }
    }

    @Mod.EventBusSubscriber(modid = Contagion.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("contagion_protection", ProtectionHudOverlay.HUD_PROTECTION);
        }
    }
}
