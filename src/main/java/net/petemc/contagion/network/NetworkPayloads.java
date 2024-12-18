package net.petemc.contagion.network;

import net.minecraft.util.Identifier;
import net.petemc.contagion.Contagion;

public class NetworkPayloads {
    public static final Identifier HUD_DATA_PACKET_ID = Identifier.of(Contagion.MOD_ID, "transmit_data_from_hud_display");
}
