package com.minerarcana.floralchemy.datagen;

import com.minerarcana.floralchemy.Floralchemy;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Floralchemy.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FloralchemyDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        final DataGenerator dataGenerator = event.getGenerator();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            FloralchemyBlockstateProvider blockstateProvider = new FloralchemyBlockstateProvider(dataGenerator, existingFileHelper);
            dataGenerator.addProvider(blockstateProvider);
            dataGenerator.addProvider(new FloralchemyItemModelProvider(dataGenerator, blockstateProvider.getExistingFileHelper()));
            dataGenerator.addProvider(new FloralchemyUSLanguageProvider(dataGenerator));
        }

        if (event.includeServer()) {
            dataGenerator.addProvider(new FloralchemyLootTableProvider(dataGenerator));
        }
    }
}
