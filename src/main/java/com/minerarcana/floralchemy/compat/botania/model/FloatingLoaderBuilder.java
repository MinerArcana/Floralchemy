package com.minerarcana.floralchemy.compat.botania.model;

import com.google.gson.JsonObject;
import com.minerarcana.floralchemy.compat.botania.FloralchemyBotaniaContent;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class FloatingLoaderBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {
    private T flower;

    public FloatingLoaderBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(FloralchemyBotaniaContent.rl("floating_flower"), parent, existingFileHelper);
    }

    public FloatingLoaderBuilder<T> flower(T modelBuilder) {
        this.flower = modelBuilder;
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        super.toJson(json);
        json.add("flower", flower.toJson());
        return json;
    }
}
