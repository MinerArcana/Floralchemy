package com.minerarcana.floralchemy.botania;

import java.awt.Color;
import java.util.Optional;

import com.minerarcana.floralchemy.api.FloralchemyAPI;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.RadiusDescriptor.Square;
import vazkii.botania.api.subtile.SubTileGenerating;

public class SubTilePetroPetunia extends SubTileGenerating {
    public static final String NAME = "petro_petunia";

    private static final int START_BURN_EVENT = 0;
    private static final String TAG_BURN_TIME = "burn_time";
    private static final String TAG_POWER = "tick_power";
    private static final String TAG_COOL_DOWN = "cool_down";
    private int burnTime;
    private int powerPerTick;
    private int coolDown;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(coolDown > 0) {
            --coolDown;
        }

        if(burnTime > 0) {
            --burnTime;
        }

        if(getWorld().isRemote) {
            if(burnTime > 0 && supertile.getWorld().rand.nextInt(10) == 0) {
                Vec3d offset = getWorld().getBlockState(getPos()).getOffset(getWorld(), getPos()).add(0.4, 0.7, 0.4);
                supertile.getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                        supertile.getPos().getX() + offset.x + Math.random() * 0.2,
                        supertile.getPos().getY() + offset.y,
                        supertile.getPos().getZ() + offset.z + Math.random() * 0.2, 0.0D, 0.0D, 0.0D);
            }
        }
        else {
            if(linkedCollector != null) {
                if(burnTime <= 0 && redstoneSignal == 0 && coolDown <= 0) {
                    if(mana < getMaxMana()) {
                        int tries = 0;
                        boolean foundFluid = false;

                        while(tries < 9 && !foundFluid) {
                            BlockPos checkingPos = getPos().add(-1 + tries / 3, -1, -1 + tries % 3);
                            IFluidHandler fluidHandler = FluidUtil.getFluidHandler(getWorld(), checkingPos,
                                    EnumFacing.UP);
                            if(fluidHandler != null) {
                                FluidStack grabbedFluid = fluidHandler.drain(1000, false);
                                if(grabbedFluid != null && grabbedFluid.getFluid() != null
                                        && grabbedFluid.amount == 1000) {
                                    Optional<Tuple<Integer, Integer>> fuelInfo = FloralchemyAPI.getFluidFuelRegistry()
                                            .getFuelInfo(grabbedFluid.getFluid().getName());
                                    if(fuelInfo.isPresent()) {
                                        foundFluid = true;
                                        burnTime = fuelInfo.get().getFirst();
                                        powerPerTick = fuelInfo.get().getSecond();
                                        fluidHandler.drain(1000, true);
                                        sync();
                                    }
                                }
                            }
                            tries++;
                        }

                        if(!foundFluid) {
                            coolDown = 100;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int event, int param) {
        if(event == START_BURN_EVENT) {

            return true;
        }
        else {
            return super.receiveClientEvent(event, param);
        }
    }

    @Override
    public int getMaxMana() {
        return 9000;
    }

    @Override
    public int getValueForPassiveGeneration() {
        return powerPerTick * getDelayBetweenPassiveGeneration();
    }

    @Override
    public boolean canGeneratePassively() {
        return burnTime > 0;
    }

    @Override
    public int getDelayBetweenPassiveGeneration() {
        return 2;
    }

    @Override
    public boolean acceptsRedstone() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RadiusDescriptor getRadius() {
        return new Square(toBlockPos(), 1);
    }

    @Override
    public int getColor() {
        return Color.BLACK.getRGB();
    }

    @Override
    public void writeToPacketNBT(NBTTagCompound cmp) {
        super.writeToPacketNBT(cmp);

        cmp.setInteger(TAG_BURN_TIME, burnTime);
        cmp.setInteger(TAG_COOL_DOWN, coolDown);
        cmp.setInteger(TAG_POWER, powerPerTick);
    }

    @Override
    public void readFromPacketNBT(NBTTagCompound cmp) {
        super.readFromPacketNBT(cmp);

        burnTime = cmp.getInteger(TAG_BURN_TIME);
        coolDown = cmp.getInteger(TAG_COOL_DOWN);
        powerPerTick = cmp.getInteger(TAG_POWER);
    }

    @Override
    public LexiconEntry getEntry() {
        return LexiconPages.petroPetunia;
    }
}
