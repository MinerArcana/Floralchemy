package com.minerarcana.floralchemy.datagen;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.block.BlockBaseBush;
import com.minerarcana.floralchemy.block.BlockHedge;
import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
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
                blockSprite(() -> block);
            }
        }
        for(BlockRegistryObjectGroup<BlockHedge, BlockItem, ?> hedge : FloralchemyBlocks.HEDGES) {
            this.singleTexture(hedge.getName(), modLoc(ITEM_FOLDER + "/tinted_wall_inventory"),
                    "wall", mcLoc("block/" + hedge.getName().replace("_hedge", "") + "_leaves"));
        }
        this.blockItem(FloralchemyBlocks.LEAKY_CAULDRON);
    }

    private String name(Supplier<? extends IItemProvider> item) {
        return item.get().asItem().getRegistryName().getPath();
    }

    private ResourceLocation itemTexture(Supplier<? extends IItemProvider> item) {
        return modLoc("item/" + name(item));
    }

    private ItemModelBuilder blockItem(Supplier<? extends Block> block) {
        return blockItem(block, "");
    }

    private ItemModelBuilder blockItem(Supplier<? extends Block> block, String suffix) {
        return withExistingParent(name(block), modLoc("block/" + name(block) + suffix));
    }

    private ItemModelBuilder blockWithInventoryModel(Supplier<? extends Block> block) {
        return withExistingParent(name(block), modLoc("block/" + name(block) + "_inventory"));
    }

    private ItemModelBuilder blockSprite(Supplier<? extends Block> block) {
        return blockSprite(block, modLoc("block/" + name(block)));
    }

    private ItemModelBuilder blockSprite(Supplier<? extends Block> block, ResourceLocation texture) {
        return generated(() -> block.get().asItem(), texture);
    }

    private ItemModelBuilder generated(Supplier<? extends IItemProvider> item) {
        return generated(item, itemTexture(item));
    }

    private ItemModelBuilder generated(Supplier<? extends IItemProvider> item, ResourceLocation texture) {
        return getBuilder(name(item)).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", texture);
    }

    private ItemModelBuilder handheld(Supplier<? extends IItemProvider> item) {
        return handheld(item, itemTexture(item));
    }

    private ItemModelBuilder handheld(Supplier<? extends IItemProvider> item, ResourceLocation texture) {
        return withExistingParent(name(item), "item/handheld").texture("layer0", texture);
    }
}
