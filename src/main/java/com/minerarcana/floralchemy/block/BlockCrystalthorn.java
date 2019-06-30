package com.minerarcana.floralchemy.block;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.teamacronymcoders.base.blocks.IHasBlockColor;
import com.teamacronymcoders.base.blocks.IHasItemBlock;
import com.teamacronymcoders.base.client.ClientHelper;
import com.teamacronymcoders.base.items.itemblocks.ItemBlockGeneric;
import com.teamacronymcoders.base.util.ColourHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrystalthorn extends BlockBush implements IHasBlockColor, IHasItemBlock {

	public static final int maxAge = 7;
	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, maxAge);
	public static final PropertyBool BERRIES = PropertyBool.create("berries");
	private static final AxisAlignedBB[] AABB = new AxisAlignedBB[] { new AxisAlignedBB(0.3, 0, 0.3, 0.7, 0.4, 0.7),
			new AxisAlignedBB(0.2, 0, 0.2, 0.8, 0.5, 0.8), new AxisAlignedBB(0.2, 0, 0.2, 0.8, 0.6, 0.8),
			new AxisAlignedBB(0.2, 0, 0.2, 0.8, 0.6, 0.8), new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.6, 0.9),
			new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.6, 0.9), new AxisAlignedBB(0, 0, 0, 1, 1, 1),
			new AxisAlignedBB(0, 0, 0, 1, 1, 1) };
	private Tuple<ResourceLocation, Integer> crystal;
	private ItemBlock itemBlock;
	private ItemStack cachedCrystalStack = ItemStack.EMPTY;

	public BlockCrystalthorn(Tuple<ResourceLocation, Integer> entry) {
		super(Material.PLANTS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0).withProperty(BERRIES, false));
		this.setTranslationKey("crystalthorn");
		this.setTickRandomly(true);
		this.crystal = entry;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(this.getCrystalStack().getDisplayName());
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (state.getValue(AGE) == maxAge && state.getValue(BERRIES)) {
			ItemStack stack = this.getCrystalStack();
			if (!stack.isEmpty()) {
				if (playerIn.addItemStackToInventory(stack)) {
					playerIn.attackEntityFrom(DamageSource.CACTUS, 3F);
					worldIn.setBlockState(pos, state.withProperty(BERRIES, false));
				}
			}
		}
		return false;
	}

	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
		if (random.nextBoolean()) {
			if (state.getValue(AGE) < maxAge) {
				worldIn.setBlockState(pos, state.withProperty(AGE, state.getValue(AGE) + 1));
				return;
			}
			if (!state.getValue(BERRIES) && random.nextInt(10) == 0) {
				worldIn.setBlockState(pos, state.withProperty(BERRIES, true));
			}
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta == maxAge + 1) {
			return this.getDefaultState().withProperty(BERRIES, true).withProperty(AGE, maxAge);
		}
		return this.getDefaultState().withProperty(AGE, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BERRIES) ? state.getValue(AGE) : state.getValue(AGE) + 1;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB[state.getValue(AGE)];
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { AGE, BERRIES });
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state,
			@Nullable TileEntity te, ItemStack stack) {
		player.attackEntityFrom(DamageSource.CACTUS, 3F);
		super.harvestBlock(worldIn, player, pos, state, te, stack);
	}
	
	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.attackEntityFrom(DamageSource.CACTUS, 1F);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
		ItemStack cStack = this.getCrystalStack();
		int tintColor = Minecraft.getMinecraft().getItemColors().colorMultiplier(
				cStack, 0);
		if (tintColor == -1) {
			//TODO This is very failure prone, need a better way. 
			IResource resource = ClientHelper.getResource(new ResourceLocation(crystal.getFirst().getNamespace(), "textures/items/" + crystal.getFirst().getPath() + ".png"));
			if (resource != null) {
				InputStream stream = resource.getInputStream();
				tintColor = ColourHelper.getColour(stream);
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return tintColor;
	}

	@Override
	public Block getBlock() {
		return this;
	}

	@Override
	public ItemBlock getItemBlock() {
		return itemBlock == null ? new ItemBlockGeneric<BlockCrystalthorn>(this) : itemBlock;
	}

	public ItemStack getCrystalStack() {
		if(cachedCrystalStack.isEmpty()) {
			cachedCrystalStack = new ItemStack(ForgeRegistries.ITEMS.getValue(crystal.getFirst()), 1, crystal.getSecond());
		}
		return cachedCrystalStack;
	}
}
