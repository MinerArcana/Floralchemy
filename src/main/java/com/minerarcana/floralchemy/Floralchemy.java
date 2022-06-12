package com.minerarcana.floralchemy;

import com.google.common.base.Suppliers;
import com.minerarcana.floralchemy.compat.botania.BotaniaContent;
import com.minerarcana.floralchemy.content.FloralchemyRecipes;
import com.tterrag.registrate.Registrate;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod(Floralchemy.ID)
public class Floralchemy {
    public static final String ID = "floralchemy";

    private static final Supplier<Registrate> REGISTRATE = Suppliers.memoize(() -> Registrate.create(ID));

    public Floralchemy() {

        FloralchemyRecipes.setup();

        setupCompat("botania", () -> BotaniaContent::setup);
    }

    public void setupCompat(String id, Supplier<Runnable> modSetup) {
        if (ModList.get().isLoaded(id)) {
            modSetup.get().run();
        }
    }
    public static Registrate getRegistrate() {
        return REGISTRATE.get();
    }
}
