package com.minerarcana.floralchemy.block.flower;

import javax.annotation.Nullable;

import com.teamacronymcoders.base.blocks.IHasBlockColor;
import com.teamacronymcoders.base.blocks.IHasItemBlock;
import com.teamacronymcoders.base.items.IHasItemColor;
import com.teamacronymcoders.base.items.itemblocks.ItemBlockGeneric;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCrystalthorn extends BlockBush implements IHasBlockColor, IHasItemBlock {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
	private final ResourceLocation crystalName;
	private final int crystalMetadata;
	private ItemBlock itemBlock;
	
	public BlockCrystalthorn(ResourceLocation crystalName, int crystalMetadata) {
		super(Material.PLANTS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
		this.setTranslationKey("crystalthorn_" + crystalName.getPath());
		this.crystalName = crystalName;
		this.crystalMetadata = crystalMetadata;
	}
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(AGE);
    }
	
    double[] AABB = { 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1 };
    
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_BLOCK_AABB.shrink(1 - AABB[state.getValue(AGE)]);
    }
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE});
    }
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
		ItemStack stack = new ItemStack(Item.getByNameOrId(crystalName.toString()), 1 + fortune, crystalMetadata);
		drops.add(stack);
    }
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
		player.attackEntityFrom(DamageSource.CACTUS, 3F);
		super.harvestBlock(worldIn, player, pos, state, te, stack);
    }
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
		return false;
    }

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
		Item crystalItem = Item.getByNameOrId(crystalName.toString());
		//TODO Make server-safe
		return Minecraft.getMinecraft().getItemColors().colorMultiplier(new ItemStack(crystalItem), 0);
	}

	@Override
	public Block getBlock() {
		return this;
	}

	@Override
	public ItemBlock getItemBlock() {
		return itemBlock == null ? new ItemBlockGeneric<BlockCrystalthorn>(this) : itemBlock;
	}
	
	public static class ItemBlockCrystalthorn extends ItemBlockGeneric<BlockCrystalthorn> implements IHasItemColor {

		public ItemBlockCrystalthorn(BlockCrystalthorn block) {
			super(block);
		}

		@Override
		public int getColorFromItemstack(ItemStack stack, int tintIndex) {
			if(tintIndex == 0) {
				return this.getActualBlock().colorMultiplier(null, null, null, tintIndex);
			}
			return 0;
		}
		
	}
}
