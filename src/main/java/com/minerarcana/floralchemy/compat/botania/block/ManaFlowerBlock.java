package com.minerarcana.floralchemy.compat.botania.block;

import com.minerarcana.floralchemy.Floralchemy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
public abstract class ManaFlowerBlock extends FlowerBlock implements EntityBlock {
    private static final RegistryObject<Block> RED_STRING_RELAY = RegistryObject.create(
            new ResourceLocation("botania", "red_string_relay"),
            Registry.BLOCK_REGISTRY,
            Floralchemy.ID
    );
    private static final VoxelShape SHAPE = box(4.8, 0, 4.8, 12.8, 16, 12.8);

    public ManaFlowerBlock(MobEffect pSuspiciousStewEffect, int pEffectDuration, Properties pProperties) {
        super(pSuspiciousStewEffect, pEffectDuration, pProperties);
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        Vec3 shift = state.getOffset(world, pos);
        return SHAPE.move(shift.x, shift.y, shift.z);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected boolean mayPlaceOn(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return state.is(RED_STRING_RELAY.get()) || super.mayPlaceOn(state, blockGetter, pos);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean triggerEvent(BlockState state, Level pLevel, BlockPos pos, int event, int param) {
        super.triggerEvent(state, pLevel, pos, event, param);
        BlockEntity blockEntity = pLevel.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(event, param);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof TileEntitySpecialFlower blockEntity) {
            blockEntity.setPlacedBy(level, pos, state, placer, stack);
        }
    }

    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> actualType,
            BlockEntityType<E> expectedType,
            BlockEntityTicker<? super E> entityTicker
    ) {
        return expectedType == actualType ? (BlockEntityTicker<A>) entityTicker : null;
    }
}
