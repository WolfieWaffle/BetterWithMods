package betterwithmods.common.registry.bulk.manager;

import betterwithmods.api.util.StackIngredient;
import betterwithmods.common.BWMItems;
import betterwithmods.common.registry.bulk.recipes.CauldronFoodRecipe;
import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class CauldronManager extends CraftingManagerBulk<CauldronRecipe> {
    private static final CauldronManager instance = new CauldronManager();

    public static CauldronManager getInstance() {
        return instance;
    }

    public CauldronRecipe addRecipe(NonNullList<ItemStack> outputs, StackIngredient... inputs) {
        return addRecipe(outputs, Lists.newArrayList(inputs));
    }

    public CauldronRecipe addRecipe(NonNullList<ItemStack> outputs, List<StackIngredient> inputs) {
        if (outputs.stream().anyMatch(i -> i.getItem() instanceof ItemFood && !i.getItem().equals(BWMItems.KIBBLE))) {
            return addRecipe(new CauldronFoodRecipe(outputs, inputs));
        }
        return addRecipe(new CauldronRecipe(outputs, inputs));
    }


}
