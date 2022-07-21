package com.minerarcana.floralchemy;

import com.google.common.base.Suppliers;
import com.minerarcana.floralchemy.compat.botania.FloralchemyBotaniaContent;
import com.minerarcana.floralchemy.content.FloralchemyRecipes;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

@Mod(Floralchemy.ID)
public class Floralchemy {
    public static final String ID = "floralchemy";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    private static final Supplier<Registrate> REGISTRATE = Suppliers.memoize(() -> Registrate.create(ID)
            .addDataGenerator(ProviderType.RECIPE, FloralchemyRecipes::fuelRecipeData)
    );

    public Floralchemy() {

        FloralchemyRecipes.setup();

        setupCompat("botania", () -> FloralchemyBotaniaContent::setup);
    }

    public void setupCompat(String id, Supplier<Runnable> modSetup) {
        if (ModList.get().isLoaded(id)) {
            try {
                modSetup.get().run();
            } catch (Throwable throwable) {
                LOGGER.error("Something went wrong while trying to init compat: %s".formatted(id), throwable);
            }

        }
    }

    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID, path);
    }
}
