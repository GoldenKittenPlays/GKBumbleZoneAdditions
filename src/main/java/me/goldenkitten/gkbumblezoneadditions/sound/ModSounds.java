package me.goldenkitten.gkbumblezoneadditions.sound;

import me.goldenkitten.gkbumblezoneadditions.GKBumbleZoneAdditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class ModSounds
{
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, GKBumbleZoneAdditions.MODID);
    public static final RegistryObject<SoundEvent> BUMBLE_TRADER_TRADE_ACCEPTED = registerSoundEvents("bumble_trader_trade_accepted");
    public static final RegistryObject<SoundEvent> BUMBLE_TRADER_TRADE_DENIED = registerSoundEvents("bumble_trader_trade_denied");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name)
    {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(GKBumbleZoneAdditions.MODID, name)));
    }

    public static void register(IEventBus eventBus)
    {
        SOUND_EVENTS.register(eventBus);
    }
}
