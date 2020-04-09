package com.minerarcana.floralchemy.block;

import com.minerarcana.floralchemy.tileentity.TileEntityFloodedSoil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class BlockFloodedSoil extends TileBlock<TileEntityFloodedSoil> {

	public static final int FLUID_TRANSFER_AMOUNT_MB = 100;
	public static final int CULTIVATION_FLUID_USE_MB = 100;
	
    public BlockFloodedSoil() {
        super(Properties.from(Blocks.DIRT).tickRandomly(), TileEntityFloodedSoil::new);
    }
    
    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
    	BlockState plant = plantable.getPlant(world, pos.offset(facing));
    	if(plant.getBlock() instanceof BlockBaseBush) {
			TileEntity tile = world.getTileEntity(pos);
        	if(tile instanceof TileEntityFloodedSoil) {
        		TileEntityFloodedSoil soil = (TileEntityFloodedSoil)tile;
        		IFluidTank tank = (IFluidTank)soil.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP);
        		if(tank.getFluidAmount() > 0) {
        			if(((BlockBaseBush)plant.getBlock()).cultivatingFluids.contains(tank.getFluid().getFluid())) {
        				return true;
        			}
        		}
        	}
    	}
    	return super.canSustainPlant(state, world, pos, facing, plantable);
    }

    /*@Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if(!worldIn.isRemote) {
            getTileEntity(worldIn, pos).ifPresent(tile -> {
                if(tile.tank.getFluidAmount() >= FLUID_TRANSFER_AMOUNT_MB * 2) {
                    for(Direction facing : Direction.VALUES) {
                        if(!Direction.UP.equals(facing)) {
                            BlockPos adjacent = pos.offset(facing);
                            IBlockState old = worldIn.getBlockState(adjacent);
                            if(old.getBlock() == this) {
                                IFluidHandler otherTank = worldIn.getTileEntity(adjacent)
                                        .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
                                FluidStack test = FluidUtil.tryFluidTransfer(otherTank, tile.tank, FLUID_TRANSFER_AMOUNT_MB, false);
                                if(test != null && test.amount == FLUID_TRANSFER_AMOUNT_MB) {
                                    FluidUtil.tryFluidTransfer(otherTank, tile.tank, FLUID_TRANSFER_AMOUNT_MB, true);
                                    worldIn.markAndNotifyBlock(adjacent, worldIn.getChunk(adjacent), old,
                                            worldIn.getBlockState(adjacent), 3);
                                    worldIn.getTileEntity(adjacent).markDirty();
                                    tile.markDirty();
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        }
    }*/
}
