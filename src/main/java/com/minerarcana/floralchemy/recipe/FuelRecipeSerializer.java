package com.minerarcana.floralchemy.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class FuelRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<FuelRecipe> {
    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public FuelRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
        return new FuelRecipe(
                pRecipeId,
                FluidIngredient.fromJson(pSerializedRecipe.get("ingredient")),
                GsonHelper.getAsInt(pSerializedRecipe, "burnTime", 100),
                GsonHelper.getAsInt(pSerializedRecipe, "manaPerTick", 50)
        );
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public FuelRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        return new FuelRecipe(
                pRecipeId,
                FluidIngredient.fromNetwork(pBuffer),
                pBuffer.readInt(),
                pBuffer.readInt()
        );
    }

    @Override
    @ParametersAreNonnullByDefault
    public void toNetwork(FriendlyByteBuf pBuffer, FuelRecipe pRecipe) {
        pRecipe.ingredient().toNetwork(pBuffer);
        pBuffer.writeInt(pRecipe.burnTime());
        pBuffer.writeInt(pRecipe.getManaPerTick());
    }
}
