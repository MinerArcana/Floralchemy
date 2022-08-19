package com.minerarcana.floralchemy.compat.botania;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.compat.botania.block.FloatingPetroPetuniaBlock;
import com.minerarcana.floralchemy.compat.botania.block.PetroPetuniaBlock;
import com.minerarcana.floralchemy.compat.botania.blockentity.PetroPetuniaBlockEntity;
import com.minerarcana.floralchemy.compat.botania.item.ManaFlowerBlockItem;
import com.minerarcana.floralchemy.compat.botania.model.FloatingLoaderBuilder;
import com.minerarcana.floralchemy.compat.botania.recipe.PetalRecipeBuilder;
import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.client.render.tile.RenderTileSpecialFlower;
import vazkii.botania.common.block.BlockFloatingSpecialFlower;

import java.util.Objects;
import java.util.Optional;

public class FloralchemyBotaniaContent {
    public static final String MOD_ID = "botania";

    private static final Pair<TagKey<Item>, TagKey<Block>> GENERATING_FLOWERS = dualKeys("generating_special_flowers");
    private static final Pair<TagKey<Item>, TagKey<Block>> FLOATING_SPECIAL_FLOWERS = dualKeys("special_floating_flowers");
    public static final BlockEntry<PetroPetuniaBlock> PETRO_PETUNIA = Floralchemy.getRegistrate()
            .object("petro_petunia")
            .block(PetroPetuniaBlock::new)
            .initialProperties(Material.GRASS)
            .properties(BlockBehaviour.Properties::dynamicShape)
            .properties(BlockBehaviour.Properties::noCollission)
            .properties(BlockBehaviour.Properties::instabreak)
            .addLayer(() -> RenderType::cutout)
            .tag(GENERATING_FLOWERS.getSecond())
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
            .item(ManaFlowerBlockItem::new)
            .tag(GENERATING_FLOWERS.getFirst())
            .model((context, provider) -> provider.generated(context, provider.modLoc("block/" + context.getName())))
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

    public static final BlockEntry<FloatingPetroPetuniaBlock> FLOATING_PETRO_PETUNIA = Floralchemy.getRegistrate()
            .object("floating_petro_petunia")
            .block(FloatingPetroPetuniaBlock::new)
            .initialProperties(Material.DIRT)
            .properties(properties -> properties.sound(SoundType.GRAVEL)
                    .strength(0.5F)
                    .lightLevel(s -> 15)
            )
            .addLayer(() -> RenderType::cutout)
            .tag(FLOATING_SPECIAL_FLOWERS.getSecond())
            .blockstate((context, provider) -> provider.getVariantBuilder(context.get())
                    .forAllStates(blockState -> {
                        String name = context.getName() + (blockState.getValue(FloatingPetroPetuniaBlock.POWERED) ? "_powered" : "");
                        return ConfiguredModel.builder()
                                .modelFile(provider.models()
                                        .getBuilder("block/" + name)
                                        .parent(provider.models()
                                                .getExistingFile(provider.mcLoc("block/block"))
                                        )
                                        .customLoader(FloatingLoaderBuilder::new)
                                        .flower(provider.models()
                                                .nested()
                                                .parent(provider.models()
                                                        .getExistingFile(provider.modLoc("block/petro_petunia"))
                                                )
                                        )
                                        .end()
                                )
                                .build();
                    })
            )
            .item(ManaFlowerBlockItem::new)
            .tag(FLOATING_SPECIAL_FLOWERS.getFirst())
            .tab(() -> CreativeModeTab.TAB_MISC)
            .build()
            .register();

    public static final RegistryEntry<BlockEntityType<PetroPetuniaBlockEntity>> PETRO_PETUNIA_BLOCK_ENTITY =
            Floralchemy.getRegistrate()
                    .object("petro_petunia")
                    .blockEntity(PetroPetuniaBlockEntity::new)
                    .validBlocks(
                            PETRO_PETUNIA,
                            FLOATING_PETRO_PETUNIA
                    )
                    .renderer(() -> RenderTileSpecialFlower::new)
                    .register();

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

    private static Pair<TagKey<Item>, TagKey<Block>> dualKeys(String name) {
        return Pair.of(
                Objects.requireNonNull(ForgeRegistries.ITEMS.tags())
                        .createTagKey(rl(name)),
                Objects.requireNonNull(ForgeRegistries.BLOCKS.tags())
                        .createTagKey(rl(name))
        );
    }
}
