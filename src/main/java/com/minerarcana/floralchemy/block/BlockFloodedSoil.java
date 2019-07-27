package com.minerarcana.floralchemy.block;

import java.util.Optional;

import com.minerarcana.floralchemy.tileentity.TileEntityFloodedSoil;
import com.teamacronymcoders.base.blocks.BlockTEBase;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFloodedSoil extends BlockTEBase<TileEntityFloodedSoil> {

    public BlockFloodedSoil() {
        super(Material.GROUND, "flooded_soil");
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
            EnumFacing side) {
        return blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false
                : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.getHeldItem(hand).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            Optional<TileEntityFloodedSoil> te = getTileEntity(worldIn, pos);
            if(te.isPresent()) {
                if(FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing)) {
                    te.get().sendBlockUpdate();
                    return true;
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public Class<TileEntityFloodedSoil> getTileEntityClass() {
        return TileEntityFloodedSoil.class;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState blockState) {
        return new TileEntityFloodedSoil();
    }
}
