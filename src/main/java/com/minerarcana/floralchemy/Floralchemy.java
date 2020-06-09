package com.minerarcana.floralchemy;

import com.hrznstudio.titanium.tab.TitaniumTab;
import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Floralchemy.MOD_ID)
@EventBusSubscriber
public class Floralchemy {

    public static final String MOD_ID = "floralchemy";

    public static Logger LOGGER = LogManager.getLogger();

    public static ItemGroup ITEM_GROUP = new TitaniumTab(MOD_ID,
            () -> new ItemStack(net.minecraft.block.Blocks.ANVIL));

    public Floralchemy() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modBus.addListener(ClientEventHandler::clientSetup));
        FloralchemyBlocks.register(modBus);
    }
        /*
        // Loot
        LootFunctionManager.registerFunction(new LootFunctionCrystalthorn.Serializer());
        // Vilages
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageHedgedHouseHandler());
        MapGenStructureIO.registerStructureComponent(VillageHedgeHouse.class, "hedge_house");
        GameRegistry.registerWorldGenerator(new FloralchemyWorldGenerator(), 0);*/
}
