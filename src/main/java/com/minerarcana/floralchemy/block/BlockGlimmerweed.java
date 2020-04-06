package com.minerarcana.floralchemy.block;

import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockGlimmerweed extends BlockBaseBush {

	//Only need one vertical state
	//public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	protected static final AxisAlignedBB REED_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
	
    public BlockGlimmerweed() {
        super(Properties.from(Blocks.DEAD_BUSH).tickRandomly().lightValue(7), Fluids.WATER);
    }
    
    /*@Override
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
    }*/

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.getBlock() == Blocks.DIRT ||
                state.getBlock() == Blocks.GRASS ||
                state.getBlock() == Blocks.STONE ||
                state.getBlock() == this ||
                state.getBlock() == FloralchemyBlocks.FLOODED_SOIL.getBlock();
    }
    
    /*@Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side)
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
    		Direction facing = state.getValue(FACING);
    		if(!canSustainBush(worldIn.getBlockState(pos.offset(facing)))) {
    			this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
    		}
        }
    }*/
}
