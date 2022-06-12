package com.minerarcana.floralchemy.recipe;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.EmptyHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class FuelInventory extends RecipeWrapper {
    private final FluidStack fluidStack;

    public FuelInventory(FluidStack fluidStack) {
        super(new EmptyHandler());
        this.fluidStack = fluidStack;
    }

    public FluidStack getFluidStack() {
        return fluidStack;
    }
}
