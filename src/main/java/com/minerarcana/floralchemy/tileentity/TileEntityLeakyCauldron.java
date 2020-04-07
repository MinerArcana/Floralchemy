package com.minerarcana.floralchemy.tileentity;

import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityLeakyCauldron extends TileEntity implements ITickableTileEntity {
    public FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 4);
    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

    public TileEntityLeakyCauldron() {
        super(FloralchemyBlocks.LEAKY_CAULDRON.getTileEntityType());
    }

    @Override
    public void read(CompoundNBT data) {
        tank.readFromNBT(data);
    }

    @Override
    public CompoundNBT write(CompoundNBT data) {
        return tank.writeToNBT(data);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return holder.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void tick() {
        if(!this.getWorld().isRemote) {
            BlockPos down = this.getPos().down();
            if (tank.getFluidAmount() > 0) {
                //Leak to world
                /*if (tank.getFluidAmount() == 1000 && tank.getFluid().getFluid().getFluid().get != null && this.getWorld().isAirBlock(down)) {
                    worldIn.setBlockState(down, tile.tank.getFluid().getFluid().getBlock().getDefaultState());
                }
                //Leak to fluid handlers
                else if (this.getWorld().getTileEntity(down) != null && worldIn.getTileEntity(down).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP)) {
                    IBlockState old = worldIn.getBlockState(down);
                    IFluidHandler otherTank = worldIn.getTileEntity(down)
                            .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP);
                    FluidStack test = FluidUtil.tryFluidTransfer(otherTank, tile.tank, 500, false);
                    if (test != null && test.amount == 500) {
                        FluidUtil.tryFluidTransfer(otherTank, tile.tank, 100, true);
                        worldIn.markAndNotifyBlock(down, worldIn.getChunk(down), old,
                                worldIn.getBlockState(down), 3);
                        worldIn.getTileEntity(down).markDirty();
                        tile.markDirty();
                    }
                }*/
            }
        }
    }
}
