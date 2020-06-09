package com.minerarcana.floralchemy.content;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.block.*;
import com.minerarcana.floralchemy.tileentity.TileEntityFloodedSoil;
import com.minerarcana.floralchemy.tileentity.TileEntityLeakyCauldron;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FloralchemyBlocks {

    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Floralchemy.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Floralchemy.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Floralchemy.MOD_ID);

    public static final BlockRegistryObjectGroup<BlockCindermoss, BlockItem, ?> CINDERMOSS =
            new BlockRegistryObjectGroup<>("cindermoss", BlockCindermoss::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);

    public static final BlockRegistryObjectGroup<BlockDevilsnare, BlockItem, ?> DEVILSNARE = 
            new BlockRegistryObjectGroup<>("devilsnare", BlockDevilsnare::new, blockItemCreator())
            .register(BLOCKS, ITEMS);

    public static final BlockRegistryObjectGroup<BlockGlimmerweed, BlockItem, ?> GLIMMERWEED =
            new BlockRegistryObjectGroup<>("glimmerweed", BlockGlimmerweed::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);

    public static final List<BlockRegistryObjectGroup<BlockHedge, BlockItem, ?>> HEDGES =
            Stream.of(Blocks.ACACIA_LEAVES, Blocks.OAK_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.SPRUCE_LEAVES)
                    .map(leafBlock -> new BlockRegistryObjectGroup<>(leafBlock.getRegistryName().getPath().replace("_leaves", "_hedge"), () ->
                            new BlockHedge(leafBlock.getRegistryName().getPath().replace("_leaves", ""), false), blockItemCreator()).register(BLOCKS, ITEMS))
                    //.peek(group -> FloralchemyBlocks.HEDGES.add(FloralchemyBlocks.THORNY_HEDGE))
                    .collect(Collectors.toList());

    public static final BlockRegistryObjectGroup<BlockHedge, BlockItem, ?> THORNY_HEDGE =
            new BlockRegistryObjectGroup<>("thorny_hedge", () -> new BlockHedge("thorny",true), blockItemCreator())
                    .register(BLOCKS, ITEMS);

    public static final BlockRegistryObjectGroup<BlockLeakyCauldron, BlockItem, TileEntityLeakyCauldron> LEAKY_CAULDRON =
            new BlockRegistryObjectGroup<>("leaky_cauldron", BlockLeakyCauldron::new, blockItemCreator(), TileEntityLeakyCauldron::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static final BlockRegistryObjectGroup<BlockFloodedSoil, BlockItem, TileEntityFloodedSoil> FLOODED_SOIL =
            new BlockRegistryObjectGroup<>("flooded_soil", BlockFloodedSoil::new, blockItemCreator(), TileEntityFloodedSoil::new)
                    .register(BLOCKS, ITEMS, TILE_ENTITIES);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        TILE_ENTITIES.register(bus);
        ITEMS.register(bus);
    }

    private static <B extends Block> Function<B, BlockItem> blockItemCreator() {
        return block -> new BlockItem(block, new Item.Properties().group(Floralchemy.ITEM_GROUP));
    }
}
