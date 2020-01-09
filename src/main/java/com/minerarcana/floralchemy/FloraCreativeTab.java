package com.minerarcana.floralchemy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class FloraCreativeTab extends CreativeTabs {

	public FloraCreativeTab() {
		super(Floralchemy.MOD_ID);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(FloraObjectHolder.DEVILSNARE);
	}

}
