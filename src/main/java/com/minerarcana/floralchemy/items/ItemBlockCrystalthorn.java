package com.minerarcana.floralchemy.items;

import com.minerarcana.floralchemy.block.BlockCrystalthorn;
import com.teamacronymcoders.base.items.IHasItemColor;
import com.teamacronymcoders.base.items.itemblocks.ItemBlockGeneric;

import net.minecraft.item.ItemStack;

public class ItemBlockCrystalthorn extends ItemBlockGeneric<BlockCrystalthorn> implements IHasItemColor {

	public ItemBlockCrystalthorn(BlockCrystalthorn block) {
		super(block);
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		if (tintIndex == 0) {
			return this.getActualBlock().colorMultiplier(null, null, null, tintIndex);
		}
		return 0;
	}
}