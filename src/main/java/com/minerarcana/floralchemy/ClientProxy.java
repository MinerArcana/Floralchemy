package com.minerarcana.floralchemy;

import com.minerarcana.floralchemy.client.TileEntityFloodedSoilRenderer;
import com.minerarcana.floralchemy.client.TileEntityLeakyCauldronRenderer;
import com.minerarcana.floralchemy.tileentity.TileEntityFloodedSoil;
import com.minerarcana.floralchemy.tileentity.TileEntityLeakyCauldron;

import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerModels() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFloodedSoil.class, new TileEntityFloodedSoilRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLeakyCauldron.class, new TileEntityLeakyCauldronRenderer());
    }
}
