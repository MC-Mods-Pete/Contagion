package net.petemc.contagion.network;

import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.petemc.contagion.config.ContagionConfig;

public class ServerPlayerJoinEvent {

    private static ServerPlayNetworkHandler pHandler = null;
    private static PacketSender pSender = null;
    private static MinecraftServer pServer = null;

    public ServerPlayerJoinEvent() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            pHandler = handler;
            pSender = sender;
            pServer = server;
            execute();
        });
    }

    public static void execute() {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeInt(ContagionConfig.INSTANCE.baseInfectionChance);
        buf.writeInt(ContagionConfig.INSTANCE.minimumInfectionChance);
        buf.writeBoolean(ContagionConfig.INSTANCE.armorLowersInfectionChance);
        ServerPlayNetworking.send(pHandler.getPlayer(), NetworkPayloads.HUD_DATA_PACKET_ID, buf);
    }

    public static void registerEvent() { new ServerPlayerJoinEvent(); }
}
