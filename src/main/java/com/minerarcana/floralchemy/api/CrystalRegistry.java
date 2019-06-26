package com.minerarcana.floralchemy.api;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;

public class CrystalRegistry {
    private ArrayList<Tuple<ResourceLocation, Integer>> crystals;

    public CrystalRegistry() {
        crystals = Lists.newArrayList();
    }

    public void putCrystal(ResourceLocation registryName, int metadata) {
        crystals.add(new Tuple<ResourceLocation, Integer>(registryName, metadata));
    }
    
    public void putCrystal(ResourceLocation registryName) {
    	this.putCrystal(registryName, 0);
    }
    
    public ArrayList<Tuple<ResourceLocation, Integer>> getCrystals() {
        return crystals;
    }

}
