package com.minerarcana.floralchemy.proxy;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.api.FloralchemyAPI;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT, modid = Floralchemy.MOD_ID)
public class ClientProxy implements IProxy {
	@SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        for(Tuple<ResourceLocation, Integer> crystal : FloralchemyAPI.getCrystalRegistry().getCrystals()) {
    		ModelLoader.setCustomModelResourceLocation(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Floralchemy.MOD_ID + ":" + "crystalthorn_" + crystal.getFirst().getPath())), 0, new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn"), "inventory"));
    	}
    }
}
