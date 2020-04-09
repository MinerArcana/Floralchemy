package com.minerarcana.floralchemy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

//Stolen with <3 from Tinker's Construct. 
public final class RenderUtil {

    private RenderUtil() {
    }

    public static float FLUID_OFFSET = 0.005f;

    protected static Minecraft mc = Minecraft.getInstance();

    /**
     * Renders a fluid block, call from TESR. x/y/z is the rendering offset.
     *
     * @param fluid Fluid to render
     * @param pos   BlockPos where the Block is rendered. Used for brightness.
     * @param x     Rendering offset. TESR x parameter.
     * @param y     Rendering offset. TESR x parameter.
     * @param z     Rendering offset. TESR x parameter.
     * @param w     Width. 1 = full X-Width
     * @param h     Height. 1 = full Y-Height
     * @param d     Depth. 1 = full Z-Depth
     */
    public static void renderFluidCuboid(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, FluidStack fluid, BlockPos pos, float x, float y, float z, float w, float h, float d) {
        float wd = (1f - w) / 2f;
        float hd = (1f - h) / 2f;
        float dd = (1f - d) / 2f;

        renderFluidCuboid(matrixStackIn, bufferIn, fluid, pos, x, y, z, wd, hd, dd, 1f - wd, 1f - hd, 1f - dd);
    }

    public static void renderFluidCuboid(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, FluidStack fluid, BlockPos pos, float x, float y, float z, float x1, float y1, float z1, float x2, float y2, float z2) {
        int color = fluid.getFluid().getAttributes().getColor(fluid);
        renderFluidCuboid(matrixStackIn, bufferIn, fluid, pos, x, y, z, x1, y1, z1, x2, y2, z2, color);
    }

