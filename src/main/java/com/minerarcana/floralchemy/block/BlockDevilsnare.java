package com.minerarcana.floralchemy.block;


import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockDevilsnare extends BlockBaseBush {

    public BlockDevilsnare() {
        super(Fluids.WATER);
    }
    
    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS;
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        entityIn.setMotionMultiplier(state, new Vec3d(0.2, 0.2, 0.2));
        if(worldIn.getLight(pos) < 7) {
            entityIn.attackEntityFrom(DamageSource.MAGIC, 2F);
        }
    }
}
