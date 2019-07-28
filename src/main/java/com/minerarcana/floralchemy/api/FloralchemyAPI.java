package com.minerarcana.floralchemy.api;

public class FloralchemyAPI {
    private final static FluidFuelRegistry fluidFuelRegistry = new FluidFuelRegistry();
    private final static CrystalRegistry crystalRegistry = new CrystalRegistry();

    public static FluidFuelRegistry getFluidFuelRegistry() {
        return fluidFuelRegistry;
    }

    public static CrystalRegistry getCrystalRegistry() {
        return crystalRegistry;
    }
}
