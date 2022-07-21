package com.minerarcana.floralchemy.recipe;

import com.minerarcana.floralchemy.content.FloralchemyRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public record FuelRecipe(
        ResourceLocation id,
        FluidIngredient ingredient,
        int burnTime,
        int manaPerTick
) implements IFuelRecipe {

    @Override
    public int getBurnTime() {
        return burnTime();
    }

    @Override
    public int getManaPerTick() {
        return manaPerTick();
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(FuelInventory pContainer, Level pLevel) {
        return ingredient.test(pContainer.getFluidStack());
    }

    @Override
    @Nonnull
    public ItemStack assemble(@NotNull FuelInventory pContainer) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    @NotNull
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return id();
    }

    @Override
    @NotNull
    public RecipeSerializer<?> getSerializer() {
        return FloralchemyRecipes.FUEL_RECIPE_SERIALIZER.get();
    }

    @Override
    @NotNull
    public RecipeType<?> getType() {
        return FloralchemyRecipes.FUEL_RECIPE_TYPE.get();
    }
}
