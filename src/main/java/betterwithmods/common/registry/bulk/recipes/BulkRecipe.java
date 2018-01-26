package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.api.util.StackIngredient;
import betterwithmods.util.InvUtils;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BulkRecipe implements Comparable<BulkRecipe> {

    @Nonnull
    protected NonNullList<ItemStack> outputs;
    @Nonnull
    protected List<StackIngredient> inputs;

    protected int priority = 0;

    public BulkRecipe() {
    }

    public BulkRecipe(ItemStack output, ItemStack... inputs) {
        this(InvUtils.asNonnullList(output), Arrays.stream(inputs).map(StackIngredient::fromItemStack).collect(Collectors.toList()));
    }

    @Deprecated
    public BulkRecipe(@Nonnull ItemStack output, @Nonnull ItemStack secondaryOutput, ItemStack... inputs) {
        this(InvUtils.asNonnullList(output, secondaryOutput), Arrays.stream(inputs).map(StackIngredient::fromItemStack).collect(Collectors.toList()));
    }

    public BulkRecipe(ItemStack output, List<StackIngredient> inputs) {
        this(InvUtils.asNonnullList(output), inputs);
    }

    @Deprecated
    public BulkRecipe(@Nonnull ItemStack output, @Nonnull ItemStack secondaryOutput, List<StackIngredient> inputs) {
        this(InvUtils.asNonnullList(output, secondaryOutput), inputs);
    }

    public BulkRecipe(NonNullList<ItemStack> outputs, List<StackIngredient> inputs) {
        this.outputs = outputs;
        this.inputs = inputs;
    }

    public NonNullList<ItemStack> onCraft(World world, TileEntity tile, ItemStackHandler inv) {
        this.consumeInvIngredients(inv);
        return outputs;
    }


    public NonNullList<ItemStack> getOutputs() {
        return outputs;
    }

    public List<StackIngredient> getInputs() {
        return this.inputs;
    }

    public boolean matches(ItemStackHandler inv) {
        if (!inputs.isEmpty()) {
            for (StackIngredient ingredient : inputs) {
                boolean available = false;
                for (int i = 0; i < inv.getSlots(); i++) {
                    if (ingredient.apply(inv.getStackInSlot(i))) {
                        available = true;
                        break;
                    }
                }
                if (!available)
                    return false;
            }
            return true;
        }
        return false;
    }

    public boolean consumeInvIngredients(ItemStackHandler inv) {
        if (!inputs.isEmpty()) {
            for (StackIngredient ingredient : inputs) {
                for (int i = 0; i < inv.getSlots(); i++) {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (ingredient.apply(stack)) {
                        if (!InvUtils.consumeItemsInInventory(inv, stack, ingredient.getStackSize(), false))
                            return false;
                    }
                }
            }
            return true;
        }
        return false;
    }


    public boolean isEmpty() {
        return this.inputs.isEmpty() || getOutputs().isEmpty();
    }


    /**
     * Recipes with higher priority will be crafted first.
     *
     * @return sorting priority for Comparable
     */
    public int getPriority() {
        return priority;
    }

    public BulkRecipe setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public int compareTo(BulkRecipe other) {
        return Integer.compare(other.getPriority(), this.getPriority());
    }

    public static class Factory implements IRecipeFactory {


        @Override
        public IRecipe parse(JsonContext context, JsonObject json) {

            JsonArray ingredients = JsonUtils.getJsonArray(json, "ingredients");

            List<StackIngredient> inputs = Lists.newArrayList();
            for(JsonElement element: ingredients) {
                JsonObject obj = element.getAsJsonObject();
                obj.get("item")
            }

            return

        }
    }
}
