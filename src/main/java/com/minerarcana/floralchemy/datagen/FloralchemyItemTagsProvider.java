package com.minerarcana.floralchemy.datagen;


import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;

import javax.annotation.Nonnull;

public class FloralchemyItemTagsProvider extends ItemTagsProvider {

    public FloralchemyItemTagsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        FloralchemyBlocks.HEDGES.forEach(group -> this.getBuilder(ItemTags.WALLS).add(group.getItem()));
    }

    @Override
    @Nonnull
    public String getName() {
        return "Floralchemy Item Tags";
    }
}
