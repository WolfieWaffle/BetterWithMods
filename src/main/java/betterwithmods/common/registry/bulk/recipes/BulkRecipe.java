package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.api.util.StackIngredient;
import betterwithmods.util.InvUtils;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BulkRecipe implements Comparable<BulkRecipe>, IRecipe {

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

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public IRecipe setRegistryName(ResourceLocation name) {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return null;
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return null;
    }


    public static abstract class Factory<T extends BulkRecipe> implements IRecipeFactory {

        protected NonNullList<ItemStack> parseOutputs(JsonContext context, JsonObject json) {
            JsonArray outputs = JsonUtils.getJsonArray(json, "outputs");
            List<ItemStack> recipeOutputs = Lists.newArrayList();
            for (JsonElement element : outputs) {
                recipeOutputs.add(CraftingHelper.getItemStack(element.getAsJsonObject(), context));
            }
            return InvUtils.asNonnullList(recipeOutputs);
        }

        protected List<StackIngredient> parseInputs(JsonContext context, JsonObject json) {
            JsonArray inputs = JsonUtils.getJsonArray(json, "inputs");

            List<StackIngredient> recipeInputs = Lists.newArrayList();
            for (JsonElement element : inputs) {
                JsonObject obj = element.getAsJsonObject();
                JsonElement e = obj.get("item");
                Ingredient base = CraftingHelper.getIngredient(e, context);
                int count = obj.get("count").getAsInt();
                recipeInputs.add(new StackIngredient(base, count));
            }
            return recipeInputs;
        }

        @Override
        public abstract T parse(JsonContext context, JsonObject json);
    }
}
