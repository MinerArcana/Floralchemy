package com.minerarcana.floralchemy.datagen;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.block.BlockBaseBush;
import com.minerarcana.floralchemy.block.BlockHedge;
import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
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
        for(BlockRegistryObjectGroup<BlockHedge, BlockItem, ?> hedge : FloralchemyBlocks.HEDGES) {
            this.tintedWallBlock(hedge.getBlock(), hedge.getName(), mcLoc("block/" + hedge.getBlock().getType() + "_leaves"));
        }
        this.simpleBlock(FloralchemyBlocks.LEAKY_CAULDRON.getBlock(),
                models().withExistingParent(FloralchemyBlocks.LEAKY_CAULDRON.getName(), "cauldron")
                        .texture("particle", "minecraft:block/cauldron_side")
                        .texture("top", "minecraft:block/cauldron_top")
                        .texture("bottom", modLoc("block/leaky_cauldron").toString())
                        .texture("inside", "minecraft:block/cauldron_inner")
        );
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

    private void tintedWallBlock(WallBlock block, String baseName, ResourceLocation texture) {
        wallBlock(block, tintedWallPost(baseName + "_post", texture), tintedWallSide(baseName + "_side", texture));
    }

    public BlockModelBuilder tintedWallPost(String name, ResourceLocation wall) {
        return this.models().singleTexture(name, modLoc(ModelProvider.BLOCK_FOLDER + "/tinted_wall_post"), "wall", wall);
    }

    public BlockModelBuilder tintedWallSide(String name, ResourceLocation wall) {
        return this.models().singleTexture(name, modLoc(ModelProvider.BLOCK_FOLDER + "/tinted_wall_side"), "wall", wall);
    }
}
