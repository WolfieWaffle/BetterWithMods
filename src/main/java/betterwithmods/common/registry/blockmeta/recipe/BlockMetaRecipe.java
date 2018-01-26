package betterwithmods.common.registry.blockmeta.recipe;

import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

/**
 * Purpose:
 *
 * @author primetoxinz
 * @version 11/11/16
 */
public class BlockMetaRecipe extends BlockMeta{
    private final NonNullList<ItemStack> outputs;

    public BlockMetaRecipe(ItemStack stack, List<ItemStack> outputs) {
        super(stack);
        this.outputs = InvUtils.asNonnullList(outputs);
    }

    public BlockMetaRecipe(Block block, int meta, List<ItemStack> outputs) {
        this(block, meta, InvUtils.asNonnullList(outputs));
    }

    public BlockMetaRecipe(Block block, int meta, NonNullList<ItemStack> outputs) {
        super(block,meta);
        this.outputs = outputs;
    }

    public NonNullList<ItemStack> getOutputs() {
        return outputs;
    }

    @Override
    public String toString() {
        return String.format("%s-> %s", getStack(), getOutputs());
    }
}