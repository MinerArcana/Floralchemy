package com.minerarcana.floralchemy.compat.botania.block;

import com.minerarcana.floralchemy.compat.botania.BotaniaContent;
import com.minerarcana.floralchemy.compat.botania.blockentity.PetroPetuniaBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

public class PetroPetuniaBlock extends ManaFlowerBlock {
    public PetroPetuniaBlock(Properties pProperties) {
        super(MobEffects.WITHER, 10, pProperties);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PetroPetuniaBlockEntity(
                BotaniaContent.PETRO_PETUNIA_BLOCK_ENTITY.get(),
                pPos,
                pState
        );
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return ManaFlowerBlock.createTickerHelper(
                pBlockEntityType,
                BotaniaContent.PETRO_PETUNIA_BLOCK_ENTITY.get(),
                PetroPetuniaBlockEntity::commonTick
        );
    }
}
