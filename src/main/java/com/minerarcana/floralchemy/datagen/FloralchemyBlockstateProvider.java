package com.minerarcana.floralchemy.datagen;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.block.BlockBaseBush;
import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FloralchemyBlockstateProvider extends BlockStateProvider {

    private final ExistingFileHelper helper;

    public FloralchemyBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Floralchemy.MOD_ID, exFileHelper);
        this.helper = exFileHelper;
    }

    public ExistingFileHelper getExistingFileHelper() {
        return helper;
    }

    @Override
    protected void registerStatesAndModels() {
        List<Block> ourBlocks = ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> Optional.ofNullable(block.getRegistryName())
                        .filter(registryName -> registryName.getNamespace().equals(Floralchemy.MOD_ID))
                        .isPresent()).collect(Collectors.toList());
        for (Block block : ourBlocks) {
            if (block instanceof BlockBaseBush) {
                this.getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(bush(block, state)).build());
            }
        }
    }

    private ModelFile bush(Block block, BlockState state) {
        String name = block.getRegistryName().getPath();
        /*Collection blockStateProperties = block.getDefaultState().getProperties();
        IntegerProperty properties = BlockStateProperties.AGE_0_3;
        if (blockStateProperties.contains(BlockStateProperties.AGE_0_5))
            properties = BlockStateProperties.AGE_0_5;
        name = name + "_" + state.get(properties);*/
        return models().withExistingParent(name, mcLoc("block/cross"))
                .texture("particle", blockTexture(block))
                .texture("cross", blockTexture(block));
    }

    private ResourceLocation blockTexture(Supplier<? extends Block> block) {
        ResourceLocation base = block.get().getRegistryName();
        return modLoc("block/" + base.getPath());
    }
}
