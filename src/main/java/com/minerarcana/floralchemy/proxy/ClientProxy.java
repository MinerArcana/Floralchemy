package com.minerarcana.floralchemy.proxy;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.block.flower.SubTilePetroPetunia;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPIClient;

public class ClientProxy implements IProxy {
    @Override
    public void registerModels() {
        BotaniaAPIClient.registerSubtileModel(SubTilePetroPetunia.NAME,
                new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, SubTilePetroPetunia.NAME), "normal"),
                new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, SubTilePetroPetunia.NAME), "inventory"));
    }
}
