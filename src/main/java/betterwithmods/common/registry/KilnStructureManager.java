package betterwithmods.common.registry;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.tile.TileCamo;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by primetoxinz on 6/6/17.
 */
public class KilnStructureManager {

    public static Set<IBlockState> KILN_BLOCKS = new HashSet<>();

    public static void registerKilnBlock(IBlockState state) {
        KILN_BLOCKS.add(state);
    }

    public static boolean isKilnBlock(IBlockState state) {
        if (state == Blocks.AIR.getDefaultState())
            return false;
        return KILN_BLOCKS.contains(state);
    }

    public static boolean createKiln(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if(!isKilnBlock(state))
            return false;
        if (isValidKiln(world, pos)) {
            IBlockState kiln = BWMBlocks.KILN.getDefaultState();
            world.setBlockState(pos, kiln);
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileCamo) {
                ((TileCamo) tile).setCamoState(state);
                world.notifyBlockUpdate(pos, kiln, kiln, 8);
            }
            return true;
        }
        return false;
    }

    //@Param BlockPos pos - the position of the kiln block
    public static boolean hasHeat(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos.down());
        BWMHeatRegistry.HeatSource source = BWMHeatRegistry.get(state);
        return source != null;
    }

    public static boolean isValidKiln(IBlockAccess world, BlockPos pos) {
        int numBrick = 0;
        BlockPos center = pos.up();
        for (EnumFacing face : EnumFacing.VALUES) {
            if (face == EnumFacing.DOWN) continue;
            IBlockState state = world.getBlockState(center.offset(face));
            if (isKilnBlock(state))
                numBrick++;
        }
        return numBrick > 2 && hasHeat(world, pos);
    }


    public static void removeKilnBlock(IBlockState state) {
        KILN_BLOCKS.remove(state);
    }
}
