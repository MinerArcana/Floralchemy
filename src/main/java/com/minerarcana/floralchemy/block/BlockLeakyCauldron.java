package com.minerarcana.floralchemy.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class BlockLeakyCauldron extends Block {

    public BlockLeakyCauldron() {
        super(Block.Properties.from(Blocks.CAULDRON));
    }

    /*@Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        if(player.getHeldItem(handIn).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
            Optional<TileEntityLeakyCauldron> te = getTileEntity(worldIn, pos);
            if(te.isPresent()) {
                if(FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing)) {
                    te.get().sendBlockUpdate();
                    return true;
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }*/
}
