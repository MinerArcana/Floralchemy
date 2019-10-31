package com.minerarcana.floralchemy.block;

import com.minerarcana.floralchemy.FloraObjectHolder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDevilsnare extends BlockBaseBush {

    public BlockDevilsnare() {
        super("devilsnare");
    }
    
    @Override
    public boolean canSustainBush(IBlockState state)
    {
        return state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS || state.getBlock() == FloraObjectHolder.FLOODED_SOIL;
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        entityIn.motionX *= 0.2;
        entityIn.motionY *= 0.2;
        entityIn.motionZ *= 0.2;
        if(worldIn.getLight(pos) < 7) {
            entityIn.attackEntityFrom(DamageSource.MAGIC, 2F);
        }
    }
}
