package me.goldenkitten.gkbumblezoneadditions;

import com.mojang.logging.LogUtils;
import me.goldenkitten.gkbumblezoneadditions.client.TraderBeeRenderer;
import me.goldenkitten.gkbumblezoneadditions.entity.ModEntities;
import me.goldenkitten.gkbumblezoneadditions.events.ModEvents;
import me.goldenkitten.gkbumblezoneadditions.sound.ModSounds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(GKBumbleZoneAdditions.MODID)
public class GKBumbleZoneAdditions {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "gkbumblezoneadditions";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public GKBumbleZoneAdditions() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModSounds.register(modEventBus);
        ModEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ModEvents());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.BUMBLE_TRADER.get(), TraderBeeRenderer::new);
            //MenuScreens.register(ModMenuTypes.GEM_POLISHING_MENU.get(), GemPolishingStationScreen::new);
        }
    }
}
