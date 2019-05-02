package com.minerarcana.floralchemy;

import java.io.File;
import java.util.Map;

import com.minerarcana.floralchemy.api.FloralchemyAPI;
import com.minerarcana.floralchemy.block.flower.BlockCrystalthorn;
import com.minerarcana.floralchemy.block.flower.SubTilePetroPetunia;
import com.minerarcana.floralchemy.proxy.IProxy;
import com.teamacronymcoders.base.BaseModFoundation;
import com.teamacronymcoders.base.registrysystem.BlockRegistry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.api.BotaniaAPI;

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
    public static final String DEPENDS = "required-after:botania;required-after:base@[0.0.0,);after:thaumcraft";

    @SidedProxy(clientSide = "com.minerarcana.floralchemy.proxy.ClientProxy",
            serverSide = "com.minerarcana.floralchemy.ServerProxy")
    public static IProxy proxy;

    public Floralchemy() {
        super(MOD_ID, MOD_NAME, VERSION, CreativeTabs.MISC);
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	Config.initConfig(new File(event.getModConfigurationDirectory(), "acronym/floralchemy.cfg")); //TODO is this ok? 
        super.preInit(event);
    }

    @Override
    public void afterModuleHandlerInit(FMLPreInitializationEvent event) {
        BotaniaAPI.registerSubTile(SubTilePetroPetunia.NAME, SubTilePetroPetunia.class);
        BotaniaAPI.addSubTileToCreativeMenu(SubTilePetroPetunia.NAME);
        proxy.registerModels();
    }

    @Override
    @EventHandler
    public void init(FMLInitializationEvent event) {
        super.init(event);
        BotaniaRecipes.init();
        LexiconPages.init();
    }

    @Override
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
    
    @Override
    public void registerBlocks(BlockRegistry registry) {
		for(Map.Entry<ResourceLocation, Integer> entry : FloralchemyAPI.getCrystalRegistry().getCrystals().entrySet()) {
			registry.register(new BlockCrystalthorn(entry.getKey(), entry.getValue()));
		}
    }
    
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
    	//TODO
    	for(Map.Entry<ResourceLocation, Integer> entry : FloralchemyAPI.getCrystalRegistry().getCrystals().entrySet()) {
    		Block block = Block.getBlockFromName(MOD_ID + ":" + "crystalthorn_" + entry.getKey().getPath());
    		ModelLoader.setCustomStateMapper(block, new StateMapperCrystalthorn());
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn"), "inventory"));
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
