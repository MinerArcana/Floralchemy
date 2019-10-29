package com.minerarcana.floralchemy.worldgen;

import java.util.Random;

import com.minerarcana.floralchemy.FloraObjectHolder;
import com.minerarcana.floralchemy.block.BlockCindermoss;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class FloralchemyWorldGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
            IChunkProvider chunkProvider) {
        switch(world.provider.getDimension()) {
            case -1:
                generateNether(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 0:
                break;
            case 1:
                break;
        }

    }

    private void generateNether(World world, Random random, int chunkX, int chunkZ) {
        if(random.nextInt(5) == 0) {
            this.generateCindermoss(world, random, new BlockPos(chunkX + 8, random.nextInt(40), chunkZ + 8));
        }
    }

    public boolean generateCindermoss(World worldIn, Random rand, BlockPos position) {
        while(worldIn.isAirBlock(position) && position.getY() > 0) {
            position = position.down();
        }

        for(int i = 0; i < 128; ++i) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4),
                    rand.nextInt(8) - rand.nextInt(8));
            if(worldIn.isAirBlock(blockpos) && ((BlockCindermoss) FloraObjectHolder.CINDERMOSS)
                    .canSustainBush(worldIn.getBlockState(blockpos.down()))) {
                worldIn.setBlockState(blockpos, FloraObjectHolder.CINDERMOSS.getDefaultState(), 2);
            }
        }

        return true;
    }
}
