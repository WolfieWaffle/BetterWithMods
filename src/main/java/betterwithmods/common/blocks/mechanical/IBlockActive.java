package betterwithmods.common.blocks.mechanical;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by primetoxinz on 7/19/17.
 */
public interface IBlockActive {
    PropertyBool ACTIVE = PropertyBool.create("active");

    default boolean isActive(IBlockState state) {
        return state.getValue(ACTIVE);
    }

    default void setActive(World world, BlockPos pos, boolean active) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof IBlockActive) {
            world.setBlockState(pos, state.withProperty(ACTIVE, active));
            world.scheduleUpdate(pos, state.getBlock(),0);
        }
    }

}
