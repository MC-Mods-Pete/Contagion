package net.petemc.contagion.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
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
}
