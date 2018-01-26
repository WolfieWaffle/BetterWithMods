package betterwithmods.common.registry.bulk.manager;

import betterwithmods.api.util.StackIngredient;
import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public abstract class CraftingManagerBulk<T extends BulkRecipe> {
    private List<T> recipes;

    protected CraftingManagerBulk() {
        this.recipes = new ArrayList<>();
    }


    public T addRecipe(T recipe) {
        if (!recipe.isEmpty())
            recipes.add(recipe);
        return recipe;
    }

//    public List<T> findRecipeForRemoval(@Nonnull ItemStack output, @Nonnull ItemStack secondary) {
//        return recipes.stream().filter(recipe -> recipe.matches(output, secondary)).collect(Collectors.toList());
//    }
//
//    public List<T> findRecipeForRemoval(@Nonnull ItemStack output, @Nonnull ItemStack secondary, @Nonnull Object... inputs) {
//        List<T> removed = Lists.newArrayList();
//        List<T> found = findRecipeForRemoval(output, secondary);
//        if (inputs.length > 0) {
//            for (T recipe : found) {
//                boolean match = true;
//                for (Object input : inputs) {
//                    match = hasMatch(input, recipe.getInputs());
//                    if (!match)
//                        break;
//                }
//                if (match)
//                    removed.add(recipe);
//            }
//        } else {
//            removed.addAll(found);
//        }
//        return removed;
//    }


//    public boolean removeRecipe(ItemStack output, ItemStack secondary) {
//        Iterator<T> iterator = recipes.iterator();
//        List<T> remove = findRecipeForRemoval(output, secondary);
//        while (iterator.hasNext()) {
//            T next = iterator.next();
//            if (remove.contains(next))
//                iterator.remove();
//        }
//        return remove.isEmpty();
//    }
//
//    public boolean removeRecipe(ItemStack output, ItemStack secondary, Object... inputs) {
//        Iterator<T> iterator = recipes.iterator();
//        List<T> remove = findRecipeForRemoval(output, secondary, inputs);
//        while (iterator.hasNext()) {
//            T next = iterator.next();
//            if (remove.contains(next))
//                iterator.remove();
//        }
//        return remove.isEmpty();
//    }


    public NonNullList<ItemStack> getCraftingResult(ItemStackHandler inv) {
        T recipe = getMostValidRecipe(inv);
        if (recipe != null) {
            if (recipe.matches(inv)) {
                return recipe.getOutputs();
            }
        }
        return null;
    }

    public T getMostValidRecipe(ItemStackHandler inv) {
        TreeSet<T> recipes = getValidRecipes(inv);
        if (recipes.isEmpty())
            return null;
        return recipes.first();
    }

    private TreeSet<T> getValidRecipes(ItemStackHandler inv) {
        TreeSet<T> recipes = new TreeSet<>();
        for (T recipe : this.recipes) {
            if (recipe.matches(inv))
                recipes.add(recipe);
        }
        return recipes;
    }



    public List<StackIngredient> getValidCraftingIngredients(ItemStackHandler inv) {
        T recipe = getMostValidRecipe(inv);
        if (recipe != null)
            return recipe.getInputs();
        return Lists.newArrayList();
    }

    public NonNullList<ItemStack> craftItem(World world, TileEntity tile, ItemStackHandler inv) {
        T recipe = getMostValidRecipe(inv);
        if (recipe != null) {
            return recipe.onCraft(world, tile, inv);
        }
        return NonNullList.create();
    }

    public List<T> getRecipes() {
        return this.recipes;
    }

}
