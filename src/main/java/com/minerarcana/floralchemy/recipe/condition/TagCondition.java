package com.minerarcana.floralchemy.recipe.condition;

import com.google.gson.JsonObject;
import com.minerarcana.floralchemy.Floralchemy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class TagCondition<T> implements ICondition {
    private static final ResourceLocation NAME = Floralchemy.rl("tag_empty");
    private final TagKey<T> tag;

    public TagCondition(ResourceLocation tag, ResourceKey<? extends Registry<T>> registry) {
        this.tag = TagKey.create(registry, tag);
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test(ICondition.IContext context) {
        return context.getTag(tag).getValues().isEmpty();
    }

    @Override
    @SuppressWarnings("removal")
    public boolean test() {
        return test(IContext.EMPTY);
    }

    @Override
    public String toString() {
        return "tag_empty(\"" + tag.registry() + "\",\"" + tag.location() + "\")";
    }

    public static class Serializer implements IConditionSerializer<TagCondition<?>> {
        public static final TagCondition.Serializer INSTANCE = new TagCondition.Serializer();

        @Override
        public void write(JsonObject json, TagCondition value) {
            json.addProperty("tag", value.tag.location().toString());
            json.addProperty("registry", value.tag.registry().location().toString());
        }

        @Override
        @SuppressWarnings({"rawtypes", "unchecked"})
        public TagCondition<?> read(JsonObject json) {
            return new TagCondition(
                    new ResourceLocation(GsonHelper.getAsString(json, "tag")),
                    ResourceKey.create(
                            Registry.REGISTRY.key(),
                            new ResourceLocation(GsonHelper.getAsString(json, "registry"))
                    )
            );
        }

        @Override
        public ResourceLocation getID() {
            return TagCondition.NAME;
        }
    }
}
