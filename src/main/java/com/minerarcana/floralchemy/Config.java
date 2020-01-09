package com.minerarcana.floralchemy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;
import com.minerarcana.floralchemy.api.FloralchemyAPI;
import com.teamacronymcoders.base.util.files.BaseFileUtils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config {
    private static final Map<String, Tuple<Integer, Integer>> FUEL_DEFAULTS = Maps.newHashMap();
    private static final List<String> CRYSTAL_DEFAULTS = new ArrayList<>();

    static {
        FUEL_DEFAULTS.put("oil", new Tuple<>(100, 50));
        FUEL_DEFAULTS.put("fuel", new Tuple<>(750, 50));
        FUEL_DEFAULTS.put("diesel", new Tuple<>(350, 50));
        FUEL_DEFAULTS.put("biodiesel", new Tuple<>(250, 50));
        CRYSTAL_DEFAULTS.add("minecraft:diamond:0");
        CRYSTAL_DEFAULTS.add("minecraft:emerald:0");
        CRYSTAL_DEFAULTS.add("minecraft:quartz:0");
        CRYSTAL_DEFAULTS.add("minecraft:prismarine_crystals:0");
    }

    public static void initConfig(File configFile) {
        BaseFileUtils.createFile(configFile);
        Configuration configuration = new Configuration(configFile);

        Property generateDefaults = configuration.get("general", "generateDefaults", true,
                "Regenerate Default Config Values");

        if(generateDefaults.getBoolean()) {
            for(Map.Entry<String, Tuple<Integer, Integer>> entry : FUEL_DEFAULTS.entrySet()) {
                String category = "fuelValues." + entry.getKey();
                configuration.getInt("burnTime", category, entry.getValue().getFirst(), 1, 10000,
                        "Number of ticks this fluid will burn for");
                configuration.getInt("powerPreTick", category, entry.getValue().getSecond(), 1, 10000,
                        "Amount of Mana produced each Tick");
            }
            configuration.getStringList("crystalList", "crystals", CRYSTAL_DEFAULTS.toArray(new String[0]), "Syntax is MODID:ItemRegName:Metadata");
            generateDefaults.set(false);
        }

        ConfigCategory fuelValues = configuration.getCategory("fuelValues");

        for(ConfigCategory fuelEntry : fuelValues.getChildren()) {
            String fluidName = fuelEntry.getName();
            int burnTime = fuelEntry.get("burnTime").getInt(1);
            int powerPreTick = fuelEntry.get("powerPreTick").getInt(1);
            FloralchemyAPI.getFluidFuelRegistry().putFuel(fluidName, burnTime, powerPreTick);
        }

        for(String crystalEntry : configuration.getStringList("crystalList", "crystals", CRYSTAL_DEFAULTS.toArray(new String[0]), "Syntax is MODID:ItemRegName:Metadata")) {
            Pair<String, String> split = splitCrystalString(crystalEntry);
            FloralchemyAPI.getCrystalRegistry().putCrystal(new ResourceLocation(split.getLeft()),
                    Integer.parseInt(split.getRight()));
        }

        if(configuration.hasChanged()) {
            configuration.save();
        }
    }
    
    private static Pair<String, String> splitCrystalString(String in) {
        String[] split = in.split(":");
        if(split.length != 3 || !NumberUtils.isParsable(split[2])) {
            Floralchemy.instance.getLogger().error("Crystal string " + in + " has a syntax error");
        }
        return Pair.of(split[0] + ":" + split[1], split[2]);
    }
}
