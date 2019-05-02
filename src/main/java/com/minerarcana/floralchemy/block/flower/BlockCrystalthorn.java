package com.minerarcana.floralchemy.block.flower;

import javax.annotation.Nullable;

import com.teamacronymcoders.base.blocks.BlockBaseNoModel;
import com.teamacronymcoders.base.blocks.IHasBlockColor;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrystalthorn extends BlockBaseNoModel implements IHasBlockColor {

	protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.30000001192092896D, 0.0D, 0.30000001192092896D, 0.699999988079071D, 0.6000000238418579D, 0.699999988079071D);
	
	private final ResourceLocation crystalName;
	private final int crystalMetadata;

	public BlockCrystalthorn(ResourceLocation crystalName, int crystalMetadata) {
		super(Material.PLANTS, "crystalthorn_" + crystalName.getPath());
		this.crystalName = crystalName;
		this.crystalMetadata = crystalMetadata;
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
		ItemStack stack = new ItemStack(Item.getByNameOrId(crystalName.toString()), 1 + fortune, crystalMetadata);
		drops.add(stack);
    }
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
		player.attackEntityFrom(DamageSource.CACTUS, 3F);
		super.harvestBlock(worldIn, player, pos, state, te, stack);
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
		return Minecraft.getMinecraft().getItemColors().colorMultiplier(new ItemStack(crystalItem), 0);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BUSH_AABB;
    }

	@Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
}
