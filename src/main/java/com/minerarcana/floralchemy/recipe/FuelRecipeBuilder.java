package com.minerarcana.floralchemy.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minerarcana.floralchemy.content.FloralchemyRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class FuelRecipeBuilder {
    private final FluidIngredient ingredient;
    private int manaPerTick;
    private int burnTime;
    private final List<ICondition> conditions;

    public FuelRecipeBuilder(FluidIngredient ingredient) {
        this.ingredient = ingredient;
        this.conditions = Lists.newArrayList();
    }

    public FuelRecipeBuilder withManaPerTick(int manaPerTick) {
        this.manaPerTick = manaPerTick;
        return this;
    }

    public FuelRecipeBuilder withBurnTime(int burnTime) {
        this.burnTime = burnTime;
        return this;
    }

    public FuelRecipeBuilder withCondition(ICondition condition) {
        this.conditions.add(condition);
        return this;
    }

    public void build(ResourceLocation id, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        Objects.requireNonNull(ingredient, "Ingredient cannot be null");
        if (burnTime <= 0) {
            throw new IllegalArgumentException("burnTime must be positive");
        }
        if (manaPerTick <= 0) {
            throw new IllegalArgumentException("manaPerTick must be positive");
        }
        finishedRecipeConsumer.accept(new FuelFinishedRecipe(
                id,
                ingredient,
                manaPerTick,
                burnTime,
                conditions
        ));
    }

    public static FuelRecipeBuilder of(FluidIngredient fluidIngredient) {
        return new FuelRecipeBuilder(fluidIngredient);
    }

    private record FuelFinishedRecipe(
            ResourceLocation id,
            FluidIngredient ingredient,
            int manaPerTick,
            int burnTime,
            List<ICondition> conditions
    ) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("ingredient", ingredient.toJson());
            pJson.addProperty("manaPerTick", manaPerTick);
            pJson.addProperty("burnTime", burnTime);
            JsonArray conditions = new JsonArray();
            for (ICondition c : this.conditions) {
                conditions.add(CraftingHelper.serialize(c));
            }
            pJson.add("conditions", conditions);
        }

        @Override
        @NotNull
        public ResourceLocation getId() {
            return id;
        }

        @Override
        @NotNull
        public RecipeSerializer<?> getType() {
            return FloralchemyRecipes.FUEL_RECIPE_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
