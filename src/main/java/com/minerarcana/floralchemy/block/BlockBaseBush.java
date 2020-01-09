package com.minerarcana.floralchemy.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.minerarcana.floralchemy.FloraObjectHolder;
import com.minerarcana.floralchemy.tileentity.TileEntityFloodedSoil;
import com.teamacronymcoders.base.IBaseMod;
import com.teamacronymcoders.base.IModAware;
import com.teamacronymcoders.base.blocks.IAmBlock;
import com.teamacronymcoders.base.blocks.IHasItemBlock;
import com.teamacronymcoders.base.client.models.IHasModel;
import com.teamacronymcoders.base.client.models.generator.IHasGeneratedModel;
import com.teamacronymcoders.base.client.models.generator.generatedmodel.GeneratedModel;
import com.teamacronymcoders.base.client.models.generator.generatedmodel.IGeneratedModel;
import com.teamacronymcoders.base.client.models.generator.generatedmodel.ModelType;
import com.teamacronymcoders.base.items.IHasOreDict;
import com.teamacronymcoders.base.items.IHasSubItems;
import com.teamacronymcoders.base.items.itemblocks.ItemBlockModel;
import com.teamacronymcoders.base.util.files.templates.TemplateFile;
import com.teamacronymcoders.base.util.files.templates.TemplateManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class BlockBaseBush extends BlockBush implements IHasItemBlock, IHasSubItems, IModAware, IAmBlock, IHasOreDict, IHasModel, IHasGeneratedModel {
    private IBaseMod<?> mod;
    private boolean creativeTabSet = false;
    private ItemBlock itemBlock;
    private String name;
    //public static final PropertyBool PASSIVE_SPREAD = PropertyBool.create("passive_spread");
    public List<String> cultivatingFluidNames = new ArrayList<>();

    public BlockBaseBush(String name) {
        super();
        this.setHardness(0.2F);
        this.name = name;
        this.setTranslationKey(name);
        this.setTickRandomly(true);
    }
    
    public BlockBaseBush(String name, String... cultivatingFluidNames) {
    	this(name);
    	for(String fluidName : cultivatingFluidNames) {
    		this.cultivatingFluidNames.add(fluidName);
    	}
    }
    
    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        if(random.nextInt(10) == 0) {
            BlockPos testPos = pos.down().offset(EnumFacing.byHorizontalIndex(random.nextInt(3)));
            if(worldIn.isAirBlock(testPos.up()) && worldIn.getBlockState(testPos).getBlock() == FloraObjectHolder.FLOODED_SOIL) {
            	TileEntity tile = worldIn.getTileEntity(pos.down()); //Use the flooded soil we're spreading *from* not to. 
            	if(tile instanceof TileEntityFloodedSoil) {
            		TileEntityFloodedSoil soil = (TileEntityFloodedSoil)tile;
            		IFluidTank tank = (IFluidTank)soil.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
            		if(tank != null) {
            			if(tank.getFluidAmount() >= BlockFloodedSoil.CULTIVATION_FLUID_USE_MB && cultivatingFluidNames.contains(FluidRegistry.getFluidName(tank.getFluid().getFluid()))) {
            				tank.drain(BlockFloodedSoil.CULTIVATION_FLUID_USE_MB, true);
            				worldIn.setBlockState(testPos.up(), this.getDefaultState());
            			}
            		}
            	}
            }
        }
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        world.updateComparatorOutputLevel(pos, this);
        super.breakBlock(world, pos, state);
    }
    
    @Override
    public boolean canSustainBush(IBlockState state) {
    	return super.canSustainBush(state);
    }

    @Override
    @Nonnull
    public Block setCreativeTab(@Nonnull CreativeTabs tab) {
        if (!creativeTabSet) {
            super.setCreativeTab(tab);
            this.creativeTabSet = true;
        }
        return this;
    }

    @Override
    public boolean causesSuffocation(@Nonnull IBlockState state) {
        return state.getMaterial().blocksMovement() && state.isFullCube();
    }

    @Override
    @Nonnull
    public EnumPushReaction getPushReaction(@Nonnull IBlockState state) {
        return state.getMaterial().getPushReaction();
    }

    public void setItemBlock(ItemBlock itemBlock) {
        this.itemBlock = itemBlock;
    }

    @Override
    public ItemBlock getItemBlock() {
        return itemBlock == null ? new ItemBlockModel<>(this) : itemBlock;
    }

    @Override
    public IBaseMod<?> getMod() {
        return mod;
    }

    @Override
    public void setMod(IBaseMod mod) {
        this.mod = mod;
    }

    @Override
    public void getSubBlocks(@Nullable CreativeTabs creativeTab, @Nonnull NonNullList<ItemStack> list) {
        list.addAll(this.getAllSubItems(new ArrayList<>()));
    }

    @Override
    public List<ItemStack> getAllSubItems(List<ItemStack> itemStacks) {
        itemStacks.add(new ItemStack(this, 1));
        return itemStacks;
    }

    @Override
    public Item getItem() {
        return this.getItemBlock();
    }

    @Override
    public Block getBlock() {
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public Map<ItemStack, String> getOreDictNames(Map<ItemStack, String> names) {
        return names;
    }
    
    @Override
    public List<String> getModelNames(List<String> modelNames) {
        if (!Strings.isNullOrEmpty(this.getName())) {
            modelNames.add(this.getName());
        }
        return modelNames;
    }

    @Override
    public List<IGeneratedModel> getGeneratedModels() {
        List<IGeneratedModel> models = Lists.newArrayList();
        this.getResourceLocations(Lists.newArrayList()).forEach(resourceLocation ->  {
            TemplateFile templateFile = TemplateManager.getTemplateFile("block");
            Map<String, String> replacements = Maps.newHashMap();

            replacements.put("texture", new ResourceLocation(resourceLocation.getNamespace(),
                    "blocks/" + resourceLocation.getPath()).toString());
            templateFile.replaceContents(replacements);

            models.add(new GeneratedModel(resourceLocation.getPath(), ModelType.BLOCKSTATE,
                    templateFile.getFileContents()));
        });

        return models;
    }
}
