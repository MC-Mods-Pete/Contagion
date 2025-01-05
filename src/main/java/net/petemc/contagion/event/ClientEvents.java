package net.petemc.contagion.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.network.ClientPayloadHandler;
import net.petemc.contagion.network.ProtectionHUDInfoNetworkPayload;

public class ClientEvents {
    @EventBusSubscriber(modid = Contagion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientForgeEvents {
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
