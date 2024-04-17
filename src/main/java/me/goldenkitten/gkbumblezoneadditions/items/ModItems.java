package me.goldenkitten.gkbumblezoneadditions.items;

import com.telepathicgrunt.the_bumblezone.items.DispenserAddedSpawnEgg;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import me.goldenkitten.gkbumblezoneadditions.GKBumbleZoneAdditions;
import me.goldenkitten.gkbumblezoneadditions.entity.ModEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class ModItems
{
    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, GKBumbleZoneAdditions.MODID);
    public static final RegistryEntry<Item> BUMBLE_TRADER_TREAT = ITEMS.register("bumble_trader_treat", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> BUMBLE_TRADER_SPAWN_EGG = ITEMS.register("bumble_trader_spawn_egg", () -> new DispenserAddedSpawnEgg(ModEntities.BUMBLE_TRADER, 0xFFCA47, 0x68372A, (new Item.Properties())));
}