package com.minerarcana.floralchemy.worldgen;

import java.util.Random;

import com.minerarcana.floralchemy.FloraObjectHolder;
import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.block.BlockBaseBush;
import com.minerarcana.floralchemy.block.BlockCindermoss;
import com.minerarcana.floralchemy.block.BlockGlimmerweed;

import net.minecraft.block.BlockDirt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
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
                generateSurface(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 1:
                break;
        }

    }

    private void generateSurface(World world, Random random, int x, int z) {
    	//Devilsnare gen on podzol in forests
        if(random.nextInt(15) == 0) {
            BlockPos position = new BlockPos(x + 8, random.nextInt(world.getActualHeight()), z + 8);
            if(BiomeDictionary.hasType(world.getBiome(position), BiomeDictionary.Type.FOREST)) {
                while(world.isAirBlock(position) && position.getY() > 0) {
                    position = position.down();
                }

                for(int i = 0; i < 3; ++i) {
                    BlockPos blockpos = position.add(random.nextInt(8) - random.nextInt(8),
                            random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
                    if(world.isAirBlock(blockpos) && BlockDirt.DirtType.PODZOL
                            .equals(world.getBlockState(blockpos.down()).getProperties().get(BlockDirt.VARIANT))) {
                        world.setBlockState(blockpos, FloraObjectHolder.DEVILSNARE.getDefaultState(), 2);
                    }
                }
            }
        }
        //Glimmerweed gen in caves in hills
        //if(random.nextInt(30) == 0) {
        	BlockPos pos = new BlockPos(x + 8, 0, z + 8);
            if(BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.HILLS)) {
            	//Limits the maximum generation height to ten block below the surface
            	pos = new BlockPos(pos.getX(), random.nextInt(world.getHeight(pos).getY() - 10), pos.getY());
            		if(world.isAirBlock(pos)) {
            			for(EnumFacing facing : EnumFacing.VALUES) {
            				if(((BlockGlimmerweed) FloraObjectHolder.GLIMMERWEED).canSustainBush(world.getBlockState(pos.offset(facing)))) {
            					Floralchemy.instance.getLogger().devInfo(pos.toString());
            					world.setBlockState(pos, FloraObjectHolder.GLIMMERWEED.getDefaultState().withProperty(BlockGlimmerweed.FACING, facing));
            					break;
            				}
            			}
            		}
           // }
        }
    }

    private void generateNether(World world, Random random, int x, int z) {
        if(random.nextInt(30) == 0) {
            this.generateBush(world, random, new BlockPos(x + 8, random.nextInt(40), z + 8),
                    (BlockCindermoss) FloraObjectHolder.CINDERMOSS);
        }
    }

    public <B extends BlockBaseBush> boolean generateBush(World world, Random random, BlockPos position, B bush) {
        while(world.isAirBlock(position) && position.getY() > 0) {
            position = position.down();
        }

        for(int i = 0; i < 7; ++i) {
            BlockPos blockpos = position.add(random.nextInt(8) - random.nextInt(8),
                    random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
            if(world.isAirBlock(blockpos) && ((B) bush).canSustainBush(world.getBlockState(blockpos.down()))) {
                world.setBlockState(blockpos, bush.getDefaultState(), 2);
            }
        }

        return true;
    }
}
