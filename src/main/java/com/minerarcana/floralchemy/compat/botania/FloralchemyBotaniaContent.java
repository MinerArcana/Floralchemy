package com.minerarcana.floralchemy.compat.botania;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.compat.botania.block.PetroPetuniaBlock;
import com.minerarcana.floralchemy.compat.botania.blockentity.PetroPetuniaBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.registries.ForgeRegistries;

public class FloralchemyBotaniaContent {

    public static final BlockEntry<PetroPetuniaBlock> PETRO_PETUNIA = Floralchemy.getRegistrate()
            .object("petro_petunia")
            .block(PetroPetuniaBlock::new)
            .properties(BlockBehaviour.Properties::dynamicShape)
            .properties(BlockBehaviour.Properties::noCollission)
            .addLayer(() -> RenderType::cutout)
            .blockstate((context, provider) -> provider.getVariantBuilder(context.get())
                    .forAllStates(blockState -> {
                        String name = context.getName() + (blockState.getValue(PetroPetuniaBlock.POWERED) ? "_powered" : "");
                        return ConfiguredModel.builder()
                                .modelFile(provider.models()
                                        .getBuilder("block/" + name)
                                        .parent(provider.models()
                                                .getExistingFile(new ResourceLocation(
                                                        "botania",
                                                        "block/shapes/cross"
                                                ))
                                        )
                                        .texture("cross", provider.modLoc("block/" + name)))
                                .build();
                    })
            )
            .blockEntity(PetroPetuniaBlockEntity::new)
            .build()
            .item()
            .build()
            .register();

    public static final RegistryEntry<BlockEntityType<PetroPetuniaBlockEntity>> PETRO_PETUNIA_BLOCK_ENTITY =
            PETRO_PETUNIA.getSibling(ForgeRegistries.BLOCK_ENTITIES);

    public static void setup() {

    }
}
