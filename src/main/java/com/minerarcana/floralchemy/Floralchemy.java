package com.minerarcana.floralchemy;

import java.io.File;

import com.minerarcana.floralchemy.api.FloralchemyAPI;
import com.minerarcana.floralchemy.block.*;
import com.minerarcana.floralchemy.loot.LootFunctionCrystalthorn;
import com.minerarcana.floralchemy.village.VillageHedgeHouse;
import com.minerarcana.floralchemy.village.VillageHedgedHouseHandler;
import com.minerarcana.floralchemy.worldgen.FloralchemyWorldGenerator;
import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

@Mod(modid = Floralchemy.MOD_ID, name = Floralchemy.MOD_NAME, version = Floralchemy.VERSION, dependencies = Floralchemy.DEPENDS)
@EventBusSubscriber
public class Floralchemy extends BaseModFoundation<Floralchemy> {

    public static final String MOD_ID = "floralchemy";
    public static final String MOD_NAME = "Floralchemy";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDS = "required-after:base@[0.0.0,);after:thaumcraft; after:botania";

    @Instance("floralchemy")
    public static Floralchemy instance;

    @SidedProxy(clientSide = "com.minerarcana.floralchemy.ClientProxy", serverSide = "com.minerarcana.floralchemy.CommonProxy")
    public static CommonProxy proxy;

    public Floralchemy() {
        super(MOD_ID, MOD_NAME, VERSION, CreativeTabs.MISC);
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Forced earlier so it is available to the crystalthorn. Bit jank...
        Config.initConfig(new File(event.getModConfigurationDirectory(), "acronym/floralchemy.cfg"));
        super.preInit(event);
        proxy.registerModels();
    }

    @Override
    @EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
        // Loot
        LootFunctionManager.registerFunction(new LootFunctionCrystalthorn.Serializer());
        LootTableList.register(new ResourceLocation(MOD_ID, "inject/end_city_treasure"));
        LootTableList.register(new ResourceLocation(MOD_ID, "block/hedge"));
        LootTableList.register(new ResourceLocation(MOD_ID, "block/thorny_hedge"));
        // Vilages
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageHedgedHouseHandler());
        MapGenStructureIO.registerStructureComponent(VillageHedgeHouse.class, "hedge_house");
        GameRegistry.registerWorldGenerator(new FloralchemyWorldGenerator(), 0);
    }

    @Override
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public void registerBlocks(BlockRegistry registry) {
        for(Tuple<ResourceLocation, Integer> entry : FloralchemyAPI.getCrystalRegistry().getCrystals()) {
            Block block = new BlockCrystalthorn(entry);
            registry.register(getCrystalthornResourceLocation(entry),
                    block);
        }
        registry.register(new BlockHedge("hedge", false));
        registry.register(new BlockHedge("thorny_hedge", true));
        registry.register(new BlockFloodedSoil());
        registry.register(new BlockLeakyCauldron());
        registry.register(new BlockCindermoss());
    }

    @Override
    public Floralchemy getInstance() {
        return this;
    }

    @Override
    public boolean hasConfig() {
        return false;
    }
    
    public static ResourceLocation getCrystalthornResourceLocation(Tuple<ResourceLocation, Integer> entry) {
        return new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn_" + entry.getFirst().getPath() + (entry.getSecond() > 0 ? "_" + entry.getSecond() : ""));
    }
}
