package com.minerarcana.floralchemy.tileentity;

import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityLeakyCauldron extends TileEntity implements ITickableTileEntity {
    public static final int FLUID_TRANSFER_AMOUNT_MB = 100;
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
                if (tank.getFluidAmount() >= 1000 && this.getWorld().isAirBlock(down)) {
                    this.getWorld().setBlockState(down, tank.getFluid().getFluid().getDefaultState().getBlockState());
                }
                //Leak to fluid handlers
                else {
                    if (tank.getFluidAmount() >= FLUID_TRANSFER_AMOUNT_MB && this.getWorld().getTileEntity(down) != null) {
                        this.getWorld().getTileEntity(down).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP).ifPresent(cap -> {
                            FluidStack test = FluidUtil.tryFluidTransfer(cap, tank, FLUID_TRANSFER_AMOUNT_MB, false);
                            if (test.getAmount() == FLUID_TRANSFER_AMOUNT_MB) {
                                FluidUtil.tryFluidTransfer(cap, tank, FLUID_TRANSFER_AMOUNT_MB, true);
                                //this.getWorld().notifyBlockUpdate(this.getPos(), );
                                this.getWorld().getTileEntity(down).markDirty();
                                this.markDirty();
                            }
                        });
                    }
                }
            }
        }
    }
}
