package com.minerarcana.floralchemy.block.flower;

import java.util.Random;

import javax.annotation.Nullable;

import com.teamacronymcoders.base.blocks.IHasBlockColor;
import com.teamacronymcoders.base.blocks.IHasItemBlock;
import com.teamacronymcoders.base.items.IHasItemColor;
import com.teamacronymcoders.base.items.itemblocks.ItemBlockGeneric;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrystalthorn extends BlockBush implements IHasBlockColor, IHasItemBlock {

	public static final int maxAge = 7;
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, maxAge);
	public static final PropertyBool BERRIES = PropertyBool.create("berries");
	private final ResourceLocation crystalName;
	private final int crystalMetadata;
	private ItemBlock itemBlock;
	
	public BlockCrystalthorn(ResourceLocation crystalName, int crystalMetadata) {
		super(Material.PLANTS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(BERRIES, false));
		this.setTranslationKey("crystalthorn_" + crystalName.getPath());
		this.setTickRandomly(true);
		this.crystalName = crystalName;
		this.crystalMetadata = crystalMetadata;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(state.getValue(AGE) == maxAge && state.getValue(BERRIES)) {
			ItemStack stack = new ItemStack(Item.getByNameOrId(crystalName.toString()), 1, crystalMetadata);
			if(!stack.isEmpty()) {
				EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getZ(), pos.getZ(), stack);
				worldIn.spawnEntity(item);
				playerIn.attackEntityFrom(DamageSource.CACTUS, 3F);
			}
		}
		return false;
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
    {
		if(random.nextBoolean()) {
			if(state.getValue(AGE) < maxAge) {
				worldIn.setBlockState(pos, state.withProperty(AGE, state.getValue(AGE)+1));
				return;
			}
			if(!state.getValue(BERRIES) && random.nextInt(10) == 0) {
				worldIn.setBlockState(pos, state.withProperty(BERRIES, true));
			}
		}
    }
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
		if(meta == maxAge + 1) {
			return this.getDefaultState().withProperty(BERRIES, true).withProperty(AGE, maxAge);
		}
        return this.getDefaultState().withProperty(AGE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(BERRIES) ? state.getValue(AGE) : state.getValue(AGE) + 1;
    }
	
//    double[] AABB = { 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1 };
//    
//	@Override
//	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
//    {
//		double shrink = 1 - AABB[state.getValue(AGE)];
//        return FULL_BLOCK_AABB.shrink(shrink).offset(0, 0.05 * state.getValue(AGE), 0);
//    }
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE, BERRIES});
    }
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		player.attackEntityFrom(DamageSource.CACTUS, 3F);
		super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

	@SideOnly(Side.CLIENT)
	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
		Item crystalItem = Item.getByNameOrId(crystalName.toString());
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
