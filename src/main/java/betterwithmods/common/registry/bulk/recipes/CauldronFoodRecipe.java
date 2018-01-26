package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.api.util.StackIngredient;
import betterwithmods.common.BWMItems;
import betterwithmods.util.InvUtils;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreIngredient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by primetoxinz on 6/24/17.
 */
public class CauldronFoodRecipe extends CauldronRecipe {


    public CauldronFoodRecipe(NonNullList<ItemStack> outputs, List<StackIngredient> inputs) {
        super(outputs,inputs);
    }

    @Override
    public NonNullList<ItemStack> onCraft(World world, TileEntity tile, ItemStackHandler inv) {

        if (shouldFoul(inv)) {
            NonNullList<ItemStack> items = super.onCraft(world, tile, inv);
            items = InvUtils.asNonnullList(items.stream().map(stack -> new ItemStack(BWMItems.FERTILIZER, stack.getCount())).collect(Collectors.toList()));
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack item = inv.getStackInSlot(i);
                if (item.getItem() instanceof ItemFood) {
                    ItemStack fertilizer = new ItemStack(BWMItems.FERTILIZER, item.getCount());
                    inv.setStackInSlot(i, fertilizer);
                }
            }
            return items;
        }
        return super.onCraft(world, tile, inv);
    }

    public boolean shouldFoul(ItemStackHandler inv) {
        return InvUtils.getFirstOccupiedStackOfItem(inv, new OreIngredient("dung")) > -1;
    }
}