package me.goldenkitten.gkbumblezoneadditions.items.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.goldenkitten.gkbumblezoneadditions.GKBumbleZoneAdditions;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BumbleTraderTreatRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;

    public BumbleTraderTreatRecipe(NonNullList<Ingredient> inputItems, ItemStack output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        return inputItems.get(0).test(pContainer.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer pContainer, @NotNull RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<BumbleTraderTreatRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "gem_polishing";
    }

    public static class Serializer implements RecipeSerializer<BumbleTraderTreatRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(GKBumbleZoneAdditions.MODID, "bumble_trader_treat");

        @Override
        public @NotNull BumbleTraderTreatRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new BumbleTraderTreatRecipe(inputs, output, pRecipeId);
        }

        @Override
        public @Nullable BumbleTraderTreatRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            inputs.replaceAll(ignored -> Ingredient.fromNetwork(pBuffer));

            ItemStack output = pBuffer.readItem();
            return new BumbleTraderTreatRecipe(inputs, output, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, BumbleTraderTreatRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(RegistryAccess.EMPTY), false);
        }
    }
}
