package com.minerarcana.floralchemy.recipe;

import net.minecraft.world.item.crafting.Recipe;

public interface IFuelRecipe extends Recipe<FuelInventory> {

    int getBurnTime();

    int getManaPerTick();
}
