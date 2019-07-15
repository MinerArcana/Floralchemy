package com.minerarcana.floralchemy.botania;

import com.minerarcana.floralchemy.Floralchemy;
import com.teamacronymcoders.base.modulesystem.proxies.IModuleProxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.botania.api.BotaniaAPIClient;

public class ClientProxy implements IModuleProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
        BotaniaAPIClient.registerSubtileModel(SubTilePetroPetunia.NAME,
                new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, SubTilePetroPetunia.NAME), "normal"),
                new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, SubTilePetroPetunia.NAME), "inventory"));
	}

	@Override
	public void init(FMLInitializationEvent event) {
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
