package com.minerarcana.floralchemy.loot;

import com.minerarcana.floralchemy.Floralchemy;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Floralchemy.MOD_ID)
public class LootEventHandler {
    @SubscribeEvent
    public static void lootLoad(LootTableLoadEvent event) {
        String prefix = "minecraft:";
        String name = event.getName().toString();
        if(name.startsWith(prefix)) {
            String file = name.substring(name.indexOf(prefix) + prefix.length());
            switch(file) {
                case "chests/end_city_treasure":
                case "chests/stronghold_library":
                case "chests/stronghold_crossing":
                case "chests/stronghold_corridor":
                case "entities/witch":
                    event.getTable().addPool(getInjectPool(file));
                    break;
                default:
                    break;
            }
        }
    }

    // Ta Botania
    private static LootPool getInjectPool(String entryName) {
        return new LootPool(new LootEntry[] { getInjectEntry(entryName, 1) }, new LootCondition[0],
                new RandomValueRange(1), new RandomValueRange(0, 1), "floralchemy_inject_pool");
    }

    private static LootEntryTable getInjectEntry(String name, int weight) {
        return new LootEntryTable(new ResourceLocation(Floralchemy.MOD_ID, "inject/" + name), weight, 0,
                new LootCondition[0], "floralchemy_inject_entry");
    }
}
