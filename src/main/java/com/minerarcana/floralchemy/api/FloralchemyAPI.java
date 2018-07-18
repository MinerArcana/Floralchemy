package com.minerarcana.floralchemy.api;

public class FloralchemyAPI {
    private final static FluidFuelRegistry fluidFuelRegistry = new FluidFuelRegistry();

    public static FluidFuelRegistry getFluidFuelRegistry() {
        return fluidFuelRegistry;
    }
}
