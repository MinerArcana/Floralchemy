package com.minerarcana.floralchemy.block;

import java.util.Optional;
import java.util.Random;

import com.minerarcana.floralchemy.tileentity.TileEntityFloodedSoil;
import com.teamacronymcoders.base.blocks.BlockTEBase;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFloodedSoil extends BlockTEBase<TileEntityFloodedSoil> {

	public static final int FLUID_TRANSFER_AMOUNT_MB = 100;
	public static final int CULTIVATION_FLUID_USE_MB = 100;
	
    public BlockFloodedSoil() {
        super(Material.GROUND, "flooded_soil");
        setTickRandomly(true);
    }
    
    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
    {
    	IBlockState plant = plantable.getPlant(world, pos.offset(direction));
    	if(plant.getBlock() instanceof BlockBaseBush) {
			TileEntity tile = world.getTileEntity(pos);
        	if(tile instanceof TileEntityFloodedSoil) {
        		TileEntityFloodedSoil soil = (TileEntityFloodedSoil)tile;
        		IFluidTank tank = (IFluidTank)soil.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
        		if(tank != null && tank.getFluidAmount() > 0) {
        			if(((BlockBaseBush)plant.getBlock()).cultivatingFluidNames.contains(FluidRegistry.getFluidName(tank.getFluid().getFluid()))) {
        				return true;
        			}
        		}
        	}
    	}
    	return super.canSustainPlant(state, world, pos, direction, plantable);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if(!worldIn.isRemote) {
            getTileEntity(worldIn, pos).ifPresent(tile -> {
                if(tile.tank.getFluidAmount() >= FLUID_TRANSFER_AMOUNT_MB * 2) {
                    for(EnumFacing facing : EnumFacing.VALUES) {
                        if(!EnumFacing.UP.equals(facing)) {
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
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
            EnumFacing side) {
        return blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false
                : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.getHeldItem(hand).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            Optional<TileEntityFloodedSoil> te = getTileEntity(worldIn, pos);
            if(te.isPresent()) {
                if(FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing)) {
                    te.get().sendBlockUpdate();
                    return true;
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public Class<TileEntityFloodedSoil> getTileEntityClass() {
        return TileEntityFloodedSoil.class;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState blockState) {
        return new TileEntityFloodedSoil();
    }
}
