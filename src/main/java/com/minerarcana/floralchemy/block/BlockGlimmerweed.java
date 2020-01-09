package com.minerarcana.floralchemy.block;

import com.minerarcana.floralchemy.FloraObjectHolder;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGlimmerweed extends BlockBaseBush {

	//Only need one vertical state
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	protected static final AxisAlignedBB REED_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
	
    public BlockGlimmerweed() {
        super("glimmerweed", "water");
        this.setLightLevel(0.7F);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        state = state.getActualState(source, pos);
        
        switch(state.getValue(FACING)) {
        	case SOUTH: return SOUTH_AABB;
        	case NORTH: return NORTH_AABB;
        	case WEST: return WEST_AABB;
        	case EAST: return EAST_AABB;
			default: return REED_AABB; 
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta)); 
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING });
    }

    @Override
    public boolean canSustainBush(IBlockState state)
    {
        return state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.STONE || state.getBlock() == this || state.getBlock() == FloraObjectHolder.FLOODED_SOIL;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
    	IBlockState state = worldIn.getBlockState(pos.offset(side.getOpposite()));
    	if(side.getAxis() == Axis.Y) {
    		return canSustainBush(state);
    	}
    	//Prevent sideways chaining of glimmerweed whilst still allowing vertical stacking
    	else {
    		return worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock() != this ? canSustainBush(state) : false;
    	}
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	if (!worldIn.isRemote)
        {
    		EnumFacing facing = state.getValue(FACING);
    		if(!canSustainBush(worldIn.getBlockState(pos.offset(facing)))) {
    			this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
    		}
        }
    }
    
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
    	return this.getDefaultState().withProperty(FACING, facing.getOpposite());
    }
}
