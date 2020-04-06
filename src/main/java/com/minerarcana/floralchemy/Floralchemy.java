package com.minerarcana.floralchemy;

import com.hrznstudio.titanium.tab.TitaniumTab;
import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Floralchemy.MOD_ID)
@EventBusSubscriber
public class Floralchemy {

    public static final String MOD_ID = "floralchemy";

    public static ItemGroup ITEM_GROUP = new TitaniumTab(MOD_ID,
            () -> new ItemStack(net.minecraft.block.Blocks.ANVIL));

    public Floralchemy() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        FloralchemyBlocks.register(modBus);
    }

        /*
        // Loot
        LootFunctionManager.registerFunction(new LootFunctionCrystalthorn.Serializer());
        LootTableList.register(new ResourceLocation(MOD_ID, "inject/end_city_treasure"));
        LootTableList.register(new ResourceLocation(MOD_ID, "inject/stronghold_corridor"));
        LootTableList.register(new ResourceLocation(MOD_ID, "inject/stronghold_crossing"));
        LootTableList.register(new ResourceLocation(MOD_ID, "inject/stronghold_library"));
        LootTableList.register(new ResourceLocation(MOD_ID, "inject/witch"));
        LootTableList.register(new ResourceLocation(MOD_ID, "block/hedge"));
        LootTableList.register(new ResourceLocation(MOD_ID, "block/thorny_hedge"));
        // Vilages
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageHedgedHouseHandler());
        MapGenStructureIO.registerStructureComponent(VillageHedgeHouse.class, "hedge_house");
        GameRegistry.registerWorldGenerator(new FloralchemyWorldGenerator(), 0);*/

    private void clientSetup(final FMLClientSetupEvent event) {

    }

    public static ResourceLocation getCrystalthornResourceLocation(Tuple<ResourceLocation, Integer> entry) {
        return new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn_" + entry.getA().getPath() + (entry.getB() > 0 ? "_" + entry.getB() : ""));
    }
}
