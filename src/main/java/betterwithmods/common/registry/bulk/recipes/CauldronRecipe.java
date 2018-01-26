package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.api.util.StackIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class CauldronRecipe extends BulkRecipe {
    public CauldronRecipe() {
    }

    public CauldronRecipe(ItemStack output, ItemStack... inputs) {
        super(output, inputs);
    }

    public CauldronRecipe(ItemStack output, List<StackIngredient> inputs) {
        super(output, inputs);
    }

    public CauldronRecipe(NonNullList<ItemStack> outputs, List<StackIngredient> inputs) {
        super(outputs, inputs);
    }
}
