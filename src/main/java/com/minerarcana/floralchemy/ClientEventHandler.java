package com.minerarcana.floralchemy;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import com.minerarcana.floralchemy.block.BlockBaseBush;
import com.minerarcana.floralchemy.block.BlockHedge;
import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.ILightReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientEventHandler {
    public static void clientSetup(final FMLClientSetupEvent event) {
        List<Block> ourBlocks = ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> Optional.ofNullable(block.getRegistryName())
                        .filter(registryName -> registryName.getNamespace().equals(Floralchemy.MOD_ID))
                        .isPresent()).collect(Collectors.toList());
        for (Block block : ourBlocks) {
            if (block instanceof BlockBaseBush) {
                RenderTypeLookup.setRenderLayer(block, RenderType.getCutout());
            }
            else if(block instanceof BlockHedge) {
                RenderTypeLookup.setRenderLayer(block, RenderType.getCutout());
            }
        }
    }

    @SubscribeEvent
    public void onColours(ColorHandlerEvent.Item event) {
        for(BlockRegistryObjectGroup<BlockHedge, BlockItem, ?> hedge : FloralchemyBlocks.HEDGES) {
            event.getBlockColors().register((state, lightReader, pos, tintIndex) -> getFoliageColor(hedge.getBlock().getType(), lightReader, pos), hedge.getBlock());
            event.getItemColors().register((itemStack, tintIndex) -> {
                BlockState blockstate = ((BlockItem)itemStack.getItem()).getBlock().getDefaultState();
                return event.getBlockColors().getColor(blockstate, null, null, tintIndex);
            }, hedge.getItem());
        }
    }

    public static int getFoliageColor(String type, ILightReader lightReader, BlockPos pos) {
        //MC Overrides biome colours for spruce and birch.
        if("birch".equals(type)) {
            return FoliageColors.getBirch();
        }
        else if("spruce".equals(type)) {
            return FoliageColors.getSpruce();
        }
        return lightReader != null && pos != null ? BiomeColors.getFoliageColor(lightReader, pos) : FoliageColors.getDefault();
    }
}
