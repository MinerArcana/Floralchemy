package com.minerarcana.floralchemy;

import java.io.File;
import java.util.Map;

import com.google.common.collect.Maps;
import com.minerarcana.floralchemy.api.FloralchemyAPI;
import com.teamacronymcoders.base.util.files.BaseFileUtils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.config.*;

public class Config {
    private static final Map<String, Tuple<Integer, Integer>> FUEL_DEFAULTS = Maps.newHashMap();
    private static final Map<String, Integer> CRYSTAL_DEFAULTS = Maps.newHashMap();

    static {
        FUEL_DEFAULTS.put("oil", new Tuple<>(100, 50));
        FUEL_DEFAULTS.put("fuel", new Tuple<>(750, 50));
        FUEL_DEFAULTS.put("diesel", new Tuple<>(350, 50));
        FUEL_DEFAULTS.put("biodiesel", new Tuple<>(250, 50));
        CRYSTAL_DEFAULTS.put("minecraft:diamond", 0);
        CRYSTAL_DEFAULTS.put("minecraft:emerald", 0);
        CRYSTAL_DEFAULTS.put("minecraft:quartz", 0);
        CRYSTAL_DEFAULTS.put("minecraft:prismarine_crystals", 0);
    }

    public static void initConfig(File configFile) {
        BaseFileUtils.createFile(configFile);
        Configuration configuration = new Configuration(configFile);

        Property generateDefaults = configuration.get("general", "generateDefaults", true,
                "Regenerate Default Fuel Values");

        if(generateDefaults.getBoolean()) {
            for(Map.Entry<String, Tuple<Integer, Integer>> entry : FUEL_DEFAULTS.entrySet()) {
                String category = "fuelValues." + entry.getKey();
                configuration.getInt("burnTime", category, entry.getValue().getFirst(), 1, 10000,
                        "Number of ticks this fluid will burn for");
                configuration.getInt("powerPreTick", category, entry.getValue().getSecond(), 1, 10000,
                        "Amount of Mana produced each Tick");
            }
            for(Map.Entry<String, Integer> entry : CRYSTAL_DEFAULTS.entrySet()) {
                String category = "crystals." + entry.getKey();
                configuration.getInt("metadata", category, entry.getValue(), 0, Short.MAX_VALUE,
                        "Metadata of the crystal item");
            }
            generateDefaults.set(false);
        }

        ConfigCategory fuelValues = configuration.getCategory("fuelValues");

        for(ConfigCategory fuelEntry : fuelValues.getChildren()) {
            String fluidName = fuelEntry.getName();
            int burnTime = fuelEntry.get("burnTime").getInt(1);
            int powerPreTick = fuelEntry.get("powerPreTick").getInt(1);
            FloralchemyAPI.getFluidFuelRegistry().putFuel(fluidName, burnTime, powerPreTick);
        }

        ConfigCategory crystals = configuration.getCategory("crystals");

        for(ConfigCategory crystalEntry : crystals.getChildren()) {
            FloralchemyAPI.getCrystalRegistry().putCrystal(new ResourceLocation(crystalEntry.getName()),
                    crystalEntry.get("metadata").getInt(0));
        }

        if(configuration.hasChanged()) {
            configuration.save();
        }
    }
}
