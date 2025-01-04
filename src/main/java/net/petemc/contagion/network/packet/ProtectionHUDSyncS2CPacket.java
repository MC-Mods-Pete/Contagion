package net.petemc.contagion.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.petemc.contagion.client.ProtectionHudOverlay;

import java.util.function.Supplier;

public class ProtectionHUDSyncS2CPacket {
    private int baseInfectionChance = 80;
    private int minimumInfectionChance = 10;
    private boolean armorLowersInfectionChance = true;

    public ProtectionHUDSyncS2CPacket(int baseInfectionChance, int minimumInfectionChance, boolean armorLowersInfectionChance) {
        this.baseInfectionChance = baseInfectionChance;
        this.minimumInfectionChance = minimumInfectionChance;
        this.armorLowersInfectionChance = armorLowersInfectionChance;
    }

    public ProtectionHUDSyncS2CPacket(FriendlyByteBuf buf) {
        this.baseInfectionChance = buf.readInt();
        this.minimumInfectionChance = buf.readInt();
        this.armorLowersInfectionChance = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(baseInfectionChance);
        buf.writeInt(minimumInfectionChance);
        buf.writeBoolean(armorLowersInfectionChance);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // On the client side
            ProtectionHudOverlay.receivedBaseInfectionChance = baseInfectionChance;
            ProtectionHudOverlay.receivedMinimumInfectionChance = minimumInfectionChance;
            ProtectionHudOverlay.receivedArmorLowersInfectionChance = armorLowersInfectionChance;
        });
        return true;
    }

}
