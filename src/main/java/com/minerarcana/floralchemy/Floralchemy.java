package com.minerarcana.floralchemy;

import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import com.hrznstudio.titanium.tab.TitaniumTab;
import com.hrznstudio.titanium.util.SidedHandler;
import com.minerarcana.floralchemy.block.BlockBaseBush;
import com.minerarcana.floralchemy.block.BlockHedge;
import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import com.minerarcana.floralchemy.datagen.FloralchemyLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.ILightReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mod(Floralchemy.MOD_ID)
@EventBusSubscriber
public class Floralchemy {

    public static final String MOD_ID = "floralchemy";

    public static Logger LOGGER = LogManager.getLogger();

    public static ItemGroup ITEM_GROUP = new TitaniumTab(MOD_ID,
            () -> new ItemStack(net.minecraft.block.Blocks.ANVIL));

    public Floralchemy() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        FloralchemyBlocks.register(modBus);

        SidedHandler.runOn(Dist.CLIENT, () -> () -> EventManager.mod(ColorHandlerEvent.Item.class).process((event) -> {
            for(BlockRegistryObjectGroup<BlockHedge, BlockItem, ?> hedge : FloralchemyBlocks.HEDGES) {
                event.getBlockColors().register((state, lightReader, pos, tintIndex) -> getFoliageColor(hedge.getBlock().getType(), lightReader, pos), hedge.getBlock());
                event.getItemColors().register((itemStack, tintIndex) -> {
                    BlockState blockstate = ((BlockItem)itemStack.getItem()).getBlock().getDefaultState();
                    return event.getBlockColors().getColor(blockstate, (ILightReader)null, (BlockPos)null, tintIndex);
                }, hedge.getItem());
            }
        }).subscribe());
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

        /*
        // Loot
        LootFunctionManager.registerFunction(new LootFunctionCrystalthorn.Serializer());
        // Vilages
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageHedgedHouseHandler());
        MapGenStructureIO.registerStructureComponent(VillageHedgeHouse.class, "hedge_house");
        GameRegistry.registerWorldGenerator(new FloralchemyWorldGenerator(), 0);*/

    private void clientSetup(final FMLClientSetupEvent event) {
        List<Block> ourBlocks = ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> Optional.ofNullable(block.getRegistryName())
                        .filter(registryName -> registryName.getNamespace().equals(Floralchemy.MOD_ID))
                        .isPresent()).collect(Collectors.toList());
        for (Block block : ourBlocks) {
            if (block instanceof BlockBaseBush) {
                RenderTypeLookup.setRenderLayer(block, RenderType.getCutout());
            }
            if(block instanceof BlockHedge) {
                RenderTypeLookup.setRenderLayer(block, RenderType.getCutout());
            }
        }
    }

    public static ResourceLocation getCrystalthornResourceLocation(Tuple<ResourceLocation, Integer> entry) {
        return new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn_" + entry.getA().getPath() + (entry.getB() > 0 ? "_" + entry.getB() : ""));
    }
}
