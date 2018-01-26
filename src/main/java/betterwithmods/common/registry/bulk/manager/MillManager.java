package betterwithmods.common.registry.bulk.manager;

import betterwithmods.api.util.StackIngredient;
import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class MillManager extends CraftingManagerBulk<MillRecipe> {
    private static final MillManager instance = new MillManager();

    public static MillManager getInstance() {
        return instance;
    }

    public void addRecipe(int grindType, NonNullList<ItemStack> outputs, List<StackIngredient> inputs) {
        addRecipe(new MillRecipe(grindType, outputs, inputs));
    }

    @Override
    public MillRecipe addRecipe(MillRecipe recipe) {
        return super.addRecipe(recipe);
    }
}
