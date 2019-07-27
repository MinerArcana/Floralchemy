package com.minerarcana.floralchemy.client;

import com.minerarcana.floralchemy.tileentity.TileEntityFloodedSoil;
import com.teamacronymcoders.base.util.RenderingUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityFloodedSoilRenderer extends TileEntitySpecialRenderer<TileEntityFloodedSoil> {

    protected static Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void render(TileEntityFloodedSoil tile, double x, double y, double z, float partialTicks, int destroyStage,
            float alpha) {
        FluidTank tank = tile.tank;
        FluidStack liquid = tank.getFluid();

        if(liquid != null) {
            // TODO Render fluid with increasing transparency at lesser amounts
            // float height = ((float) liquid.amount) / (float) tank.getCapacity();
            float d = RenderingUtils.FLUID_OFFSET;
            RenderingUtils.renderFluidCuboid(liquid, tile.getPos(), x, y, z, d, d, d, 1d - d, 1d - d, 1d - d);
        }
    }
}