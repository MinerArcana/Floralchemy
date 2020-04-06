package com.minerarcana.floralchemy.loot;

import com.minerarcana.floralchemy.Floralchemy;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Floralchemy.MOD_ID)
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

    //Ta Botania
    public static LootPool getInjectPool(String entryName) {
        return LootPool.builder()
                .addEntry(getInjectEntry(entryName, 1))
                .bonusRolls(0, 1)
                .name(Floralchemy.MOD_ID + "_inject")
                .build();
    }

    private static LootEntry.Builder getInjectEntry(String name, int weight) {
        ResourceLocation table = new ResourceLocation(Floralchemy.MOD_ID, "inject/" + name);
        return TableLootEntry.builder(table)
                .weight(weight);
    }
}
