package com.minerarcana.floralchemy.compat.botania;

import com.minerarcana.floralchemy.compat.botania.blockentity.PetroPetuniaBlockEntity;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;

public class FloralchemyBotaniaClient {
    public static ICapabilityProvider createCapabilities(
            PetroPetuniaBlockEntity blockEntity
    ) {
        return new ICapabilityProvider() {
            private final LazyOptional<IWandHUD> hud = LazyOptional.of(() ->
                    new TileEntityGeneratingFlower.GeneratingWandHud<>(blockEntity)
            );

            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return BotaniaForgeClientCapabilities.WAND_HUD.orEmpty(cap, hud);
            }
        };
    }
}
