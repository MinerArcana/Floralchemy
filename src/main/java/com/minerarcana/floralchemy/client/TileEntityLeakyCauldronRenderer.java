package com.minerarcana.floralchemy.client;

import com.minerarcana.floralchemy.RenderUtil;
import com.minerarcana.floralchemy.tileentity.TileEntityLeakyCauldron;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileEntityLeakyCauldronRenderer extends TileEntityRenderer<TileEntityLeakyCauldron> {

    protected static Minecraft mc = Minecraft.getInstance();

    public TileEntityLeakyCauldronRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityLeakyCauldron tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        FluidTank tank = tileEntityIn.tank;
        FluidStack liquid = tank.getFluid();

        if(!FluidStack.EMPTY.equals(liquid)) {
            float height = ((float) liquid.getAmount()) / (float) tank.getCapacity();
            float d = RenderUtil.FLUID_OFFSET;
            RenderUtil.renderFluidCuboid(matrixStackIn, bufferIn, liquid, tileEntityIn.getPos(), d, d, d,
                    d, d + 0.3f, d, 1f - d, height - d, 1f - d);
        }
    }
}