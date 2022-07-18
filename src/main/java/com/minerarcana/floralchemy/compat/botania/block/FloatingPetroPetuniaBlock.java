package com.minerarcana.floralchemy.compat.botania.block;

import com.minerarcana.floralchemy.compat.botania.FloralchemyBotaniaContent;
import com.minerarcana.floralchemy.compat.botania.blockentity.PetroPetuniaBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.common.block.BlockFloatingSpecialFlower;

import javax.annotation.ParametersAreNonnullByDefault;

public class FloatingPetroPetuniaBlock extends BlockFloatingSpecialFlower {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public FloatingPetroPetuniaBlock(Properties pProperties) {
        super(pProperties, null);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(POWERED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(POWERED);
    }

    @NotNull
    @Override
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PetroPetuniaBlockEntity(
                FloralchemyBotaniaContent.PETRO_PETUNIA_BLOCK_ENTITY.get(),
                pPos,
                pState
        );
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return ManaFlowerBlock.createTickerHelper(
                pBlockEntityType,
                FloralchemyBotaniaContent.PETRO_PETUNIA_BLOCK_ENTITY.get(),
                PetroPetuniaBlockEntity::commonTick
        );
    }
}
