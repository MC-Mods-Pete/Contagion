package net.petemc.contagion.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.petemc.contagion.Config;
import net.petemc.contagion.Contagion;
import net.petemc.contagion.client.ProtectionHudOverlay;
import net.petemc.contagion.effect.ContagionEffects;
import net.petemc.contagion.item.ContagionItems;
import net.petemc.contagion.network.ProtectionHUDInfoNetworkPayload;
import net.petemc.contagion.potion.ContagionPotions;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber (modid = Contagion.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ContagionEvents {
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if(!event.getLevel().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                PacketDistributor.sendToPlayer(player, new ProtectionHUDInfoNetworkPayload(Config.baseInfectionChance, Config.minimumInfectionChance, Config.armorLowersInfectionChance));
            }
        }
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RenderGuiEvent.Pre event) {
        ProtectionHudOverlay.HUD_INSTANCE.render(event.getGuiGraphics(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) {
            ProtectionHudOverlay.infectionProtection = getEffectiveInfectProtection(event.getEntity());
            if (ProtectionHudOverlay.infectionProtection != ProtectionHudOverlay.cachedInfectionProtection) {
                ProtectionHudOverlay.cachedInfectionProtection = ProtectionHudOverlay.infectionProtection;
            }
        }
    }

    private static int getEffectiveInfectProtection(@NotNull Player clientPlayerEntity) {
        int effectInfectProtection;
        if (ProtectionHudOverlay.receivedBaseInfectionChance > 100) {
            effectInfectProtection = 0;
        } else {
            effectInfectProtection = 100 - ProtectionHudOverlay.receivedBaseInfectionChance;
        }
        if (ProtectionHudOverlay.receivedArmorLowersInfectionChance) {
            effectInfectProtection = effectInfectProtection + (clientPlayerEntity.getArmorValue() * 3);
        }
        if (effectInfectProtection > (100 - ProtectionHudOverlay.receivedMinimumInfectionChance)) {
            effectInfectProtection = 100 - ProtectionHudOverlay.receivedMinimumInfectionChance;
        }
        if (clientPlayerEntity.hasEffect(ContagionEffects.IMMUNITY)) {
            effectInfectProtection = 100;
        }
        return effectInfectProtection;
    }

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, ContagionItems.GOLD_STREAKED_FLESH.get(), ContagionPotions.CURE_POTION);
        builder.addMix(ContagionPotions.CURE_POTION, Items.REDSTONE, ContagionPotions.LONG_CURE_POTION);
    }
}
