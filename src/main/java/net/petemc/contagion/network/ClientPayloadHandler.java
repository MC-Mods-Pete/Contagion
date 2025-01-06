package net.petemc.contagion.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.client.ProtectionHudOverlay;

public class ClientPayloadHandler {
    public static void handleHUDInfoData(final ProtectionHUDInfoNetworkPayload data, final IPayloadContext context) {
        // Do something with the data, on the main thread
        ProtectionHudOverlay.receivedBaseInfectionChance = data.baseInfectionChance();
        ProtectionHudOverlay.receivedMinimumInfectionChance = data.minimumInfectionChance();
        ProtectionHudOverlay.receivedArmorLowersInfectionChance = data.armorLowersInfectionChance();
        Contagion.LOGGER.info("Received: {}", data.baseInfectionChance());
    }

    @EventBusSubscriber(modid = Contagion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class RegisterClientPayloadHandler {
        @SubscribeEvent
        public static void register(final RegisterPayloadHandlersEvent event) {
            final PayloadRegistrar registrar = event.registrar("1");
            registrar.playToClient(
                    ProtectionHUDInfoNetworkPayload.TYPE,
                    ProtectionHUDInfoNetworkPayload.STREAM_CODEC,
                    ClientPayloadHandler::handleHUDInfoData
            );
        }
    }
}
