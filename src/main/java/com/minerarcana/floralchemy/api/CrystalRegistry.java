package com.minerarcana.floralchemy.api;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

public class CrystalRegistry {
    private ArrayList<Tuple<ResourceLocation, Integer>> crystals;

    public CrystalRegistry() {
        crystals = Lists.newArrayList();
    }

    public void putCrystal(ResourceLocation registryName, int metadata) {
        if(ForgeRegistries.ITEMS.getValue(registryName) == null) {
            //Floralchemy.instance.getLogger()
            //        .warning(registryName.toString() + " not found for crystalthorn registration. Discarding.");
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
