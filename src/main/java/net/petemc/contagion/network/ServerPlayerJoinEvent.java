package net.petemc.contagion.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.petemc.contagion.config.MainConfig;

public class ServerPlayerJoinEvent {

    private static ServerGamePacketListenerImpl pHandler = null;
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
        ServerPlayNetworking.send(pHandler.getPlayer(), new NetworkPayloads.hudDataPayload(MainConfig.getBaseInfectionChance(), MainConfig.getMinimumInfectionChance(), MainConfig.isArmorLowersInfectionChance()));
    }

    public static void registerEvent() { new ServerPlayerJoinEvent(); }
}
