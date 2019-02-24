package com.minerarcana.floralchemy;

import java.io.File;

import com.minerarcana.floralchemy.block.flower.SubTilePetroPetunia;
import com.minerarcana.floralchemy.proxy.IProxy;
import com.teamacronymcoders.base.BaseModFoundation;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import vazkii.botania.api.BotaniaAPI;

@Mod(
        modid = Floralchemy.MOD_ID,
        name = Floralchemy.MOD_NAME,
        version = Floralchemy.VERSION,
        dependencies = Floralchemy.DEPENDS
)
public class Floralchemy extends BaseModFoundation<Floralchemy> {

    public static final String MOD_ID = "floralchemy";
    public static final String MOD_NAME = "Floralchemy";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDS = "required-after:botania;required-after:base@[0.0.0,);";

    @SidedProxy(clientSide = "com.minerarcana.floralchemy.proxy.ClientProxy",
            serverSide = "com.minerarcana.floralchemy.ServerProxy")
    public static IProxy proxy;

    public Floralchemy() {
        super(MOD_ID, MOD_NAME, VERSION, CreativeTabs.MISC);
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        Config.initConfig(new File(event.getModConfigurationDirectory(), "acronym/floralchemy.cfg"));
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
    public Floralchemy getInstance() {
        return this;
    }

    @Override
    public boolean hasConfig() {
        return false;
    }
}
