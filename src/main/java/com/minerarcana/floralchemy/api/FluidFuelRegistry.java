package com.minerarcana.floralchemy.api;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import net.minecraft.util.Tuple;

public class FluidFuelRegistry {
    private Map<String, Tuple<Integer, Integer>> fluidInfo;

    public FluidFuelRegistry() {
        fluidInfo = Maps.newHashMap();
    }

    public void putFuel(String fluidName, int burnTime, int powerPreTick) {
        fluidInfo.put(fluidName, new Tuple<>(burnTime, powerPreTick));
    }

    public void removeFuel(String fluidName) {
        fluidInfo.remove(fluidName);
    }

    public Optional<Tuple<Integer, Integer>> getFuelInfo(String fluidName) {
        return Optional.ofNullable(fluidInfo.get(fluidName));
    }
}
