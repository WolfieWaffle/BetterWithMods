package betterwithmods.common.registry.bulk.manager;

import betterwithmods.common.registry.bulk.recipes.CrucibleRecipe;

public class CrucibleManager extends CraftingManagerBulk<CrucibleRecipe> {
    private static final CrucibleManager instance = new CrucibleManager();

    public static CrucibleManager getInstance() {
        return instance;
    }

    @Override
    public CrucibleRecipe addRecipe(CrucibleRecipe recipe) {
        return super.addRecipe(recipe);
    }
}
