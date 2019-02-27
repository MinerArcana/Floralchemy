package com.minerarcana.floralchemy;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.util.ResourceLocation;

public class StateMapperCrystalthorn implements IStateMapper {

	@Override
	public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
		Map<IBlockState, ModelResourceLocation> map = Maps.newHashMap();
		map.put(blockIn.getDefaultState(), new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn"), "normal"));
		return map;
	}

}
