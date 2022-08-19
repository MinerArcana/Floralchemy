package com.minerarcana.floralchemy.content;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.recipe.FluidIngredient;
import com.minerarcana.floralchemy.recipe.FuelRecipeBuilder;
import com.minerarcana.floralchemy.recipe.FuelRecipeSerializer;
import com.minerarcana.floralchemy.recipe.IFuelRecipe;
import com.minerarcana.floralchemy.recipe.condition.TagCondition;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class FloralchemyRecipes {

    public static DeferredRegister<RecipeType<?>> RECIPE_TYPE = DeferredRegister.create(
            Registry.RECIPE_TYPE_REGISTRY,
            Floralchemy.ID
    );

    public static RegistryObject<RecipeType<IFuelRecipe>> FUEL_RECIPE_TYPE = RECIPE_TYPE.register(
            "fuel",
            () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return Floralchemy.ID + ":fuel";
                }
            }
    );

    public static RegistryEntry<FuelRecipeSerializer> FUEL_RECIPE_SERIALIZER = Floralchemy.getRegistrate()
            .object("fuel")
            .simple(RecipeSerializer.class, FuelRecipeSerializer::new);

    public static void setup() {
        RECIPE_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void fuelRecipeData(RegistrateRecipeProvider provider) {
        FuelRecipeBuilder.of(tag("oil"))
                .withBurnTime(100)
                .withManaPerTick(50)
                .withCondition(tagExist("oil"))
                .build(Floralchemy.rl("oil"), provider);

        FuelRecipeBuilder.of(tag("crude_oil"))
                .withBurnTime(100)
                .withManaPerTick(50)
                .withCondition(tagExist("crude_oil"))
                .build(Floralchemy.rl("crude_oil"), provider);

        FuelRecipeBuilder.of(tag("diesel"))
                .withBurnTime(350)
                .withManaPerTick(50)
                .withCondition(tagExist("diesel"))
                .build(Floralchemy.rl("diesel"), provider);

        FuelRecipeBuilder.of(tag("biodiesel"))
                .withBurnTime(200)
                .withManaPerTick(50)
                .withCondition(tagExist("biodiesel"))
                .build(Floralchemy.rl("biodiesel"), provider);

        FuelRecipeBuilder.of(tag("ethanol"))
                .withBurnTime(250)
                .withManaPerTick(50)
                .withCondition(tagExist("ethanol"))
                .build(Floralchemy.rl("ethanol"), provider);

        FuelRecipeBuilder.of(tag("kerosene"))
                .withBurnTime(500)
                .withManaPerTick(50)
                .withCondition(tagExist("kerosene"))
                .build(Floralchemy.rl("kerosene"), provider);
    }

    private static FluidIngredient tag(String path) {
        return FluidIngredient.of(
                Objects.requireNonNull(ForgeRegistries.FLUIDS.tags())
                        .createTagKey(new ResourceLocation("forge", path))
        );
    }

    private static ICondition tagExist(String path) {
        return new NotCondition(new TagCondition<>(
                new ResourceLocation("forge", path),
                Registry.FLUID_REGISTRY
        ));
    }
}
