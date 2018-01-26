package betterwithmods.common.registry.blockmeta.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class BlockMeta {
    private final Block block;
    private final int meta;

    public BlockMeta(Block block, int meta) {
        this.block = block;
        this.meta = meta;
    }

    public BlockMeta(ItemStack stack) {
        Block block = null;
        if (stack.getItem() instanceof ItemBlock)
            block = ((ItemBlock) stack.getItem()).getBlock();
        this.block = block;
        this.meta = stack.getMetadata();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ItemStack) {
            ItemStack stack = (ItemStack) o;
            Block block = null;
            if (stack.getItem() instanceof ItemBlock)
                block = ((ItemBlock) stack.getItem()).getBlock();
            int meta = stack.getMetadata();
            return equals(block, meta);
        }
        if (o instanceof BlockMeta) {
            return equals(((BlockMeta) o).block, ((BlockMeta) o).meta);
        }
        return false;
    }

    public boolean equals(Block block, int meta) {
        return this.block == block && (this.meta == meta || meta == OreDictionary.WILDCARD_VALUE || this.meta == OreDictionary.WILDCARD_VALUE);
    }


    public ItemStack getStack() {
        if (block == null)
            return ItemStack.EMPTY;
        return new ItemStack(block, 1, meta);
    }

}
