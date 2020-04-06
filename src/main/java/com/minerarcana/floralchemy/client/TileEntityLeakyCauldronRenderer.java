package com.minerarcana.floralchemy.client;

import com.minerarcana.floralchemy.tileentity.TileEntityLeakyCauldron;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class TileEntityLeakyCauldronRenderer extends TileEntityRenderer<TileEntityLeakyCauldron> {

    protected static Minecraft mc = Minecraft.getInstance();

    public TileEntityLeakyCauldronRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityLeakyCauldron tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        /*
        FluidTank tank = tile.tank;
        FluidStack liquid = tank.getFluid();

        if(liquid != null) {
            float height = ((float) liquid.amount) / (float) tank.getCapacity();
            float d = RenderingUtils.FLUID_OFFSET;
            RenderingUtils.renderFluidCuboid(liquid, tile.getPos(), x, y, z, d, d + 0.3d, d, 1d - d, height - d, 1d - d);
        }
         */
    }
}