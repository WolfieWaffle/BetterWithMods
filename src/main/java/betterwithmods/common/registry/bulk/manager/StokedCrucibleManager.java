package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.StokedCrucibleRecipe;

public class StokedCrucibleManager extends CraftingManagerBulk<StokedCrucibleRecipe> {
    private static final StokedCrucibleManager instance = new StokedCrucibleManager();

    public static StokedCrucibleManager getInstance() {
        return instance;
    }

}
