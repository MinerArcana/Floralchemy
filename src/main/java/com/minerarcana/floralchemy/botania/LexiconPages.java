package com.minerarcana.floralchemy.botania;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.lexicon.BasicLexiconEntry;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class LexiconPages {
    public static LexiconEntry petroPetunia;

    public static void init() {
        petroPetunia = new BasicLexiconEntry(SubTilePetroPetunia.NAME, BotaniaAPI.categoryGenerationFlowers);
        petroPetunia.setLexiconPages(new PageText("0"), new PagePetalRecipe<>("1", BotaniaRecipes.recipePetroPetunia));
        petroPetunia.setKnowledgeType(BotaniaAPI.elvenKnowledge);
    }
}
