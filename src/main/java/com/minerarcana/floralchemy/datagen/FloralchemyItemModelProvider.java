package com.minerarcana.floralchemy.datagen;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.block.BlockBaseBush;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FloralchemyItemModelProvider extends ItemModelProvider {
    public FloralchemyItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Floralchemy.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Floralchemy Item Models";
    }

    @Override
    protected void registerModels() {
        List<Block> ourBlocks = ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> Optional.ofNullable(block.getRegistryName())
                        .filter(registryName -> registryName.getNamespace().equals(Floralchemy.MOD_ID))
                        .isPresent()).collect(Collectors.toList());
        for (Block block : ourBlocks) {
            if (block instanceof BlockBaseBush) {
                blockItem(() -> block);
            }
        }
    }

    private String name(Supplier<? extends IItemProvider> item) {
        return item.get().asItem().getRegistryName().getPath();
    }

    private ItemModelBuilder blockItem(Supplier<? extends Block> block) {
        return blockItem(block, "");
    }

    private ItemModelBuilder blockItem(Supplier<? extends Block> block, String suffix) {
        return withExistingParent(name(block), modLoc("block/" + name(block) + suffix));
    }
}
