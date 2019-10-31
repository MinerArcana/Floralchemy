package com.minerarcana.floralchemy.block;

import com.minerarcana.floralchemy.FloraObjectHolder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class BlockGlimmerweed extends BlockBaseBush {

    public BlockGlimmerweed() {
        super("glimmerweed");
        this.setLightLevel(0.7F);
    }

    @Override
    public boolean canSustainBush(IBlockState state)
    {
        return state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS || state.getBlock() == this || state.getBlock() == FloraObjectHolder.FLOODED_SOIL;
    }
}
