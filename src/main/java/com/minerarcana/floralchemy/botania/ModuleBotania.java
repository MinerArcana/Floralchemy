package com.minerarcana.floralchemy.botania;

import java.util.List;

import com.minerarcana.floralchemy.Floralchemy;
import com.teamacronymcoders.base.modulesystem.Module;
import com.teamacronymcoders.base.modulesystem.ModuleBase;
import com.teamacronymcoders.base.modulesystem.dependencies.IDependency;
import com.teamacronymcoders.base.modulesystem.dependencies.ModDependency;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.botania.api.BotaniaAPI;

@Module(value = Floralchemy.MOD_ID)
public class ModuleBotania extends ModuleBase {

    @Override
    public String getClientProxyPath() {
        return "com.minerarcana.floralchemy.botania.ClientProxy";
    }

    @Override
    public List<IDependency> getDependencies(List<IDependency> dependencies) {
        dependencies.add(new ModDependency("botania"));
        return dependencies;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        BotaniaAPI.registerSubTile(SubTilePetroPetunia.NAME, SubTilePetroPetunia.class);
        BotaniaAPI.addSubTileToCreativeMenu(SubTilePetroPetunia.NAME);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        BotaniaRecipes.init();
        LexiconPages.init();
    }

    @Override
    public String getName() {
        return "Botania";
    }

}
