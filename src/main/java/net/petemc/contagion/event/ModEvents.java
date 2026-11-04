package net.petemc.contagion.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.config.MainConfig;
import net.petemc.contagion.effect.ContagionInfectionEffect;
import net.petemc.contagion.network.ProtectionHUDInfoNetworkPayload;

@EventBusSubscriber (modid = Contagion.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                PacketDistributor.sendToPlayer(player, new ProtectionHUDInfoNetworkPayload(MainConfig.getBaseInfectionChance(), MainConfig.getMinimumInfectionChance(), MainConfig.isArmorLowersInfectionChance()));
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
        if (event.getEffect().value() instanceof ContagionInfectionEffect) {
            LivingEntity entity = event.getEntity();
            ContagionInfectionEffect.resetValues(entity);
        }
    }
}
