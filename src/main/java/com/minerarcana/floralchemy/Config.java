package com.minerarcana.floralchemy;

import com.google.common.collect.Maps;
import com.minerarcana.floralchemy.api.FloralchemyAPI;
import com.teamacronymcoders.base.util.files.BaseFileUtils;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.Map;

public class Config {
    private static final Map<String, Tuple<Integer, Integer>> FUEL_DEFAULTS = Maps.newHashMap();

    static {
        FUEL_DEFAULTS.put("oil", new Tuple<>(100, 50));
        FUEL_DEFAULTS.put("fuel", new Tuple<>(750, 50));
        FUEL_DEFAULTS.put("diesel", new Tuple<>(350, 50));
        FUEL_DEFAULTS.put("biodiesel", new Tuple<>(250, 50));
    }

    public static void initConfig(File configFile) {
        BaseFileUtils.createFile(configFile.getParentFile());
        Configuration configuration = new Configuration(configFile);

        Property generateDefaults = configuration.get("general","generateDefaults",
                true, "Regenerate Default Fuel Values");

        if (generateDefaults.getBoolean()) {
            for (Map.Entry<String, Tuple<Integer, Integer>> entry : FUEL_DEFAULTS.entrySet()) {
                String category = "fuelValues." + entry.getKey();
                configuration.getInt("burnTime", category, entry.getValue().getFirst(), 1, 10000, "Amount of Ticks this Fluid will burn");
                configuration.getInt("powerPreTick", category, entry.getValue().getSecond(), 1, 10000, "Amount of Mana produced each Tick");
            }

            generateDefaults.set(false);
        }

        ConfigCategory fuelValues = configuration.getCategory("fuelValues");

        for (ConfigCategory fuelEntry: fuelValues.getChildren()) {
            String fluidName = fuelEntry.getName();
            int burnTime = fuelEntry.get("burnTime").getInt(1);
            int powerPreTick = fuelEntry.get("powerPreTick").getInt(1);
            FloralchemyAPI.getFluidFuelRegistry().putFuel(fluidName, burnTime, powerPreTick);
        }

        if(configuration.hasChanged()) {
            configuration.save();
        }
    }
}
