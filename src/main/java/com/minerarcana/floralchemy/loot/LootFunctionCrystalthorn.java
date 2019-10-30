package com.minerarcana.floralchemy.loot;

import java.util.Random;

import javax.annotation.Nonnull;

import com.google.gson.*;
import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.api.FloralchemyAPI;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class LootFunctionCrystalthorn extends LootFunction {

    protected LootFunctionCrystalthorn(LootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    public ItemStack apply(@Nonnull ItemStack stack, @Nonnull Random rand, @Nonnull LootContext context) {
        Tuple<ResourceLocation, Integer> crystal = FloralchemyAPI.getCrystalRegistry().getCrystals()
                .get(rand.nextInt(FloralchemyAPI.getCrystalRegistry().getCrystals().size()));
        return new ItemStack(
                ForgeRegistries.ITEMS.getValue(Floralchemy.getCrystalthornResourceLocation(crystal)),
                rand.nextInt(4));
    }

    public static class Serializer extends LootFunction.Serializer<LootFunctionCrystalthorn> {
        public Serializer() {
            super(new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn"), LootFunctionCrystalthorn.class);
        }

        @Override
        public void serialize(@Nonnull JsonObject object, @Nonnull LootFunctionCrystalthorn functionClazz,
                @Nonnull JsonSerializationContext serializationContext) {
        }

        @Nonnull
        @Override
        public LootFunctionCrystalthorn deserialize(@Nonnull JsonObject object,
                @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
            return new LootFunctionCrystalthorn(conditionsIn);
        }
    }

}