package com.minerarcana.floralchemy.item;

import java.util.List;

import javax.annotation.Nullable;

import com.minerarcana.floralchemy.block.BlockLeakyCauldron;
import com.teamacronymcoders.base.items.itemblocks.ItemBlockModel;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockLeakyCauldron extends ItemBlockModel<BlockLeakyCauldron> {
	public ItemBlockLeakyCauldron(BlockLeakyCauldron block) {
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
	    tooltip.add(TextFormatting.GOLD + new TextComponentTranslation("tile.floralchemy.leaky_cauldron.desc").getFormattedText());
	}
}