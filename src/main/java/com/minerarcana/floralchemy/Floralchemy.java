package com.minerarcana.floralchemy;

import com.google.common.base.Suppliers;
import com.minerarcana.floralchemy.compat.botania.FloralchemyBotaniaContent;
import com.minerarcana.floralchemy.content.FloralchemyRecipes;
import com.tterrag.registrate.Registrate;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod(Floralchemy.ID)
public class Floralchemy {
    public static final String ID = "floralchemy";

    private static final Supplier<Registrate> REGISTRATE = Suppliers.memoize(() -> Registrate.create(ID));

    public Floralchemy() {

        FloralchemyRecipes.setup();

        setupCompat("botania", () -> FloralchemyBotaniaContent::setup);
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
