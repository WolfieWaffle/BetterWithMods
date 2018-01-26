package betterwithmods.api.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class StackIngredient extends Ingredient {
    private Ingredient base;
    private int stackSize;

    public StackIngredient(Ingredient base, int stackSize) {
        this.base = base;
        this.stackSize = stackSize;
    }

    public StackIngredient(Ingredient base) {
        this(base,1);
    }

    @Override
    public boolean apply(@Nullable ItemStack stack) {
        return stack != null && base.apply(stack) && stack.getCount() >= stackSize;
    }

    public int getStackSize() {
        return stackSize;
    }

    public static StackIngredient fromItemStack(ItemStack stack) {
        return new StackIngredient(Ingredient.fromStacks(stack), stack.getCount());
    }
}
