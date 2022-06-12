package com.minerarcana.floralchemy.compat.botania;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.compat.botania.block.PetroPetuniaBlock;
import com.minerarcana.floralchemy.compat.botania.blockentity.PetroPetuniaBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class BotaniaContent {

    public static final BlockEntry<PetroPetuniaBlock> PETRO_PETUNIA = Floralchemy.getRegistrate()
            .object("petro_petunia")
            .block(PetroPetuniaBlock::new)
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
