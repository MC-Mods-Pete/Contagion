package net.petemc.contagion.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.petemc.contagion.Contagion;
import org.jetbrains.annotations.NotNull;

public record ProtectionHUDInfoNetworkPayload(int baseInfectionChance, int minimumInfectionChance, boolean armorLowersInfectionChance) implements CustomPacketPayload {

    public static final Type<ProtectionHUDInfoNetworkPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Contagion.MOD_ID, "hud_protection_info"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'name' will be encoded and decoded as a string
    // 'age' will be encoded and decoded as an integer
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, ProtectionHUDInfoNetworkPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ProtectionHUDInfoNetworkPayload::baseInfectionChance,
            ByteBufCodecs.INT,
            ProtectionHUDInfoNetworkPayload::minimumInfectionChance,
            ByteBufCodecs.BOOL,
            ProtectionHUDInfoNetworkPayload::armorLowersInfectionChance,
            ProtectionHUDInfoNetworkPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
