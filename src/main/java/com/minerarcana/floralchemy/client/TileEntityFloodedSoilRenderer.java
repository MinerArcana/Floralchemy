package com.minerarcana.floralchemy.client;

import com.minerarcana.floralchemy.tileentity.TileEntityFloodedSoil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileEntityFloodedSoilRenderer extends TileEntityRenderer<TileEntityFloodedSoil> {

    protected static Minecraft mc = Minecraft.getInstance();

    public TileEntityFloodedSoilRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityFloodedSoil tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        FluidTank tank = tileEntityIn.tank;
        FluidStack liquid = tank.getFluid();

        if(liquid != null) {
            // TODO Render fluid with increasing transparency at lesser amounts
            // float height = ((float) liquid.amount) / (float) tank.getCapacity();
            //float d = RenderingUtils.FLUID_OFFSET;
            //RenderingUtils.renderFluidCuboid(liquid, tile.getPos(), x, y, z, d, d, d, 1d - d, 1d - d, 1d - d);
        }
    }
}