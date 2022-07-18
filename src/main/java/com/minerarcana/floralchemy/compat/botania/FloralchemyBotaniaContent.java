package com.minerarcana.floralchemy.compat.botania;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.compat.botania.block.PetroPetuniaBlock;
import com.minerarcana.floralchemy.compat.botania.blockentity.PetroPetuniaBlockEntity;
import com.minerarcana.floralchemy.compat.botania.recipe.PetalRecipeBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.Optional;

public class FloralchemyBotaniaContent {
    public static final String MOD_ID = "botania";
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
                                                .getExistingFile(rl("block/shapes/cross"))
                                        )
                                        .texture("cross", provider.modLoc("block/" + name)))
                                .build();
                    })
            )
            .blockEntity(PetroPetuniaBlockEntity::new)
            .build()
            .item()
            .tab(() -> CreativeModeTab.TAB_MISC)
            .recipe((context, provider) -> ConditionalRecipe.builder()
                    .addCondition(new ModLoadedCondition(MOD_ID))
                    .addRecipe(consumer -> PetalRecipeBuilder.of(context.get())
                            .addIngredient(tag("petals/black"))
                            .addIngredient(tag("petals/orange"))
                            .addIngredient(tag("petals/brown"))
                            .addIngredient(item("rune_water"))
                            .addIngredient(item("rune_fire"))
                            .addIngredient(item("redstone_root"))
                            .addIngredient(tag("dragonstone_gems"))
                            .build(consumer)
                    )
                    .build(provider, Floralchemy.rl("petal_apothecary/" + context.getName()))
            )
            .build()
            .register();

    public static final RegistryEntry<BlockEntityType<PetroPetuniaBlockEntity>> PETRO_PETUNIA_BLOCK_ENTITY =
            PETRO_PETUNIA.getSibling(ForgeRegistries.BLOCK_ENTITIES);

    public static void setup() {

    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    private static Ingredient tag(String path) {
        return Ingredient.of(Objects.requireNonNull(ForgeRegistries.ITEMS.tags())
                .createTagKey(rl(path))
        );
    }

    private static Ingredient item(String name) {
        return Ingredient.of(Optional.ofNullable(ForgeRegistries.ITEMS.getValue(rl(name)))
                .orElseThrow()
        );
    }
}
