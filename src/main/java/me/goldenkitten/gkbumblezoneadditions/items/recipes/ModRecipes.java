package me.goldenkitten.gkbumblezoneadditions.items.recipes;

import me.goldenkitten.gkbumblezoneadditions.GKBumbleZoneAdditions;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GKBumbleZoneAdditions.MODID);

    public static final RegistryObject<RecipeSerializer<BumbleTraderTreatRecipe>> BUMBLE_TRADER_TREAT_SERIALIZER =
            SERIALIZERS.register("bumble_trader_treat", () -> BumbleTraderTreatRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
