package net.petemc.contagion.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.petemc.contagion.Config;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.network.ContagionNetworkMessages;
import net.petemc.contagion.network.packet.ProtectionHUDSyncS2CPacket;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = Contagion.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if (!event.getLevel().isClientSide()) {
                if (event.getEntity() instanceof ServerPlayer player) {
                    ContagionNetworkMessages.sendToPlayer(new ProtectionHUDSyncS2CPacket(Config.baseInfectionChance, Config.minimumInfectionChance, Config.armorLowersInfectionChance), player);
                }
            }
        }
    }
}
