package com.minerarcana.floralchemy.event;

import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.recipe.condition.TagCondition;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Floralchemy.ID, bus = Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void registerConditions(RegistryEvent.Register<RecipeSerializer<?>> event) {
        CraftingHelper.register(TagCondition.Serializer.INSTANCE);
    }
}
