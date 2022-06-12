package com.minerarcana.floralchemy.content;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.recipe.FuelRecipeSerializer;
import com.minerarcana.floralchemy.recipe.IFuelRecipe;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

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
}
