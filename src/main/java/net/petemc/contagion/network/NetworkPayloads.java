package net.petemc.contagion.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.petemc.contagion.Contagion;

public class NetworkPayloads {
    public static final Identifier HUD_DATA_PACKET_ID = Identifier.fromNamespaceAndPath(Contagion.MOD_ID, "transmit_data_from_hud_display");

    public record hudDataPayload(Integer baseInfectionChance, Integer minimumInfectionChance, Boolean armorLowersInfectionChance) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<hudDataPayload> TYPE = new CustomPacketPayload.Type<>(HUD_DATA_PACKET_ID);
        public static final StreamCodec<RegistryFriendlyByteBuf, hudDataPayload> CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.INT, hudDataPayload::baseInfectionChance,
                        ByteBufCodecs.INT, hudDataPayload::minimumInfectionChance,
                        ByteBufCodecs.BOOL, hudDataPayload::armorLowersInfectionChance,
                        hudDataPayload::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
