package com.minerarcana.floralchemy.tileentity;

import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityFloodedSoil extends TileEntity {
    public FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME);
    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

    public TileEntityFloodedSoil() {
        super(FloralchemyBlocks.FLOODED_SOIL.getTileEntityType());
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
}
