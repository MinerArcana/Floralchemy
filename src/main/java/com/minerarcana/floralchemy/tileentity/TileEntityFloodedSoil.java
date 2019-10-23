package com.minerarcana.floralchemy.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.teamacronymcoders.base.tileentities.TileEntityBase;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFloodedSoil extends TileEntityBase {
    public FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME);

    @Override
    protected void readFromDisk(NBTTagCompound data) {
        tank.readFromNBT(data);
    }

    @Override
    protected NBTTagCompound writeToDisk(NBTTagCompound data) {
        return tank.writeToNBT(data);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
        }
        return super.getCapability(capability, facing);
    }

    // Handles sync on update
    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), -1, writeToDisk(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(net.minecraft.network.NetworkManager net,
            net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
        readFromDisk(pkt.getNbtCompound());
    }

    // Handles sync on world load
    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToDisk(super.getUpdateTag());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromDisk(tag);
    }
}
