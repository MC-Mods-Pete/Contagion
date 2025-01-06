package net.petemc.contagion.event;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.petemc.contagion.Config;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.client.ProtectionHudOverlay;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.contagion.network.ProtectionHUDInfoNetworkPayload;
import net.petemc.contagion.potion.ContagionPotions;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber (value = Dist.CLIENT, modid = Contagion.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ContagionEventsClient {
    @SubscribeEvent
    public static void registerGuiOverlays(RenderGuiEvent.Pre event) {
        ProtectionHudOverlay.HUD_INSTANCE.render(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) {
            Minecraft mc = Minecraft.getInstance();
            assert mc.level != null;
            if (mc.level.isClientSide) {
                assert mc.player != null;
                ProtectionHudOverlay.infectionProtection = getEffectiveInfectProtection(mc.player);
                if (ProtectionHudOverlay.infectionProtection != ProtectionHudOverlay.cachedInfectionProtection) {
                    ProtectionHudOverlay.cachedInfectionProtection = ProtectionHudOverlay.infectionProtection;
                }
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
        if (clientPlayerEntity.hasEffect(ContagionEffects.IMMUNITY)) {
            effectInfectProtection = 100;
        }
        return effectInfectProtection;
    }
}
