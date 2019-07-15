package com.minerarcana.floralchemy;

import java.io.File;

import com.minerarcana.floralchemy.api.FloralchemyAPI;
import com.minerarcana.floralchemy.block.BlockCrystalthorn;
import com.minerarcana.floralchemy.loot.LootFunctionCrystalthorn;
import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;

@Mod(
        modid = Floralchemy.MOD_ID,
        name = Floralchemy.MOD_NAME,
        version = Floralchemy.VERSION,
        dependencies = Floralchemy.DEPENDS
)
@EventBusSubscriber
public class Floralchemy extends BaseModFoundation<Floralchemy> {

    public static final String MOD_ID = "floralchemy";
    public static final String MOD_NAME = "Floralchemy";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDS = "required-after:base@[0.0.0,);after:thaumcraft; after:botania";
    
    @Instance("floralchemy")
	public static Floralchemy instance;

    public Floralchemy() {
        super(MOD_ID, MOD_NAME, VERSION, CreativeTabs.MISC);
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	//Forced earlier so it is available to the crystalthorn. Bit jank...
    	Config.initConfig(new File(event.getModConfigurationDirectory(), "acronym/floralchemy.cfg"));
        super.preInit(event);
    }

    @Override
    @EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
        LootFunctionManager.registerFunction(new LootFunctionCrystalthorn.Serializer());
        LootTableList.register(new ResourceLocation(MOD_ID, "inject/end_city_treasure"));
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
			registry.register(new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn_" + entry.getFirst().getPath()), block);
		}
    }

    @Override
    public Floralchemy getInstance() {
        return this;
    }

    @Override
    public boolean hasConfig() {
        return false;
    }
}
