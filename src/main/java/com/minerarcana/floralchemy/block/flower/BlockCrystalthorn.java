package com.minerarcana.floralchemy.block.flower;

import com.teamacronymcoders.base.blocks.BlockBase;
import com.teamacronymcoders.base.blocks.IHasBlockColor;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrystalthorn extends BlockBase implements IHasBlockColor {

	private final ResourceLocation crystalName;
	private final int crystalMetadata;

	public BlockCrystalthorn(ResourceLocation crystalName, int crystalMetadata) {
		super(Material.PLANTS, "crystalthorn_" + crystalName.getPath());
		this.crystalName = crystalName;
		this.crystalMetadata = crystalMetadata;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
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
		Item crystalItem = Item.getByNameOrId(crystalName.toString());
		//TODO Make server-safe
		return Minecraft.getMinecraft().getItemColors().colorMultiplier(new ItemStack(crystalItem), crystalMetadata);

//			IResource resource = ClientHelper.getResource(crystalName);
//			if(resource != null) {
//			InputStream stream = resource.getInputStream();
//			color = ColourHelper.getColour(stream);
//			try {
//				stream.close();
//			}
//			catch(IOException e) {
//				e.printStackTrace();
//			}
//			}
	}

}
