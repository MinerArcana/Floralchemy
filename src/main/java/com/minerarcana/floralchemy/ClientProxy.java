package com.minerarcana.floralchemy;

import com.minerarcana.floralchemy.client.TileEntityFloodedSoilRenderer;
import com.minerarcana.floralchemy.tileentity.TileEntityFloodedSoil;

import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerModels() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFloodedSoil.class, new TileEntityFloodedSoilRenderer());
    }
}
