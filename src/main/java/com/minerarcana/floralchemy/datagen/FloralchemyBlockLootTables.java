package com.minerarcana.floralchemy.datagen;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.stream.Collectors;

public class FloralchemyBlockLootTables extends BlockLootTables {
    @Override
    @Nonnull
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS
                .getValues()
                .stream()
                .filter(block -> Optional.ofNullable(block.getRegistryName())
                        .filter(registryName -> registryName.getNamespace().equals(Floralchemy.MOD_ID))
                        .isPresent()
                )
                .collect(Collectors.toList());
    }

    @Override
    protected void addTables() {
        //Will get overridden by any other entries
        this.getKnownBlocks().forEach(this::registerDropSelfLootTable);
        LootTable.Builder hedgeLoot = LootTable.builder().addLootPool(LootPool.builder()
                .addEntry(ItemLootEntry.builder(FloralchemyBlocks.HEDGE.getItem()).weight(50))
                .addEntry(ItemLootEntry.builder(FloralchemyBlocks.CINDERMOSS.getItem()))
                .addEntry(ItemLootEntry.builder(FloralchemyBlocks.GLIMMERWEED.getItem()))
                .addEntry(ItemLootEntry.builder(FloralchemyBlocks.DEVILSNARE.getItem())));
        this.registerLootTable(FloralchemyBlocks.HEDGE.getBlock(), withExplosionDecay(FloralchemyBlocks.HEDGE.getBlock(), hedgeLoot));
        this.registerLootTable(FloralchemyBlocks.THORNY_HEDGE.getBlock(), withExplosionDecay(FloralchemyBlocks.THORNY_HEDGE.getBlock(), hedgeLoot));
    }
}
