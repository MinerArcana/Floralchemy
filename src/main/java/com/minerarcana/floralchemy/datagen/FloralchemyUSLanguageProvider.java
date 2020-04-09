package com.minerarcana.floralchemy.datagen;

import com.hrznstudio.titanium.registry.BlockRegistryObjectGroup;
import com.minerarcana.floralchemy.Floralchemy;
import com.minerarcana.floralchemy.block.BlockHedge;
import com.minerarcana.floralchemy.content.FloralchemyBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;

public class FloralchemyUSLanguageProvider extends LanguageProvider {
    public FloralchemyUSLanguageProvider(DataGenerator gen) {
        super(gen, Floralchemy.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add("itemGroup.floralchemy", "Floralchemy");
        this.add("guide.floralchemy.name", "Hedgemage's Handbook");
        this.add("guide.floralchemy.landing_text", "Your guide to flower power");
        //region Blocks
        for(BlockRegistryObjectGroup<BlockHedge, BlockItem, ?> hedge : FloralchemyBlocks.HEDGES) {
            this.addBlock(hedge, StringUtils.capitaliseAllWords(hedge.get().getType().replace("_", " ")) + " Hedge");
        }
        this.addBlock(FloralchemyBlocks.LEAKY_CAULDRON, "Leaky Cauldron");
        //endregion
    }
}
