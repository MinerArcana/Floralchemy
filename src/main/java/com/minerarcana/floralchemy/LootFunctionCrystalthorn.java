package com.minerarcana.floralchemy;

import java.util.Random;

import javax.annotation.Nonnull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
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
		Tuple<ResourceLocation, Integer> crystal = FloralchemyAPI.getCrystalRegistry().getCrystals().get(rand.nextInt(FloralchemyAPI.getCrystalRegistry().getCrystals().size()));
		return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn_" + crystal.getFirst().getPath())), rand.nextInt(4), crystal.getSecond());
	}

	public static class Serializer extends LootFunction.Serializer<LootFunctionCrystalthorn> {
		protected Serializer() {
			super(new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn"), LootFunctionCrystalthorn.class);
		}

		@Override
		public void serialize(@Nonnull JsonObject object, @Nonnull LootFunctionCrystalthorn functionClazz, @Nonnull JsonSerializationContext serializationContext) {}

		@Nonnull
		@Override
		public LootFunctionCrystalthorn deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull LootCondition[] conditionsIn) {
			return new LootFunctionCrystalthorn(conditionsIn);
		}
	}

}