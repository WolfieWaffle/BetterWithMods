package betterwithmods.common.blocks.behaviors;

import betterwithmods.api.tile.dispenser.IBehaviorCollect;
import betterwithmods.util.InvUtils;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Created by primetoxinz on 5/25/17.
 */
public class BehaviorBreakBlock implements IBehaviorCollect {
    @Override
    public NonNullList<ItemStack> collect(IBlockSource source) {
        NonNullList<ItemStack> list = InvUtils.asNonnullList(source.getBlockState().getBlock().getDrops(source.getWorld(), source.getBlockPos(), source.getBlockState(), 0));;
        breakBlock(source.getWorld(),source.getBlockState(),source.getBlockPos());
        return list;
    }
}
