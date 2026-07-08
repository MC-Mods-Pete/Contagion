package net.petemc.contagion.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionInfectionEffect;
import net.petemc.contagion.network.ContagionNetworkMessages;
import net.petemc.contagion.network.packet.ProtectionHUDSyncS2CPacket;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = Contagion.MOD_ID)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if(!event.getLevel().isClientSide()) {
                if(event.getEntity() instanceof ServerPlayer player) {
                    ContagionNetworkMessages.sendToPlayer(new ProtectionHUDSyncS2CPacket(MainConfig.getBaseInfectionChance(), MainConfig.getMinimumInfectionChance(), MainConfig.isArmorLowersInfectionChance()), player);
                }
            }
        }

        /**
         * When the INFECTION effect is removed (e.g. via /effect clear, cure, immunity),
         * reset the internal infection state. This ensures that the init block in
         * ContagionInfectionEffect.applyEffectTick runs correctly on the next /effect give
         * and picks up the new duration instead of continuing with the old MainConfig value.
         */
        @SubscribeEvent
        public static void onInfectionEffectRemoved(MobEffectEvent.Remove event) {
            if (event.getEffect() instanceof ContagionInfectionEffect) {
                LivingEntity entity = event.getEntity();
                ContagionInfectionEffect.resetValues(entity);
            }
        }
    }
}
