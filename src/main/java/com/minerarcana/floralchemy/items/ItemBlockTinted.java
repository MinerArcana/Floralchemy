package com.minerarcana.floralchemy.items;

import java.util.ArrayList;
import java.util.List;

import com.teamacronymcoders.base.blocks.IHasBlockColor;
import com.teamacronymcoders.base.items.IHasItemColor;
import com.teamacronymcoders.base.items.IHasItemMeshDefinition;
import com.teamacronymcoders.base.items.itemblocks.ItemBlockGeneric;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemBlockTinted<BLOCK extends Block & IHasBlockColor> extends ItemBlockGeneric<BLOCK>
        implements IHasItemColor, IHasItemMeshDefinition {

    public ItemBlockTinted(BLOCK block) {
        super(block);
    }

    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        if(tintIndex == 0) {
            return getActualBlock().colorMultiplier(null, null, null, tintIndex);
        }
        return 0;
    }

    @Override
    public int getMetadata(int damage) {
        return 0;
    }

    @Override
    public ResourceLocation getResourceLocation(ItemStack itemStack) {
        return getActualBlock().getRegistryName();
    }

    @Override
    public List<ResourceLocation> getAllVariants() {
        List<ResourceLocation> locs = new ArrayList<>();
        getAllSubItems(new ArrayList<>()).forEach(stack -> locs.add(this.getResourceLocation(stack)));
        return locs;
    }
}