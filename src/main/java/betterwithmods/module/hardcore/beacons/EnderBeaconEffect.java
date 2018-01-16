package betterwithmods.module.hardcore.beacons;

import betterwithmods.api.util.ITickEffect;
import betterwithmods.common.blocks.tile.TileEnderchest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnderBeaconEffect implements ITickEffect {
    @Override
    public void tick(World world, BlockPos pos, int level) {
        int r;
        for (r = -1; r < level; r++) {
            for (int x = -(r + 1); x <= (r + 1); x++) {
                for (int z = -(r + 1); z <= (r + 1); z++) {
                    if (Math.abs(x) > r || Math.abs(z) > r) {
                        BlockPos p = pos.add(x, -r, z);
                        TileEntity tile = world.getTileEntity(p);
                        if (tile instanceof TileEnderchest) {
                            TileEnderchest.Type type = TileEnderchest.Type.VALUES[r + 1];
                            if (type != null) {
                                ((TileEnderchest) tile).setType(type);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, int level) {
        int r;
        for (r = 0; r <= level; r++) {
            for (int x = -(r + 1); x <= (r + 1); x++) {
                for (int z = -(r + 1); z <= (r + 1); z++) {
                    if (Math.abs(x) > r || Math.abs(z) > r) {
                        BlockPos p = pos.add(x, -r, z);
                        TileEntity tile = world.getTileEntity(p);
                        if (tile instanceof TileEnderchest) {
                            ((TileEnderchest) tile).setType(TileEnderchest.Type.NONE);
                        }
                    }
                }
            }
        }
    }
}
