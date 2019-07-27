package com.minerarcana.floralchemy.api;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.minerarcana.floralchemy.Floralchemy;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CrystalRegistry {
    private ArrayList<Tuple<ResourceLocation, Integer>> crystals;

    public CrystalRegistry() {
        crystals = Lists.newArrayList();
    }

    public void putCrystal(ResourceLocation registryName, int metadata) {
        if(ForgeRegistries.ITEMS.getValue(registryName) == null) {
            Floralchemy.instance.getLogger()
                    .warning(registryName.toString() + " not found for crystalthorn registration. Blocking.");
            return;
        }
        crystals.add(new Tuple<ResourceLocation, Integer>(registryName, metadata));
    }

    public void putCrystal(ResourceLocation registryName) {
        this.putCrystal(registryName, 0);
    }

    public ArrayList<Tuple<ResourceLocation, Integer>> getCrystals() {
        return crystals;
    }

}
