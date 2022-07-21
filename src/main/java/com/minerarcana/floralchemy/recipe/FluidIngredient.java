package com.minerarcana.floralchemy.recipe;

import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FluidIngredient implements Predicate<FluidStack> {
    public static final FluidIngredient EMPTY = new FluidIngredient(new Value[0]);
    private final FluidIngredient.Value[] values;
    @Nullable
    private FluidStack[] fluidStacks;

    public FluidIngredient(Value[] values) {
        this.values = values;
    }

    private void dissolve() {
        if (this.fluidStacks == null) {
            this.fluidStacks = Arrays.stream(this.values)
                    .flatMap((fluidValue) -> fluidValue.getFluids()
                            .stream()
                    )
                    .distinct()
                    .toArray(FluidStack[]::new);
        }
    }

    @Override
    public boolean test(@Nullable FluidStack testFluidStack) {
        if (testFluidStack == null) {
            return false;
        } else {
            this.dissolve();
            if (this.fluidStacks.length == 0) {
                return testFluidStack.isEmpty();
            } else {
                for (FluidStack fluidStack : this.fluidStacks) {
                    if (fluidStack != null && testFluidStack.containsFluid(fluidStack)) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public void toNetwork(FriendlyByteBuf pBuffer) {
        this.dissolve();
        pBuffer.writeCollection(Arrays.asList(this.fluidStacks), FriendlyByteBuf::writeFluidStack);
    }

    public JsonElement toJson() {
        if (this.values.length == 1) {
            return this.values[0].serialize();
        } else {
            JsonArray jsonarray = new JsonArray();

            for (FluidIngredient.Value ingredientValue : this.values) {
                jsonarray.add(ingredientValue.serialize());
            }

            return jsonarray;
        }
    }

    public static FluidIngredient fromNetwork(FriendlyByteBuf pBuffer) {
        return new FluidIngredient(
                pBuffer.readCollection(
                                ArrayList::new,
                                buf -> new FluidIngredient.FluidValue(buf.readFluidStack())
                        )
                        .toArray(FluidValue[]::new)
        );
    }

    public static FluidIngredient fromJson(JsonElement pJson) {
        if (pJson != null && !pJson.isJsonNull()) {
            if (pJson.isJsonObject()) {
                return fromValues(Stream.of(valueFromJson(pJson.getAsJsonObject())));
            } else if (pJson.isJsonArray()) {
                JsonArray jsonArray = pJson.getAsJsonArray();
                if (jsonArray.size() == 0) {
                    throw new JsonSyntaxException("Fluid array cannot be empty, at least one item must be defined");
                } else {
                    return fromValues(StreamSupport.stream(jsonArray.spliterator(), false)
                            .map((element) -> valueFromJson(GsonHelper.convertToJsonObject(element, "fluid"))));
                }
            } else {
                throw new JsonSyntaxException("Expected item to be object or array of objects");
            }
        } else {
            throw new JsonSyntaxException("Item cannot be null");
        }
    }

    public static FluidIngredient.Value valueFromJson(JsonObject pJson) {
        if (pJson.has("fluid") && pJson.has("tag")) {
            throw new JsonParseException("An ingredient entry is either a tag or an fluid, not both");
        } else if (pJson.has("fluid")) {
            Fluid item = itemFromJson(pJson);
            return new FluidIngredient.FluidValue(new FluidStack(item, 1000));
        } else if (pJson.has("tag")) {
            ResourceLocation resourcelocation = new ResourceLocation(GsonHelper.getAsString(pJson, "tag"));
            TagKey<Fluid> tagKey = TagKey.create(Registry.FLUID_REGISTRY, resourcelocation);
            return new FluidIngredient.TagValue(tagKey);
        } else {
            throw new JsonParseException("An ingredient entry needs either a tag or an item");
        }
    }

    public static Fluid itemFromJson(JsonObject pItemObject) {
        String s = GsonHelper.getAsString(pItemObject, "fluid");
        Fluid item = Optional.ofNullable(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(s)))
                .orElseThrow(() -> new JsonSyntaxException("Unknown fluid '" + s + "'"));
        if (item == Fluids.EMPTY) {
            throw new JsonSyntaxException("Invalid fluid: " + s);
        } else {
            return item;
        }
    }

    public static FluidIngredient fromValues(Stream<? extends FluidIngredient.Value> pStream) {
        FluidIngredient ingredient = new FluidIngredient(pStream.toArray(FluidIngredient.Value[]::new));
        return ingredient.values.length == 0 ? EMPTY : ingredient;
    }

    public static FluidIngredient of(Fluid... pItems) {
        return of(Arrays.stream(pItems)
                .map((fluid) -> new FluidStack(fluid, 1000)));
    }

    public static FluidIngredient of(TagKey<Fluid> pTag) {
        return fromValues(Stream.of(new FluidIngredient.TagValue(pTag)));
    }

    public static FluidIngredient of(Stream<FluidStack> pStacks) {
        return fromValues(pStacks.filter((fluidStack) -> !fluidStack.isEmpty())
                .map(FluidIngredient.FluidValue::new)
        );
    }

    public static class FluidValue implements FluidIngredient.Value {
        private final FluidStack fluidStack;

        public FluidValue(FluidStack pItem) {
            this.fluidStack = pItem;
        }

        public Collection<FluidStack> getFluids() {
            return Collections.singleton(this.fluidStack);
        }

        public JsonObject serialize() {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty(
                    "fluid",
                    Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(this.fluidStack.getFluid()))
                            .toString()
            );
            return jsonobject;
        }
    }

    public record TagValue(
            TagKey<Fluid> tag
    ) implements FluidIngredient.Value {
        public Collection<FluidStack> getFluids() {
            List<FluidStack> list = Lists.newArrayList();

            for (Fluid holder : Objects.requireNonNull(ForgeRegistries.FLUIDS.tags()).getTag(this.tag)) {
                list.add(new FluidStack(holder, 1000));
            }

            return list;
        }

        public JsonObject serialize() {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("tag", this.tag.location().toString());
            return jsonobject;
        }
    }

    interface Value {
        Collection<FluidStack> getFluids();

        JsonObject serialize();
    }
}
