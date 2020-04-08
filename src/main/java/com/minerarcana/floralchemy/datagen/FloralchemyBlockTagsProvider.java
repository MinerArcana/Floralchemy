package com.minerarcana.floralchemy.datagen;


import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;

import javax.annotation.Nonnull;

public class FloralchemyBlockTagsProvider extends BlockTagsProvider {

    public FloralchemyBlockTagsProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        FloralchemyBlocks.HEDGES.forEach(group -> this.getBuilder(BlockTags.WALLS).add(group.getBlock()));
    }

    @Override
    @Nonnull
    public String getName() {
        return "Floralchemy Block Tags";
    }
}
