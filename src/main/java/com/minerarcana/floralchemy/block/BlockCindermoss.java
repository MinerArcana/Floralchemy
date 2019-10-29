package com.minerarcana.floralchemy.block;

import java.util.Random;

import com.minerarcana.floralchemy.FloraObjectHolder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCindermoss extends BlockBaseBush {
    
    public BlockCindermoss() {
        super("cindermoss");
        this.setTickRandomly(true);
    }
    
    @Override
    public boolean canSustainBush(IBlockState state)
    {
        return state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.SAND || state.getBlock() == Blocks.MAGMA || state.getBlock() == FloraObjectHolder.FLOODED_SOIL;
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        if(random.nextInt(10) == 0) {
            BlockPos testPos = pos.down().offset(EnumFacing.byHorizontalIndex(random.nextInt(3)));
            if(canSustainBush(worldIn.getBlockState(testPos))) {
                BlockPos placePos = testPos.up();
                if(worldIn.isAirBlock(placePos)) {
                    worldIn.setBlockState(placePos, FloraObjectHolder.CINDERMOSS.getDefaultState());
                }
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if(rand.nextBoolean()) {
            double d0 = (double)pos.getX() + rand.nextDouble();
            double d1 = (double)pos.getY() + rand.nextDouble() * 0.5D;
            double d2 = (double)pos.getZ() + rand.nextDouble();
            worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        entityIn.setFire(worldIn.rand.nextInt(5));
    }
}
