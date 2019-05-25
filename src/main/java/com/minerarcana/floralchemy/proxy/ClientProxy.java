package com.minerarcana.floralchemy.proxy;

import java.util.Map;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.StateMapperCrystalthorn;
import com.minerarcana.floralchemy.api.FloralchemyAPI;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT, modid = Floralchemy.MOD_ID)
public class ClientProxy implements IProxy {
	@SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        for(Map.Entry<ResourceLocation, Integer> entry : FloralchemyAPI.getCrystalRegistry().getCrystals().entrySet()) {
    		Block block = Block.getBlockFromName(Floralchemy.MOD_ID + ":" + "crystalthorn_" + entry.getKey().getPath());
    		if(block == null) {
    			Floralchemy.instance.getLogger().warning("Failed to load model for crystalthorn " + entry.getKey().getPath());
    			return;
    		}
    		ModelLoader.setCustomStateMapper(block, new StateMapperCrystalthorn());
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn"), "inventory"));
    	}
    }
}
