package com.minerarcana.floralchemy.client;

import java.util.Map;

import com.google.common.collect.Maps;
import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.block.BlockCrystalthorn;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.util.ResourceLocation;

public class StateMapperCrystalthorn implements IStateMapper {
    @Override
    public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
        Map<IBlockState, ModelResourceLocation> map = Maps.newHashMap();
        map.put(blockIn.getDefaultState(),
                new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn"), "normal"));
        for(Integer age : BlockCrystalthorn.AGE.getAllowedValues()) {
            if(age == BlockCrystalthorn.maxAge) {
                map.put(blockIn.getDefaultState().withProperty(BlockCrystalthorn.AGE, age)
                        .withProperty(BlockCrystalthorn.BERRIES, true),
                        new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn"),
                                "age=" + age + ",berries=true"));
            }
            map.put(blockIn.getDefaultState().withProperty(BlockCrystalthorn.AGE, age)
                    .withProperty(BlockCrystalthorn.BERRIES, false),
                    new ModelResourceLocation(new ResourceLocation(Floralchemy.MOD_ID, "crystalthorn"),
                            "age=" + age + ",berries=false"));
        }
        return map;
    }
}