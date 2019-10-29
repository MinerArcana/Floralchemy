package com.minerarcana.floralchemy.item;

import java.util.List;

import com.minerarcana.floralchemy.block.BlockCrystalthorn;
import com.teamacronymcoders.base.client.models.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockCrystalthorn extends ItemBlockTinted<BlockCrystalthorn> implements IHasModel{

    public ItemBlockCrystalthorn(BlockCrystalthorn block) {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<ModelResourceLocation> getModelResourceLocations(List<ModelResourceLocation> models) {
        return models;
    }
}