    /** Renders block with offset x/y/z from x1/y1/z1 to x2/y2/z2 inside the block local coordinates, so from 0-1 */
    public static void renderFluidCuboid(MatrixStack ms, IRenderTypeBuffer bufferIn, FluidStack fluid, BlockPos pos, float x, float y, float z, float x1, float y1, float z1, float x2, float y2, float z2, int color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        //RenderType type = RenderTypeLookup.getRenderType(fluid.getFluid().getDefaultState());
        //IVertexBuilder renderer = bufferIn.getBuffer(type);
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        //mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        //RenderUtil.setColorRGBA(color);
        int brightness = Math.max(mc.world.getLight(pos), fluid.getFluid().getAttributes().getLuminosity());
        boolean upsideDown = fluid.getFluid().getAttributes().isGaseous(fluid);

        pre(ms, bufferIn, x, y, z);

        TextureAtlasSprite still = mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluid.getFluid().getAttributes().getStillTexture(mc.world, pos));
        TextureAtlasSprite flowing = mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluid.getFluid().getAttributes().getFlowingTexture(mc.world, pos));

        // x/y/z2 - x/y/z1 is because we need the width/height/depth
        putATexturedQuad(ms, renderer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.DOWN, color, brightness, false, upsideDown);
        putATexturedQuad(ms, renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.NORTH, color, brightness, true, upsideDown);
        putATexturedQuad(ms, renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.EAST, color, brightness, true, upsideDown);
        putATexturedQuad(ms, renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.SOUTH, color, brightness, true, upsideDown);
        putATexturedQuad(ms, renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.WEST, color, brightness, true, upsideDown);
        putATexturedQuad(ms, renderer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.UP, color, brightness, false, upsideDown);

        //tessellator.draw();

        post(ms, bufferIn);
    }

    public static void renderStackedFluidCuboid(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, FluidStack fluid, float px, float py, float pz, BlockPos pos,
                                                BlockPos from, BlockPos to, float ymin, float ymax) {
        renderStackedFluidCuboid(matrixStackIn, bufferIn, fluid, px, py, pz, pos, from, to, ymin, ymax, FLUID_OFFSET);
    }

    public static void renderStackedFluidCuboid(MatrixStack ms, IRenderTypeBuffer bufferIn, FluidStack fluid, float px, float py, float pz, BlockPos pos,
                                                BlockPos from, BlockPos to, float ymin, float ymax, float offsetToBlockEdge) {
        if(ymin >= ymax) {
            return;
        }
        //Tessellator tessellator = Tessellator.getInstance();
        RenderType type = RenderTypeLookup.getRenderType(fluid.getFluid().getDefaultState());
        IVertexBuilder renderer = bufferIn.getBuffer(type);

        //renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        //mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        int color = fluid.getFluid().getAttributes().getColor(fluid);
        int brightness = Math.max(mc.world.getLight(pos), fluid.getFluid().getAttributes().getLuminosity());

        pre(ms, bufferIn, px, py, pz);
        ms.translate(from.getX(), from.getY(), from.getZ());

        TextureAtlasSprite still = mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluid.getFluid().getAttributes().getStillTexture(mc.world, pos));
        TextureAtlasSprite flowing = mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluid.getFluid().getAttributes().getFlowingTexture(mc.world, pos));

        /*if(still == null) {
            still = mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).getMissingSprite();
        }
        if(flowing == null) {
            flowing = mc.getTextureMapBlocks().getMissingSprite();
        }*/
        boolean upsideDown = fluid.getFluid().getAttributes().isGaseous(fluid);

        int xd = to.getX() - from.getX();

        // the liquid can stretch over more blocks than the subtracted height is if ymin's decimal is bigger than ymax's decimal (causing UV over 1)
        // ignoring the decimals prevents this, as yd then equals exactly how many ints are between the two
        // for example, if ymax = 5.1 and ymin = 2.3, 2.8 (which rounds to 2), with the face array becoming 2.3, 3, 4, 5.1
        int yminInt = (int) ymin;
        int yd = (int) (ymax - yminInt);

        // prevents a rare case of rendering the top face multiple times if ymax is perfectly aligned with the block
        // for example, if ymax = 3 and ymin = 1, the values of the face array become 1, 2, 3, 3 as we then have middle ints
        if(ymax % 1f == 0) yd--;
        int zd = to.getZ() - from.getZ();

        float xmin = offsetToBlockEdge;
        float xmax = xd + 1f - offsetToBlockEdge;
        //float ymin = y1;
        //float ymax = y2;
        float zmin = offsetToBlockEdge;
        float zmax = zd + 1f - offsetToBlockEdge;

        float[] xs = new float[2 + xd];
        float[] ys = new float[2 + yd];
        float[] zs = new float[2 + zd];

        xs[0] = xmin;
        for(int i = 1; i <= xd; i++) xs[i] = i;
        xs[xd+1] = xmax;

        // we have to add the whole number for ymin or otherwise things render incorrectly if above the first block
        // example, heights of 2 and 5 would produce array of 2, 1, 2, 5
        ys[0] = ymin;
        for(int i = 1; i <= yd; i++) ys[i] = i + yminInt;
        ys[yd+1] = ymax;

        zs[0] = zmin;
        for(int i = 1; i <= zd; i++) zs[i] = i;
        zs[zd+1] = zmax;

        // render each side
        for(int y = 0; y <= yd; y++) {
            for(int z = 0; z <= zd; z++) {
                for(int x = 0; x <= xd; x++) {

                    float x1 = xs[x];
                    float x2 = xs[x + 1] - x1;
                    float y1 = ys[y];
                    float y2 = ys[y + 1] - y1;
                    float z1 = zs[z];
                    float z2 = zs[z + 1] - z1;

                    if(x == 0) putATexturedQuad(ms, renderer, flowing, x1, y1, z1, x2, y2, z2, Direction.WEST, color, brightness, true, upsideDown);
                    if(x == xd) putATexturedQuad(ms, renderer, flowing, x1, y1, z1, x2, y2, z2, Direction.EAST, color, brightness, true, upsideDown);
                    if(y == 0) putATexturedQuad(ms, renderer, still, x1, y1, z1, x2, y2, z2, Direction.DOWN, color, brightness, false, upsideDown);
                    if(y == yd) putATexturedQuad(ms, renderer, still, x1, y1, z1, x2, y2, z2, Direction.UP, color, brightness, false, upsideDown);
                    if(z == 0) putATexturedQuad(ms, renderer, flowing, x1, y1, z1, x2, y2, z2, Direction.NORTH, color, brightness, true, upsideDown);
                    if(z == zd) putATexturedQuad(ms, renderer, flowing, x1, y1, z1, x2, y2, z2, Direction.SOUTH, color, brightness, true, upsideDown);
                }
            }
        }

        //putTexturedQuad(renderer, still,   x1, y1, z1, x2-x1, y2-y1, z2-z1, Direction.DOWN, color, brightness);
        //putTexturedQuad(renderer, flowing, x1, y1, z1, x2-x1, y2-y1, z2-z1, Direction.NORTH, color, brightness);
        //putTexturedQuad(renderer, flowing, x1, y1, z1, x2-x1, y2-y1, z2-z1, Direction.EAST, color, brightness);
        //putTexturedQuad(renderer, flowing, x1, y1, z1, x2-x1, y2-y1, z2-z1, Direction.SOUTH, color, brightness);
        //putTexturedQuad(renderer, flowing, x1, y1, z1, x2-x1, y2-y1, z2-z1, Direction.WEST, color, brightness);
        //putTexturedQuad(renderer, still  , x1, y1, z1, x2-x1, y2-y1, z2-z1, Direction.UP, color, brightness);

        //tessellator.draw();

        post(ms, bufferIn);
    }

    public static void putTexturedCuboid(MatrixStack ms, IVertexBuilder renderer, ResourceLocation location, float x1, float y1, float z1, float x2, float y2, float z2,
                                         int color, int brightness) {
        TextureAtlasSprite sprite = mc.getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(location);
        putATexturedQuad(ms, renderer, sprite, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.DOWN, color, brightness, false);
        putATexturedQuad(ms, renderer, sprite, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.NORTH, color, brightness, false);
        putATexturedQuad(ms, renderer, sprite, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.EAST, color, brightness, false);
        putATexturedQuad(ms, renderer, sprite, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.SOUTH, color, brightness, false);
        putATexturedQuad(ms, renderer, sprite, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.WEST, color, brightness, false);
        putATexturedQuad(ms, renderer, sprite, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, Direction.UP, color, brightness, false);
    }

    public static void putATexturedQuad(MatrixStack ms, IVertexBuilder renderer, TextureAtlasSprite sprite, float x, float y, float z, float w, float h, float d, Direction face,
                                        int color, int brightness, boolean flowing) {
        putATexturedQuad(ms, renderer, sprite, x, y, z, w, h, d, face, color, brightness, flowing, false);
    }

    public static void putATexturedQuad(MatrixStack ms, IVertexBuilder renderer, TextureAtlasSprite sprite, float x, float y, float z, float w, float h, float d, Direction face,
                                        int color, int brightness, boolean flowing, boolean flipHorizontally) {
        int l1 = brightness >> 0x10 & 0xFFFF;
        int l2 = brightness & 0xFFFF;

        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;

        putATexturedQuad(ms, renderer, sprite, x, y, z, w, h, d, face, r, g, b, a, l1, l2, flowing, flipHorizontally);
    }

    public static void putATexturedQuad(MatrixStack ms, IVertexBuilder renderer, TextureAtlasSprite sprite, float x, float y, float z, float w, float h, float d, Direction face,
                                        int r, int g, int b, int a, int light1, int light2, boolean flowing) {
        putATexturedQuad(ms, renderer, sprite, x, y, z, w, h, d, face, r, g, b, a, light1, light2, flowing, false);
    }

    // x and x+w has to be within [0,1], same for y/h and z/d
    public static void putATexturedQuad(MatrixStack ms, IVertexBuilder renderer, TextureAtlasSprite sprite, float x, float y, float z, float w, float h, float d, Direction face,
                                        int r, int g, int b, int a, int light1, int light2, boolean flowing, boolean flipHorizontally) {
        // safety
        if(sprite == null) {
            return;
        }
        float minU;
        float maxU;
        float minV;
        float maxV;

        float size = 16f;
        if(flowing) {
            size = 8f;
        }

        float x2 = x + w;
        float y2 = y + h;
        float z2 = z + d;

        float xt1 = x % 1f;
        float xt2 = xt1 + w;
        while(xt2 > 1f) xt2 -= 1f;
        float yt1 = y % 1f;
        float yt2 = yt1 + h;
        while(yt2 > 1f) yt2 -= 1f;
        float zt1 = z % 1f;
        float zt2 = zt1 + d;
        while(zt2 > 1f) zt2 -= 1f;

        // flowing stuff should start from the bottom, not from the start
        if(flowing) {
            float tmp = 1f - yt1;
            yt1 = 1f - yt2;
            yt2 = tmp;
        }

        Matrix3f mat = ms.getLast().getNormal();

        switch(face) {
            case DOWN:
            case UP:
                minU = sprite.getInterpolatedU(xt1 * size);
                maxU = sprite.getInterpolatedU(xt2 * size);
                minV = sprite.getInterpolatedV(zt1 * size);
                maxV = sprite.getInterpolatedV(zt2 * size);
                break;
            case NORTH:
            case SOUTH:
                minU = sprite.getInterpolatedU(xt2 * size);
                maxU = sprite.getInterpolatedU(xt1 * size);
                minV = sprite.getInterpolatedV(yt1 * size);
                maxV = sprite.getInterpolatedV(yt2 * size);
                break;
            case WEST:
            case EAST:
                minU = sprite.getInterpolatedU(zt2 * size);
                maxU = sprite.getInterpolatedU(zt1 * size);
                minV = sprite.getInterpolatedV(yt1 * size);
                maxV = sprite.getInterpolatedV(yt2 * size);
                break;
            default:
                minU = sprite.getMinU();
                maxU = sprite.getMaxU();
                minV = sprite.getMinV();
                maxV = sprite.getMaxV();
        }

        if(flipHorizontally) {
            float tmp = minV;
            minV = maxV;
            maxV = tmp;
        }

        switch(face) {
            case DOWN:
                renderer.normal(mat, x, y, z).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x, y, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                break;
            case UP:
                renderer.normal(mat, x, y2, z).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x, y2, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y2, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y2, z).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case NORTH:
                renderer.normal(mat, x, y, z).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x, y2, z).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y2, z).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;
            case SOUTH:
                renderer.normal(mat, x, y, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case WEST:
                renderer.normal(mat, x, y, z).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x, y, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x, y2, z).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case EAST:
                renderer.normal(mat, x2, y, z).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y2, z).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;
        }
    }

    /**
     * Similar to putTexturedQuad, except its only for upwards quads and a rotation is specified
     */
    public static void putARotatedQuad(MatrixStack ms, BufferBuilder renderer, TextureAtlasSprite sprite, float x, float y, float z, float w, float d, Direction rotation,
                                       int color, int brightness, boolean flowing) {
        int l1 = brightness >> 0x10 & 0xFFFF;
        int l2 = brightness & 0xFFFF;

        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;

        putARotatedQuad(ms, renderer, sprite, x, y, z, w, d, rotation, r, g, b, a, l1, l2, flowing);
    }

    /**
     * Similar to putTexturedQuad, except its only for upwards quads and a rotation is specified
     */
    public static void putARotatedQuad(MatrixStack ms, BufferBuilder renderer, TextureAtlasSprite sprite, float x, float y, float z, float w, float d, Direction rotation,
                                       int r, int g, int b, int a, int light1, int light2, boolean flowing) {
        // safety
        if(sprite == null) {
            return;
        }

        float size = 16f;
        if(flowing) {
            size = 8f;
        }

        // coordinates for the sprite are super simple
        float x1 = x;
        float x2 = x + w;
        float z1 = z;
        float z2 = z + d;

        // textures
        float xt1 = x1%1f;
        float xt2 = xt1 + w;
        float zt1 = z1%1f;
        float zt2 = zt1 + d;

        // when rotating by 90 or 270 the dimensions switch, so switch the U and V before hand
        if(rotation.getAxis() == Direction.Axis.X) {
            float temp = xt1;
            xt1 = zt1;
            zt1 = temp;
            temp = xt2;
            xt2 = zt2;
            zt2 = temp;
        }

        // we want to start from the bottom for north or west textures as otherwise UV is backwards
        // we also want to start from the bottom for flowing fluids, and both should cancel
        if(flowing ^ (rotation == Direction.NORTH || rotation == Direction.WEST)) {
            float tmp = 1f - zt1;
            zt1 = 1f - zt2;
            zt2 = tmp;
        }

        float minU = sprite.getInterpolatedU(xt1 * size);
        float maxU = sprite.getInterpolatedU(xt2 * size);
        float minV = sprite.getInterpolatedV(zt1 * size);
        float maxV = sprite.getInterpolatedV(zt2 * size);

        Matrix3f mat = ms.getLast().getNormal();

        switch(rotation) {
            case NORTH:
                renderer.normal(mat, x1, y, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x1, y, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case WEST:
                renderer.normal(mat, x1, y, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x1, y, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z1).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;
            case SOUTH:
                renderer.normal(mat, x1, y, z1).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x1, y, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z1).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                break;
            case EAST:
                renderer.normal(mat, x1, y, z1).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x1, y, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                renderer.normal(mat, x2, y, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                break;
        }
    }

    public static void pre(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, float x, float y, float z) {
        matrixStackIn.push();

        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if(Minecraft.isAmbientOcclusionEnabled()) {
            GL11.glShadeModel(GL11.GL_SMOOTH);
        }
        else {
            GL11.glShadeModel(GL11.GL_FLAT);
        }
        matrixStackIn.translate(x, y, z);
    }

    public static void post(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn) {
        GlStateManager.disableBlend();
        matrixStackIn.pop();
        RenderHelper.enableStandardItemLighting();
    }

    public static void setColorRGB(int color) {
        setColorRGBA(color | 0xff000000);
    }

    public static void setColorRGBA(int color) {
        float a = alpha(color) / 255.0F;
        float r = red(color) / 255.0F;
        float g = green(color) / 255.0F;
        float b = blue(color) / 255.0F;

        GlStateManager.color4f(r, g, b, a);
    }

    public static void setBrightness(BufferBuilder renderer, int brightness) {
        //TODO
        //renderer.putBrightness4(brightness, brightness, brightness, brightness);
    }

    public static int compose(int r, int g, int b, int a) {
        int rgb = a;
        rgb = (rgb << 8) + r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;
        return rgb;
    }

    public static int alpha(int c) {
        return (c >> 24) & 0xFF;
    }

    public static int red(int c) {
        return (c >> 16) & 0xFF;
    }

    public static int green(int c) {
        return (c >> 8) & 0xFF;
    }

    public static int blue(int c) {
        return (c) & 0xFF;
    }
}

