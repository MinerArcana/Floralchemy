package com.minerarcana.floralchemy;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Floralchemy.MOD_ID)
public class FloraEventHandler {
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if(!event.getWorld().isRemote && event.getWorld() instanceof WorldServer) {
            event.getWorld().addEventListener(new WorldEventListener((WorldServer) event.getWorld()));
        }
    }
}
