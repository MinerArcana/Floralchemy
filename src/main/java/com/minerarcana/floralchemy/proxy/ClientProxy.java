package com.minerarcana.floralchemy.proxy;

import java.util.Map;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.StateMapperCrystalthorn;
import com.minerarcana.floralchemy.api.FloralchemyAPI;
import com.minerarcana.floralchemy.block.flower.SubTilePetroPetunia;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.botania.api.BotaniaAPIClient;

@EventBusSubscriber(value = Side.CLIENT, modid = Floralchemy.MOD_ID)
public class ClientProxy implements IProxy {
    @Override
    public void registerModels() {
        BotaniaAPIClient.registerSubtileModel(SubTilePetroPetunia.NAME,
                new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, SubTilePetroPetunia.NAME), "normal"),
                new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, SubTilePetroPetunia.NAME), "inventory"));
    }
    
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
    	for(Map.Entry<ResourceLocation, Integer> entry : FloralchemyAPI.getCrystalRegistry().getCrystals().entrySet()) {
    		Block block = Block.getBlockFromName(Floralchemy.MOD_ID + ":" + "crystalthorn_" + entry.getKey().getPath());
    		ModelLoader.setCustomStateMapper(block, new StateMapperCrystalthorn());
    		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn"), "inventory"));
    	}
    }
}
