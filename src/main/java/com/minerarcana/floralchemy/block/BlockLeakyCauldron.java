package com.minerarcana.floralchemy.block;

import java.util.*;

import javax.annotation.Nullable;

import com.minerarcana.floralchemy.tileentity.TileEntityLeakyCauldron;
import com.teamacronymcoders.base.blocks.BlockTEBase;
import com.teamacronymcoders.base.items.itemblocks.ItemBlockModel;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLeakyCauldron extends BlockTEBase<TileEntityLeakyCauldron> {

    public BlockLeakyCauldron() {
        super(Material.IRON, "leaky_cauldron");
        this.setTickRandomly(true);
        this.setItemBlock(new ItemBlockModel<BlockLeakyCauldron>(this) {
            @Override
            @SideOnly(Side.CLIENT)
            public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
            {
                tooltip.add(TextFormatting.GOLD + new TextComponentTranslation("tile.floralchemy.leaky_cauldron.desc").getFormattedText());
            }
        });
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if(!worldIn.isRemote) {
            BlockPos down = pos.down();
            getTileEntity(worldIn, pos).ifPresent(tile -> {
                if(tile.tank.getFluidAmount() > 0) {
                    //Leak to world
                    if(tile.tank.getFluidAmount() == 1000 && tile.tank.getFluid().getFluid().getBlock() != null && worldIn.isAirBlock(down)) {
                        worldIn.setBlockState(down, tile.tank.getFluid().getFluid().getBlock().getDefaultState());
                    }
                    //Leak to fluid handlers
                    else if(worldIn.getTileEntity(down) != null && worldIn.getTileEntity(down).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP)) {
                        IBlockState old = worldIn.getBlockState(down);
                            IFluidHandler otherTank = worldIn.getTileEntity(down)
                                    .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
                            FluidStack test = FluidUtil.tryFluidTransfer(otherTank, tile.tank, 500, false);
                            if(test != null && test.amount == 500) {
                                FluidUtil.tryFluidTransfer(otherTank, tile.tank, 100, true);
                                worldIn.markAndNotifyBlock(down, worldIn.getChunk(down), old,
                                        worldIn.getBlockState(down), 3);
                                worldIn.getTileEntity(down).markDirty();
                                tile.markDirty();
                            }
                    }
                }
            });
        }
    }
    
    //TODO Move this handling to a central static method
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.getHeldItem(hand).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            Optional<TileEntityLeakyCauldron> te = getTileEntity(worldIn, pos);
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
    public Class<TileEntityLeakyCauldron> getTileEntityClass() {
        return TileEntityLeakyCauldron.class;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState blockState) {
        return new TileEntityLeakyCauldron();
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
        return true;
    }

}
