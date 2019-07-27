package com.minerarcana.floralchemy.block;

import java.util.Random;

import com.minerarcana.floralchemy.FloraObjectHolder;
import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.items.ItemBlockTinted;
import com.teamacronymcoders.base.blocks.BlockBase;
import com.teamacronymcoders.base.blocks.IHasBlockColor;
import com.teamacronymcoders.base.util.ItemStackUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHedge extends BlockBase implements IHasBlockColor {

    private final boolean isThorny;
    
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    public BlockHedge(String name, boolean isThorns) {
        super(Material.LEAVES, name);
        setDefaultState(blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false))
                .withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false))
                .withProperty(WEST, Boolean.valueOf(false)));
        setTickRandomly(true);
        setCreativeTab(CreativeTabs.DECORATIONS);
        setHardness(0.2F);
        this.isThorny = isThorns;
        this.setItemBlock(new ItemBlockTinted<BlockHedge>(this));
    }
    
    @Override
    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        if(world instanceof WorldServer) {
            WorldServer serverWorld = (WorldServer) world;
            String location = isThorny ? "block/thorny_hedge" : "block/hedge";
            LootTable table = serverWorld.getLootTableManager().getLootTableFromLocation(new ResourceLocation(Floralchemy.MOD_ID, location));
            LootContext ctx = new LootContext.Builder(serverWorld).withLuck(fortune).build();
            drops.addAll(table.generateLootForPools(serverWorld.rand, ctx));
        }
        else {
            super.getDrops(drops, world, pos, state, fortune);
        }
    }
    
    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if(isThorny) {
            entityIn.attackEntityFrom(DamageSource.CACTUS, 3.0F);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { NORTH, EAST, WEST, SOUTH });
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if(!worldIn.isRemote) {
            boolean hasNeighbour = false;
            for(EnumFacing facing : EnumFacing.VALUES) {
                BlockPos adjacent = pos.offset(facing);
                if(worldIn.getBlockState(adjacent).getBlock() == this
                        || worldIn.getBlockState(adjacent).getMaterial() == Material.GRASS || worldIn.getBlockState(adjacent).getMaterial() == Material.GROUND) {
                    hasNeighbour = true;
                }
            }
            if(!hasNeighbour) {
                worldIn.setBlockToAir(pos);
            }
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return FULL_BLOCK_AABB.shrink(0.05F);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(NORTH, canThisConnectTo(worldIn, pos, EnumFacing.NORTH))
                .withProperty(EAST, canThisConnectTo(worldIn, pos, EnumFacing.EAST))
                .withProperty(SOUTH, canThisConnectTo(worldIn, pos, EnumFacing.SOUTH))
                .withProperty(WEST, canThisConnectTo(worldIn, pos, EnumFacing.WEST));
    }

    private boolean canThisConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        BlockPos other = pos.offset(facing);
        Block block = world.getBlockState(other).getBlock();
        return block.canBeConnectedTo(world, other, facing.getOpposite())
                || canConnectTo(world, other, facing.getOpposite());
    }

    public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(worldIn, pos, facing);
        Block block = iblockstate.getBlock();
        boolean flag = blockfaceshape == BlockFaceShape.MIDDLE_POLE
                && (iblockstate.getMaterial() == material || block instanceof BlockFenceGate);
        return !isExcepBlockForAttachWithPiston(block) && blockfaceshape == BlockFaceShape.SOLID || flag;
    }

    public static boolean isExcepBlockForAttachWithPiston(Block block) {
        return Block.isExceptBlockForAttachWithPiston(block) || block == Blocks.BARRIER || block == Blocks.MELON_BLOCK
                || block == Blocks.PUMPKIN || block == Blocks.LIT_PUMPKIN;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
        return world != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(world, pos) : ColorizerFoliage.getFoliageColorBasic();
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(!worldIn.isRemote) {
            if(ItemStackUtils.doItemsMatch(playerIn.getHeldItem(hand), Items.DYE) && playerIn.getHeldItem(hand).getItemDamage() == EnumDyeColor.WHITE.getDyeDamage()) {
                BlockPos toGrow = pos.offset(facing);
                if(worldIn.isAirBlock(toGrow)) {
                    worldIn.setBlockState(toGrow, FloraObjectHolder.hedge.getDefaultState());
                    playerIn.getHeldItem(hand).shrink(1);
                    worldIn.playEvent(2005, toGrow, 0);
                    return true;
                }
            }
        }
        return false;
    }
}
