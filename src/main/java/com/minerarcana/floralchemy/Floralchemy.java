package com.minerarcana.floralchemy;

import java.io.File;
import java.util.Map;

import com.minerarcana.floralchemy.api.FloralchemyAPI;
import com.minerarcana.floralchemy.block.BlockCrystalthorn;
import com.minerarcana.floralchemy.proxy.IProxy;
import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.lib.LibMisc;

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

    @SidedProxy(clientSide = "com.minerarcana.floralchemy.proxy.ClientProxy",
            serverSide = "com.minerarcana.floralchemy.proxy.ServerProxy")
    public static IProxy proxy;
    
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
		for(Map.Entry<ResourceLocation, Integer> entry : FloralchemyAPI.getCrystalRegistry().getCrystals().entrySet()) {
			Block block = new BlockCrystalthorn(entry.getKey(), entry.getValue());
			registry.register(block);
		}
    }
	
	@SubscribeEvent
	public static void lootLoad(LootTableLoadEvent evt) {
		String prefix = "minecraft:chests/";
		String name = evt.getName().toString();

		if (name.startsWith(prefix)) {
			String file = name.substring(name.indexOf(prefix) + prefix.length());
			switch (file) {
				case "end_city_treasure":
				case "stronghold_library": 
					evt.getTable().addPool(getInjectPool(file)); break;
				default: break;
			}
}
	}
	
	//Ta Botania
	private static LootPool getInjectPool(String entryName) {
		return new LootPool(new LootEntry[] { getInjectEntry(entryName, 1) }, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0, 1), "floralchemy_inject_pool");
	}

	private static LootEntryTable getInjectEntry(String name, int weight) {
		return new LootEntryTable(new ResourceLocation(LibMisc.MOD_ID, "inject/" + name), weight, 0, new LootCondition[0], "floralchemy_inject_entry");
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
