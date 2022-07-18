package com.minerarcana.floralchemy.compat.botania.recipe;

import com.google.common.base.Suppliers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minerarcana.floralchemy.compat.botania.FloralchemyBotaniaContent;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PetalRecipeBuilder {
    private final ItemLike output;
    private final List<Ingredient> ingredients;

    public PetalRecipeBuilder(ItemLike output) {
        this.output = output;
        this.ingredients = Lists.newArrayList();
    }

    public PetalRecipeBuilder addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    public void build(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        ResourceLocation itemId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output.asItem()));
        build(
                new ResourceLocation(
                        itemId.getNamespace(),
                        "petal_apothecary/" + itemId.getPath()
                ),
                finishedRecipeConsumer
        );
    }

    public void build(@NotNull ResourceLocation location, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        finishedRecipeConsumer.accept(new PetalFinishedRecipe(
                location,
                output,
                ingredients
        ));
    }

    public static PetalRecipeBuilder of(ItemLike output) {
        return new PetalRecipeBuilder(output);
    }

    public record PetalFinishedRecipe(
            ResourceLocation id,
            ItemLike output,
            List<Ingredient> ingredients
    ) implements FinishedRecipe {
        private static final Supplier<RecipeSerializer<?>> SERIALIZER = Suppliers.memoize(() ->
                ForgeRegistries.RECIPE_SERIALIZERS.getValue(FloralchemyBotaniaContent.rl("petal_apothecary"))
        );

        @Override
        public void serializeRecipeData(@NotNull JsonObject pJson) {
            pJson.add("output", ItemNBTHelper.serializeStack(new ItemStack(this.output())));
            JsonArray ingredients = new JsonArray();
            for (Ingredient ingredient : this.ingredients()) {
                ingredients.add(ingredient.toJson());
            }
            pJson.add("ingredients", ingredients);
        }

        @Override
        @NotNull
        public ResourceLocation getId() {
            return this.id();
        }

        @Override
        @NotNull
        public RecipeSerializer<?> getType() {
            return SERIALIZER.get();
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
