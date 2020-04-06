package com.minerarcana.floralchemy.block;

import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import com.minerarcana.floralchemy.tileentity.TileEntityFloodedSoil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockBaseBush extends BushBlock {
    public List<Fluid> cultivatingFluids = new ArrayList<>();

    public BlockBaseBush() {
        super(Properties.from(Blocks.DEAD_BUSH).tickRandomly());
    }
    
    public BlockBaseBush(Fluid... cultivatingFluidNames) {
    	this();
    	for(Fluid fluid : cultivatingFluidNames) {
    		this.cultivatingFluids.add(fluid);
    	}
    }

    public BlockBaseBush(Properties properties, Fluid... cultivatingFluids) {
        super(properties);
        for(Fluid fluid : this.cultivatingFluids) {
            this.cultivatingFluids.add(fluid);
        }
    }
    
    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if(random.nextInt(10) == 0) {
            BlockPos testPos = pos.down().offset(Direction.byHorizontalIndex(random.nextInt(3)));
            if(worldIn.isAirBlock(testPos.up()) && worldIn.getBlockState(testPos).getBlock() == FloralchemyBlocks.FLOODED_SOIL.getBlock()) {
            	TileEntity tile = worldIn.getTileEntity(pos.down()); //Use the flooded soil we're spreading *from* not to. 
            	if(tile instanceof TileEntityFloodedSoil) {
            		TileEntityFloodedSoil soil = (TileEntityFloodedSoil)tile;
            		IFluidTank tank = (IFluidTank)soil.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP);
                    if(tank.getFluidAmount() >= BlockFloodedSoil.CULTIVATION_FLUID_USE_MB && cultivatingFluids.contains(tank.getFluid().getFluid())) {
                        tank.drain(BlockFloodedSoil.CULTIVATION_FLUID_USE_MB, IFluidHandler.FluidAction.EXECUTE);
                        worldIn.setBlockState(testPos.up(), this.getDefaultState());
                    }
                }
            }
        }
    }

    @Override
    public Block getBlock() {
        return this;
    }
}
