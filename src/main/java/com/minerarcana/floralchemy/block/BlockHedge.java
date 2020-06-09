package com.minerarcana.floralchemy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

//TODO Hedge should know whether it was player placed and if so not drop flora plants
public class BlockHedge extends WallBlock {

    private final boolean isThorny;
    private final String type;

    public BlockHedge(String type, boolean isThorns) {
        super(Block.Properties.from(Blocks.OAK_LEAVES).tickRandomly());
        this.isThorny = isThorns;
        this.type = type;
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if(isThorny) {
            entityIn.attackEntityFrom(DamageSource.CACTUS, 3.0F);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if(!worldIn.isRemote) {
            boolean hasNeighbour = false;
            for(Direction facing : Direction.values()) {
                BlockPos adjacent = pos.offset(facing);
                if(worldIn.getBlockState(adjacent).getBlock() == this
                        || worldIn.getBlockState(adjacent).getMaterial() == Material.EARTH) {
                    hasNeighbour = true;
                }
            }
            if(!hasNeighbour) {
                //TODO
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        if(!worldIn.isRemote) {
            /*if(ItemStackUtils.doItemsMatch(playerIn.getHeldItem(hand), Items.DYE)
                    && playerIn.getHeldItem(hand).getItemDamage() == EnumDyeColor.WHITE.getDyeDamage()) {
                BlockPos toGrow = pos.offset(facing);
                if(worldIn.isAirBlock(toGrow)) {
                    worldIn.setBlockState(toGrow, state);
                    if(!playerIn.capabilities.isCreativeMode) {
                        playerIn.getHeldItem(hand).shrink(1);
                    }
                    worldIn.playEvent(2005, toGrow, 0);
                    return true;
                }
            }*/
        }
        return ActionResultType.PASS;
    }

    public String getType() {
        return type;
    }
}
