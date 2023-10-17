package me.goldenkitten.gkbumblezoneadditions.entity;

import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import me.goldenkitten.gkbumblezoneadditions.GKBumbleZoneAdditions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GKBumbleZoneAdditions.MODID);

    public static final RegistryObject<EntityType<TraderBeeEntity>> TRADER_BEE =
            ENTITY_TYPES.register("trader_bee", () -> PlatformHooks.createEntityType(TraderBeeEntity::new, MobCategory.CREATURE, 1.2F, false, 16, 3, "trader_bee"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
