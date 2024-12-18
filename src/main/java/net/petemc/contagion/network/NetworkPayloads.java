package net.petemc.contagion.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.petemc.contagion.Contagion;

import java.util.UUID;

public class NetworkPayloads {
    public static final Identifier HUD_DATA_PACKET_ID = Identifier.of(Contagion.MOD_ID, "transmit_data_from_hud_display");

    public record hudDataPayload(Integer baseInfectionChance, Integer minimumInfectionChance, Boolean armorLowersInfectionChance) implements CustomPayload {
        public static final Id<hudDataPayload> ID = new Id<>(HUD_DATA_PACKET_ID);
        public static final PacketCodec<RegistryByteBuf, hudDataPayload> CODEC =
                PacketCodec.tuple(
                        PacketCodecs.INTEGER, hudDataPayload::baseInfectionChance,
                        PacketCodecs.INTEGER, hudDataPayload::minimumInfectionChance,
                        PacketCodecs.BOOL, hudDataPayload::armorLowersInfectionChance,
                        hudDataPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
