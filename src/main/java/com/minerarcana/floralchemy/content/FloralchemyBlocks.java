package com.minerarcana.floralchemy.content;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.block.BlockCindermoss;
import com.minerarcana.floralchemy.block.BlockFloodedSoil;
import com.minerarcana.floralchemy.tileentity.TileEntityFloodedSoil;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public class FloralchemyBlocks {

    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Floralchemy.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Floralchemy.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Floralchemy.MOD_ID);

    public static final BlockRegistryObjectGroup<BlockCindermoss, BlockItem, ?> CINDERMOSS =
            new BlockRegistryObjectGroup<>("cindermoss", BlockCindermoss::new, blockItemCreator())
                    .register(BLOCKS, ITEMS);

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
