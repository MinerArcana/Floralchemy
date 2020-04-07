package com.minerarcana.floralchemy.datagen;

import com.minerarcana.floralchemy.Floralchemy;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;

public class FloralchemyBlockstateProvider extends BlockStateProvider {

    private final ExistingFileHelper helper;

    public FloralchemyBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Floralchemy.MOD_ID, exFileHelper);
        this.helper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
    }
}
