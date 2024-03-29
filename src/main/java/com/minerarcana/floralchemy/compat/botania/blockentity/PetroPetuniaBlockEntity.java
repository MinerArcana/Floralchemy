package com.minerarcana.floralchemy.compat.botania.blockentity;

import com.minerarcana.floralchemy.compat.botania.FloralchemyBotaniaClient;
import com.minerarcana.floralchemy.compat.botania.block.PetroPetuniaBlock;
import com.minerarcana.floralchemy.content.FloralchemyRecipes;
import com.minerarcana.floralchemy.recipe.FuelInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.wrappers.BucketPickupHandlerWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;

import java.awt.*;
import java.util.Objects;
import java.util.Optional;

public class PetroPetuniaBlockEntity extends TileEntityGeneratingFlower {
    private static final int START_BURN_EVENT = 0;
    private static final String TAG_BURN_TIME = "burn_time";
    private static final String TAG_POWER = "tick_power";
    private static final String TAG_COOL_DOWN = "cool_down";
    private int burnTime;
    private int powerPerTick;
    private int coolDown;

    private ICapabilityProvider capabilityProvider;

    public PetroPetuniaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tickFlower() {
        super.tickFlower();

        if (coolDown > 0) {
            --coolDown;
        }

        if (burnTime > 0) {
            --burnTime;
        }

        if (this.getLevel() != null) {
            if (this.getLevel().isClientSide()) {
                Level level = this.getLevel();
                BlockPos blockPos = this.getBlockPos();
                if (burnTime > 0 && level.random.nextInt(10) == 0) {
                    Vec3 offset = level.getBlockState(blockPos)
                            .getOffset(level, blockPos)
                            .add(0.4, 0.7, 0.4);
                    this.getLevel()
                            .addParticle(
                                    ParticleTypes.SMOKE,
                                    blockPos.getX() + offset.x + Math.random() * 0.2,
                                    blockPos.getY() + offset.y,
                                    blockPos.getZ() + offset.z + Math.random() * 0.2,
                                    0.0D,
                                    0.0D,
                                    0.0D
                            );
                }
            } else if (this.burnTime > 0 && this.powerPerTick > 0) {
                this.addMana(this.powerPerTick);
            }
        }
        if (this.isValidBinding()) {
            if (burnTime <= 0 && coolDown <= 0) {
                if (this.getMana() < this.getMaxMana()) {
                    int tries = 0;
                    boolean foundFluid = false;

                    while (tries < 9 && !foundFluid) {
                        BlockPos checkingPos = this.getBlockPos()
                                .offset(-1 + tries / 3, -1, -1 + tries % 3);
                        Optional<IFluidHandler> fluidHandlerOpt = FluidUtil.getFluidHandler(
                                        this.getLevel(),
                                        checkingPos,
                                        Direction.UP
                                )
                                .resolve()
                                .or(() -> {
                                    Block block = this.getLevel()
                                            .getBlockState(checkingPos)
                                            .getBlock();
                                    if (block instanceof IFluidBlock fluidBlock) {
                                        return Optional.of(new FluidBlockWrapper(
                                                fluidBlock,
                                                level,
                                                checkingPos
                                        ));
                                    } else if (block instanceof BucketPickup bucketPickup) {
                                        return Optional.of(new BucketPickupHandlerWrapper(
                                                bucketPickup,
                                                level,
                                                checkingPos
                                        ));
                                    } else {
                                        return Optional.empty();
                                    }
                                });

                        if (fluidHandlerOpt.isPresent()) {
                            IFluidHandler fluidHandler = fluidHandlerOpt.orElseThrow(IllegalStateException::new);
                            FluidStack grabbedFluid = fluidHandler.drain(1000, FluidAction.SIMULATE);
                            if (!grabbedFluid.isEmpty() && grabbedFluid.getAmount() == 1000) {
                                foundFluid = this.getLevel()
                                        .getRecipeManager()
                                        .getRecipeFor(
                                                FloralchemyRecipes.FUEL_RECIPE_TYPE.get(),
                                                new FuelInventory(
                                                        grabbedFluid
                                                ),
                                                this.getLevel()
                                        ).map(recipe -> {
                                            this.setBurnTime(recipe.getBurnTime());
                                            this.setPowerPerTick(recipe.getManaPerTick());
                                            return true;
                                        })
                                        .orElse(false);
                                if (foundFluid) {
                                    fluidHandler.drain(1000, FluidAction.EXECUTE);
                                    if (!this.getBlockState().getValue(PetroPetuniaBlock.POWERED)) {
                                        this.getLevel().setBlock(
                                                this.getBlockPos(),
                                                this.getBlockState()
                                                        .setValue(PetroPetuniaBlock.POWERED, true),
                                                Block.UPDATE_ALL
                                        );
                                    }
                                    sync();
                                }
                            }
                        }
                        tries++;
                    }

                    if (!foundFluid) {
                        coolDown = 100;
                        if (this.getBlockState().getValue(PetroPetuniaBlock.POWERED)) {
                            this.getLevel().setBlock(
                                    this.getBlockPos(),
                                    this.getBlockState()
                                            .setValue(PetroPetuniaBlock.POWERED, false),
                                    Block.UPDATE_ALL
                            );
                        }
                        this.setPowerPerTick(0);
                    }
                }
            }
        }
    }

    private void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    private void setPowerPerTick(int powerPerTick) {
        this.powerPerTick = powerPerTick;
    }

    @Override
    public boolean triggerEvent(int event, int param) {
        if (event == START_BURN_EVENT) {
            return true;
        } else {
            return super.triggerEvent(event, param);
        }
    }

    @Override
    public int getMaxMana() {
        return 9000;
    }

    @Override
    public RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(this.getBlockPos().below(), 3);
    }

    @Override
    public int getColor() {
        return Color.GREEN.getRGB();
    }

    @Override
    public void writeToPacketNBT(CompoundTag tag) {
        super.writeToPacketNBT(tag);

        tag.putInt(TAG_BURN_TIME, burnTime);
        tag.putInt(TAG_COOL_DOWN, coolDown);
        tag.putInt(TAG_POWER, powerPerTick);
    }

    @Override
    public void readFromPacketNBT(CompoundTag tag) {
        super.readFromPacketNBT(tag);

        burnTime = tag.getInt(TAG_BURN_TIME);
        coolDown = tag.getInt(TAG_COOL_DOWN);
        powerPerTick = tag.getInt(TAG_POWER);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (Objects.requireNonNull(this.getLevel()).isClientSide()) {
            if (this.capabilityProvider == null) {
                this.capabilityProvider = FloralchemyBotaniaClient.createCapabilities(this);
            }
            LazyOptional<T> lazyOptional = this.capabilityProvider.getCapability(cap);
            if (lazyOptional.isPresent()) {
                return lazyOptional;
            }
        }
        return super.getCapability(cap, side);
    }
}
