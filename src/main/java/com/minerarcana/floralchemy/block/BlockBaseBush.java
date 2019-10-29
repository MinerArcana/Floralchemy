package com.minerarcana.floralchemy.block;

import java.util.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.teamacronymcoders.base.IBaseMod;
import com.teamacronymcoders.base.IModAware;
import com.teamacronymcoders.base.blocks.IAmBlock;
import com.teamacronymcoders.base.blocks.IHasItemBlock;
import com.teamacronymcoders.base.client.models.IHasModel;
import com.teamacronymcoders.base.client.models.generator.IHasGeneratedModel;
import com.teamacronymcoders.base.client.models.generator.generatedmodel.*;
import com.teamacronymcoders.base.items.IHasOreDict;
import com.teamacronymcoders.base.items.IHasSubItems;
import com.teamacronymcoders.base.items.itemblocks.ItemBlockGeneric;
import com.teamacronymcoders.base.util.files.templates.TemplateFile;
import com.teamacronymcoders.base.util.files.templates.TemplateManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBaseBush extends BlockBush implements IHasItemBlock, IHasSubItems, IModAware, IAmBlock, IHasOreDict, IHasModel, IHasGeneratedModel {
    private IBaseMod mod;
    private boolean creativeTabSet = false;
    private ItemBlock itemBlock;
    private String name;

    public BlockBaseBush(String name) {
        super();
        this.setHardness(1F);
        this.name = name;
        this.setTranslationKey(name);
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        world.updateComparatorOutputLevel(pos, this);
        super.breakBlock(world, pos, state);
    }

    @Override
    public void onBlockAdded(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        this.updateState(world, pos, state);
        super.onBlockAdded(world, pos, state);
    }

    @Override
    public void onNeighborChange(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull BlockPos neighborPos) {
        this.updateState(world, pos, world.getBlockState(neighborPos));
        super.onNeighborChange(world, pos, neighborPos);
    }

    protected void updateState(IBlockAccess world, BlockPos pos, IBlockState state) {

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
    @SuppressWarnings("deprecation")
    public boolean causesSuffocation(@Nonnull IBlockState state) {
        return state.getMaterial().blocksMovement() && state.isFullCube();
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public EnumPushReaction getPushReaction(@Nonnull IBlockState state) {
        return state.getMaterial().getPushReaction();
    }

    public void setItemBlock(ItemBlock itemBlock) {
        this.itemBlock = itemBlock;
    }

    @Override
    public ItemBlock getItemBlock() {
        return itemBlock == null ? new ItemBlockGeneric<>(this) : itemBlock;
    }

    @Override
    public IBaseMod getMod() {
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
