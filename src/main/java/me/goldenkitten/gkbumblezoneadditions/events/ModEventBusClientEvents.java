package me.goldenkitten.gkbumblezoneadditions.events;

import me.goldenkitten.gkbumblezoneadditions.GKBumbleZoneAdditions;
import me.goldenkitten.gkbumblezoneadditions.client.TraderBeeModel;
import me.goldenkitten.gkbumblezoneadditions.client.layer.ModLayers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GKBumbleZoneAdditions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModLayers.TRADER_BEE_LAYER, TraderBeeModel::createBodyLayer);
    }
}
