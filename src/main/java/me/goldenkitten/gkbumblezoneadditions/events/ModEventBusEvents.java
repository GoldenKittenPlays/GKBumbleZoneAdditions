package me.goldenkitten.gkbumblezoneadditions.events;

import me.goldenkitten.gkbumblezoneadditions.GKBumbleZoneAdditions;
import me.goldenkitten.gkbumblezoneadditions.entity.ModEntities;
import me.goldenkitten.gkbumblezoneadditions.entity.TraderBeeEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GKBumbleZoneAdditions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.TRADER_BEE.get(), TraderBeeEntity.getAttributeBuilder().build());
    }
}
