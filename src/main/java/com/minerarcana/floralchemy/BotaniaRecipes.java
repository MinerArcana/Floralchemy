package com.minerarcana.floralchemy;

import com.minerarcana.floralchemy.block.flower.SubTilePetroPetunia;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class BotaniaRecipes {
    public static RecipePetals recipePetroPetunia;

    public static void init() {
        recipePetroPetunia = BotaniaAPI.registerPetalRecipe(ItemBlockSpecialFlower.ofType(SubTilePetroPetunia.NAME),
                "redstoneRoot", "runeWaterB", "runeFireB", "petalOrange", "petalBlack", "petalBrown", "elvenDragonstone");
    }
}
