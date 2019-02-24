package com.minerarcana.floralchemy.api;

import java.util.Map;

import com.google.common.collect.Maps;

public class CrystalRegistry {
    private Map<String, Integer> crystals;

    public CrystalRegistry() {
        crystals = Maps.newHashMap();
    }

    public void putCrystal(String registryName, int metadata) {
        crystals.put(registryName, metadata);
    }
    
    public void putCrystal(String registryName) {
        crystals.put(registryName, 0);
    }
    
    public Map<String, Integer> getCrystals() {
        return crystals;
    }

}
