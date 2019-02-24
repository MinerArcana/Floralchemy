package com.minerarcana.floralchemy.block.flower;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

import com.teamacronymcoders.base.blocks.BlockBase;
import com.teamacronymcoders.base.blocks.IHasBlockColor;
import com.teamacronymcoders.base.client.ClientHelper;
import com.teamacronymcoders.base.util.ColourHelper;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrystalthorn extends BlockBase implements IHasBlockColor {
	
	ResourceLocation crystalName;

	public BlockCrystalthorn(ResourceLocation crystalName) {
		super(Material.PLANTS, "crystalthorn_" + crystalName.getPath().substring(15, crystalName.getPath().length() - 4));
		this.crystalName = crystalName;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
		IResource resource = ClientHelper.getResource(crystalName);
		int color = Color.PINK.getRGB();
		if(resource != null) {
			InputStream stream = resource.getInputStream();
			color = ColourHelper.getColour(stream);
			try {
				stream.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		return color;
	}

}
