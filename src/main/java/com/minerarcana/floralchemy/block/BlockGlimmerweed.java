package com.minerarcana.floralchemy.block;

import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockGlimmerweed extends BlockBaseBush {

	//Only need one vertical state
	public static final DirectionProperty FACING = BlockStateProperties.FACING_EXCEPT_UP;
	
	protected static final VoxelShape REED_AABB = Block.makeCuboidShape(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);
	protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 0.0625D, 1.0D, 1.0D);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(0.9375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D);
	
    public BlockGlimmerweed() {
        super(Properties.from(Blocks.DEAD_BUSH).tickRandomly().lightValue(7), Fluids.WATER);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch(state.get(FACING)) {
        	case SOUTH: return SOUTH_AABB;
        	case NORTH: return NORTH_AABB;
        	case WEST: return WEST_AABB;
        	case EAST: return EAST_AABB;
			default: return REED_AABB; 
        }
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.getBlock() == Blocks.DIRT ||
                state.getBlock() == Blocks.GRASS ||
                state.getBlock() == Blocks.STONE ||
                state.getBlock() == this ||
                state.getBlock() == FloralchemyBlocks.FLOODED_SOIL.getBlock();
    }
    
    /*@Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
    {
    	BlockState state = worldIn.getBlockState(pos.offset(side.getOpposite()));
    	if(side.getAxis().isVertical()) {
    		return worldIn.getBlockState(pos).canSustainPlant(worldIn, pos, side, this);
    	}
    	//Prevent sideways chaining of glimmerweed whilst still allowing vertical stacking
    	else {
    		return worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock() != this && worldIn.getBlockState(pos).canSustainPlant(worldIn, pos, side, this);
    	}
    }*/
    
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
    	if (!worldIn.isRemote)
        {
    		Direction facing = state.get(FACING);
    		if(!worldIn.getBlockState(pos).canSustainPlant(worldIn, pos, facing, this)) {
    			replaceBlock(state, Blocks.AIR.getDefaultState(), worldIn, pos, 3);
    		}
        }
    }

    @Override
    public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, net.minecraft.entity.LivingEntity entity) {
        return true;
    }
}
