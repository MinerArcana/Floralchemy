package com.minerarcana.floralchemy.api;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.util.ResourceLocation;

public class CrystalRegistry {
    private Map<ResourceLocation, Integer> crystals;

    public CrystalRegistry() {
        crystals = Maps.newHashMap();
    }

    public void putCrystal(ResourceLocation registryName, int metadata) {
        crystals.put(registryName, metadata);
    }
    
    public void putCrystal(ResourceLocation registryName) {
        crystals.put(registryName, 0);
    }
    
    public Map<ResourceLocation, Integer> getCrystals() {
        return crystals;
    }

}
