package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.api.util.StackIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class MillRecipe extends BulkRecipe {
    private int grindType;

    public MillRecipe(int grindType, @Nonnull ItemStack output, @Nonnull ItemStack secondaryOutput, ItemStack... inputs) {
        super(output, secondaryOutput, inputs);
        this.grindType = grindType;
    }

    public MillRecipe(int grindType, @Nonnull NonNullList<ItemStack> outputs, List<StackIngredient> inputs) {
        super(outputs, inputs);
        this.grindType = grindType;
    }

    public int getGrindType() {
        return grindType;
    }
}
