package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.StokedCauldronRecipe;

public class StokedCauldronManager extends CraftingManagerBulk<StokedCauldronRecipe> {
    private static final StokedCauldronManager instance = new StokedCauldronManager();

    public static StokedCauldronManager getInstance() {
        return instance;
    }
}
