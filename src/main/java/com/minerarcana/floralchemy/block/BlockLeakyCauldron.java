package com.minerarcana.floralchemy.block;

import com.minerarcana.floralchemy.tileentity.TileEntityLeakyCauldron;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class BlockLeakyCauldron extends TileBlock<TileEntityLeakyCauldron> {

    public BlockLeakyCauldron() {
        super(Block.Properties.from(Blocks.CAULDRON), TileEntityLeakyCauldron::new);
    }

}
